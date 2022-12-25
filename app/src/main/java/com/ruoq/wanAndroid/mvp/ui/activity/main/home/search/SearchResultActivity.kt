package com.ruoq.wanAndroid.mvp.ui.activity.main.home.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseViewHolder
import com.jess.arms.di.component.AppComponent
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.ruoq.wanAndroid.R
import com.ruoq.wanAndroid.app.event.CollectEvent
import com.ruoq.wanAndroid.app.event.LoginFreshEvent
import com.ruoq.wanAndroid.app.utils.RecyclerViewUtils
import com.ruoq.wanAndroid.app.utils.SettingUtil
import com.ruoq.wanAndroid.app.weight.CollectView
import com.ruoq.wanAndroid.app.weight.DefineLoadMoreView
import com.ruoq.wanAndroid.app.weight.loadCallBack.EmptyCallback
import com.ruoq.wanAndroid.app.weight.loadCallBack.ErrorCallback
import com.ruoq.wanAndroid.app.weight.loadCallBack.LoadingCallBack
import com.ruoq.wanAndroid.databinding.ActivitySearchResultBinding
import com.ruoq.wanAndroid.di.component.home.search.DaggerSearchResultComponent
import com.ruoq.wanAndroid.di.module.main.home.search.SearchResultModule
import com.ruoq.wanAndroid.mvp.contract.main.home.search.SearchResultContract
import com.ruoq.wanAndroid.mvp.model.entity.ApiPagerResponse
import com.ruoq.wanAndroid.mvp.model.entity.ArticleResponse
import com.ruoq.wanAndroid.mvp.presenter.main.home.search.SearchResultPresenter
import com.ruoq.wanAndroid.mvp.ui.activity.main.web.WebViewActivity
import com.ruoq.wanAndroid.mvp.ui.adapter.ArticleAdapter
import com.ruoq.wanAndroid.mvp.ui.base.BaseActivity
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/8 17:05
 * @Description : 搜索结果
 */
class SearchResultActivity:BaseActivity<SearchResultPresenter>(),SearchResultContract.View {
    private var initPageNo = 0 //注意，页码从0开始的
    var pageNo = initPageNo
    lateinit var  loadSir : LoadService<Any>
    lateinit var searchKey:String //搜索关键词
    lateinit var articleAdapter:ArticleAdapter
    private var footView:DefineLoadMoreView ?= null
    private var _binding: ActivitySearchResultBinding?= null
    private val binding get()= _binding!!
    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerSearchResultComponent.builder()
            .appComponent(appComponent)
            .searchResultModule(SearchResultModule(this))
            .build()
            .inject(this)
    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return  0
    }

    override fun initData(savedInstanceState: Bundle?) {
        _binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        searchKey =  intent.getStringExtra("searchKey").toString()
        binding.toolBar.run{
            setSupportActionBar(this)
            title = searchKey
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener {
                finish()
            }
        }

        //绑定loadSir
        loadSir = LoadSir.getDefault().register(binding.swipeRefreshLayout){
            loadSir.showCallback(LoadingCallBack::class.java)
            pageNo = initPageNo
            mPresenter?.getAriList(pageNo,searchKey)
        }.apply {
            SettingUtil.setLoadingColor(this@SearchResultActivity,this)
            showCallback(LoadingCallBack::class.java)
        }

        //初始化adapter，并设置监听
        articleAdapter = ArticleAdapter(arrayListOf(),true).apply {
            if(SettingUtil.getListMode(this@SearchResultActivity) != 0){
                openLoadAnimation(SettingUtil.getListMode(this@SearchResultActivity))
            }else{
                closeLoadAnimation()
            }

            setOnCollectViewClickListener(object:ArticleAdapter.OnCollectViewClickListener{
                override fun onClick(helper: BaseViewHolder, v: CollectView, position: Int) {
                    //点击爱心收藏执行操作
                    if(v.isChecked){
                        mPresenter?.unCollect(data[position].id,position)
                    }else{
                        mPresenter?.collect(data[position].id,position)
                    }
                }

            })

            //点击了整行
            setOnItemClickListener{_, view ,position ->
                val intent = Intent(this@SearchResultActivity,WebViewActivity::class.java)
                val bundle = Bundle().also{
                    it.putSerializable("data",data[position])
                    it.putString("tag",this@SearchResultActivity::class.java.simpleName)
                    it.putInt("position",position)
                }
                intent.putExtras(bundle)
                startActivity(intent)
            }

            binding.floatbtn.run{
                backgroundTintList = SettingUtil.getOneColorStateList(this@SearchResultActivity)
                setOnClickListener{
                    val layoutManager = binding.swiperecyclerview.layoutManager as LinearLayoutManager
                    if(layoutManager.findLastVisibleItemPosition() >= 40){
                        binding.swiperecyclerview.scrollToPosition(0)
                    }else{
                        binding.swiperecyclerview.smoothScrollToPosition(0)
                    }
                }
            }

            //初始化SwipeRefreshLayout
            binding.swipeRefreshLayout.run{
                setColorSchemeColors(SettingUtil.getColor(this@SearchResultActivity))
                setOnRefreshListener {
                    //刷新
                    pageNo = initPageNo
                    mPresenter?.getAriList(pageNo,searchKey)
                }
            }

            //初始化Recyclerview
            footView = RecyclerViewUtils().initRecyclerView(this@SearchResultActivity,binding.swiperecyclerview,SwipeRecyclerView.LoadMoreListener {
                //加载更多
                mPresenter?.getAriList(pageNo,searchKey)
            }).apply {
                setLoadViewColor(SettingUtil.getOneColorStateList(this@SearchResultActivity))
            }
        }

        //监听Recyclerview滑动到顶部时，需要把向上返回顶部的按钮隐藏
        binding.swiperecyclerview.run{
            adapter = articleAdapter
            addOnScrollListener(object: RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if(!canScrollVertically(-1)){
                        binding.floatbtn.visibility = View.INVISIBLE
                    }
                }
            })
        }

        mPresenter?.getAriList(pageNo,searchKey) //发起请求

    }

    /**
     * 获取文章数据成功
     */
    override fun requestArticleListSuccess(articles: ApiPagerResponse<MutableList<ArticleResponse>>) {
        binding.swipeRefreshLayout.isRefreshing = false
        if(pageNo == initPageNo && articles.datas.size == 0){
            //如果是第一页，并且没有数据，显示空布局
            loadSir.showCallback(EmptyCallback::class.java)
        }else if(pageNo == initPageNo){
            loadSir.showSuccess()
            //如果是刷新的话，floatButton就要隐藏了，因为这个时候肯定要在顶部的
            binding.floatbtn.visibility = View.INVISIBLE
            articleAdapter.setNewData(articles.datas)
        }else{
            //不是第一页
            loadSir.showSuccess()
            articleAdapter.addData(articles.datas)
        }

        pageNo++

        if(articles.pageCount >= pageNo){
            //如果总条数大于当前页数时，还有更多数据
            binding.swiperecyclerview.loadMoreFinish(false,true)
        }else{
            //没有更多数据
            //没有更多数据
            binding.swiperecyclerview.postDelayed({
                //解释一下为什么这里要延时0.2秒操作。。。
                //因为上面的adapter.addData(data) 数据刷新了适配器，是需要时间的，还没刷新完，这里就已经执行了没有更多数据
                //所以在界面上会出现一个小bug，刷新最后一页的时候，没有更多数据啦提示先展示出来了，然后才会加载出请求到的数据
                //暂时还没有找到好的方法，就用这个处理一下，如果觉得没什么影响的可以去掉这个延时操作，或者有更好的解决方式可以告诉我一下
                binding.swiperecyclerview.loadMoreFinish(false, false)
            }, 200)
        }
    }

    override fun requestArticleFailed(errorMsg: String) {
        binding.swipeRefreshLayout.isRefreshing = false
        if (pageNo == initPageNo) {
            //如果页码是 初始页 说明是刷新，界面切换成错误页
            loadSir.setCallBack(ErrorCallback::class.java) { _, view ->
                //设置错误页文字错误提示
                view.findViewById<TextView>(R.id.error_text).text = errorMsg
            }
            //设置错误
            loadSir.showCallback(ErrorCallback::class.java)
        } else {
            //页码不是0 说明是加载更多时出现的错误，设置recyclerview加载错误，
            binding.swiperecyclerview.loadMoreError(0, errorMsg)
        }
    }

    /**
     * 收藏文章回调
     */
    override fun collect(collected: Boolean, position: Int) {
        CollectEvent(collected, articleAdapter.data[position].id).post()
    }

    /**
     * 接收到登录或退出的EventBus 刷新数据
     */
    @Subscribe
    fun freshLogin(event: LoginFreshEvent) {
        //如果是登录了， 当前界面的数据与账户收藏集合id匹配的值需要设置已经收藏
        if (event.login) {
            event.collectIds.forEach {
                for (item in articleAdapter.data) {
                    if (item.id == it.toInt()) {
                        item.collect = true
                        break
                    }
                }
            }
        } else {
            //退出了，把所有的收藏全部变为未收藏
            for (item in articleAdapter.data) {
                item.collect = false
            }
        }
        articleAdapter.notifyDataSetChanged()
    }

    /**
     * 在详情中收藏时，接收到EventBus
     */
    @Subscribe
    fun collectChange(event: CollectEvent) {
        //使用协程做耗时操作
        GlobalScope.launch {
            async {
                var indexResult = -1
                for (index in articleAdapter.data.indices) {
                    if (articleAdapter.data[index].id == event.id) {
                        articleAdapter.data[index].collect = event.collect
                        indexResult = index
                        break
                    }
                }
                indexResult
            }.run {
                if (await() != -1) {
                    articleAdapter.notifyItemChanged(await())
                }
            }
        }

    }
}