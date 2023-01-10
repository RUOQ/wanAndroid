package com.ruoq.wanAndroid.mvp.ui.activity.main.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bingoogolapple.bgabanner.BGABanner
import com.chad.library.adapter.base.BaseViewHolder
import com.jess.arms.di.component.AppComponent
import com.jess.arms.http.imageloader.glide.ImageConfigImpl
import com.jess.arms.utils.ArmsUtils
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.ruoq.wanAndroid.R
import com.ruoq.wanAndroid.app.event.CollectEvent
import com.ruoq.wanAndroid.app.event.LoginFreshEvent
import com.ruoq.wanAndroid.app.event.SettingChangeEvent
import com.ruoq.wanAndroid.app.utils.RecyclerViewUtils
import com.ruoq.wanAndroid.app.utils.SettingUtil
import com.ruoq.wanAndroid.app.weight.CollectView
import com.ruoq.wanAndroid.app.weight.DefineLoadMoreView
import com.ruoq.wanAndroid.app.weight.loadCallBack.EmptyCallback
import com.ruoq.wanAndroid.app.weight.loadCallBack.ErrorCallback
import com.ruoq.wanAndroid.app.weight.loadCallBack.LoadingCallBack
import com.ruoq.wanAndroid.databinding.FragmentHomeBinding
import com.ruoq.wanAndroid.di.component.home.DaggerHomeComponent
import com.ruoq.wanAndroid.di.module.main.home.HomeModule
import com.ruoq.wanAndroid.mvp.contract.main.home.HomeContract
import com.ruoq.wanAndroid.mvp.model.entity.ApiPagerResponse
import com.ruoq.wanAndroid.mvp.model.entity.ArticleResponse
import com.ruoq.wanAndroid.mvp.model.entity.BannerResponse

import com.ruoq.wanAndroid.mvp.presenter.main.home.HomePresenter
import com.ruoq.wanAndroid.mvp.ui.activity.main.home.search.SearchActivity
import com.ruoq.wanAndroid.mvp.ui.activity.main.web.WebViewActivity
import com.ruoq.wanAndroid.mvp.ui.adapter.ArticleAdapter
import com.ruoq.wanAndroid.mvp.ui.base.BaseFragment
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe

import javax.inject.Inject

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/8 17:05
 * @Description : 文件描述
 */
class HomeFragment : BaseFragment<HomePresenter>(),HomeContract.View{
    private var initPageNo = 0 //  注意，主页的页码从0开始的
    var pageNo = initPageNo
    private var _binding:FragmentHomeBinding ?= null
    private val binding get() = _binding!!

    @Inject
    lateinit var adapter:ArticleAdapter
    lateinit var loadSir: LoadService<Any>
    lateinit var footView:DefineLoadMoreView
    companion object{
        fun newInstance():HomeFragment {
            return HomeFragment()
        }
    }

    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerHomeComponent.builder()
            .appComponent(appComponent)
            .homeModule(HomeModule(this))
            .build()
            .inject(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //加上这句话，menu才会显示出来
        setHasOptionsMenu(true)
    }
    @SuppressLint("LogNotTimber")
    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        loadSir = LoadSir.getDefault().register(binding.swipeRefreshLayout){
            //界面加载失败，或者没有数据时，点击重试的监听
            loadSir.showCallback(LoadingCallBack::class.java)
            pageNo = initPageNo
            mPresenter?.getBanner()
            mPresenter?.getAriList(pageNo)
            Log.e("qin","getAriList")
        }.apply {
            SettingUtil.setLoadingColor(_mActivity,this)
        }
        return binding.root
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        binding.toolBar.run{
            setBackgroundColor(SettingUtil.getColor(_mActivity))
            title = "首页"
            inflateMenu(R.menu.home_menu)
            setOnMenuItemClickListener{
                when(it.itemId){
                    R.id.home_search ->{
                        launchActivity(Intent(_mActivity,SearchActivity::class.java))
                    }

                }
                true
            }
        }

        adapter.apply {
            if(SettingUtil.getListMode(_mActivity) != 0){
                openLoadAnimation(SettingUtil.getListMode(_mActivity))
            }else{
                closeLoadAnimation()
            }
            setOnCollectViewClickListener(object:ArticleAdapter.OnCollectViewClickListener{
                override fun onClick(helper: BaseViewHolder, v: CollectView, position: Int) {
                    //点击爱心收藏执行操作
                    if(v.isChecked){
                        //注意了，这里因为我的Recyclerview添加了头部
                        //所以这个索引需要减去头部的count才是实际的position
                        mPresenter?.uncollect(data[position - binding.swiperecyclerview.headerCount].id,
                        position-binding.swiperecyclerview.headerCount)
                    }else{
                        mPresenter?.collect(data[position-binding.swiperecyclerview.headerCount].id,
                        position-binding.swiperecyclerview.headerCount)
                    }
                }
            })

            setOnItemClickListener{_, view, position ->
                //点击了整行
                //注意了，这里因为首页的Recyclerview添加了头部，所以这个索引需要减去头部的数量
                val intent = Intent(_mActivity, WebViewActivity::class.java)
                val bundle = Bundle().also {
                    it.putSerializable("data",data[position-binding.swiperecyclerview.headerCount])
                    it.putString("tag",this@HomeFragment::class.java.simpleName)
                    it.putInt("position", position - binding.swiperecyclerview.headerCount)
                }
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }

        binding.floatbtn.run{
            backgroundTintList = SettingUtil.getOneColorStateList(_mActivity)
            setOnClickListener {
                val layoutManager = binding.swiperecyclerview.layoutManager as LinearLayoutManager
                //如果当前Recyclerview最后一个视图位置的索引大于40 则迅速返回顶部，否则带有动画效果缓慢地返回顶部
                if(layoutManager.findLastVisibleItemPosition() >= 40){
                    binding.swiperecyclerview.scrollToPosition(0)
                }else{
                    binding.swiperecyclerview.smoothScrollToPosition(0) //带动画效果较慢
                }
            }
        }

        //
        binding.swipeRefreshLayout.run{
            setColorSchemeColors(SettingUtil.getColor(_mActivity))
            setOnRefreshListener {
                //刷新
                pageNo = initPageNo
                mPresenter?.getAriList(pageNo)
            }
        }

        //初始化Recyclerview
        footView = RecyclerViewUtils().initRecyclerView(_mActivity,binding.swiperecyclerview,SwipeRecyclerView.LoadMoreListener {
            //加载更多
            mPresenter?.getAriList(pageNo)
        }).apply {
            setLoadViewColor(SettingUtil.getOneColorStateList(_mActivity))
        }

        binding.swiperecyclerview.run{
            //监听recyclerview滑动到顶部的时候，需要把向上返回顶部的按钮隐藏
            addOnScrollListener(object :RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if(!canScrollVertically(-1)){
                        binding.floatbtn.visibility = View.INVISIBLE
                    }
                }
            })
        }
    }


    /**
     * 懒加载，只有该Fragment获得视图时才会调用
     */
    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        loadSir.showCallback(LoadingCallBack::class.java) //默认设置界面加载中
        binding.swiperecyclerview.adapter = adapter
        mPresenter?.getBanner()
        mPresenter?.getAriList(pageNo)
    }


    //获取Banner数据成功
    override fun requestBannerSuccess(banners: MutableList<BannerResponse>) {
        val view = LayoutInflater.from(_mActivity).inflate(R.layout.include_banner,null)
        val banner = view.findViewById<BGABanner>(R.id.banner)
        banner.run {
            setAdapter(BGABanner.Adapter<ImageView, BannerResponse>{ _:BGABanner, view:ImageView, banner:BannerResponse?, i:Int->
                ArmsUtils.obtainAppComponentFromContext(_mActivity)
                    .imageLoader()
                    .loadImage(_mActivity.applicationContext,
                        ImageConfigImpl
                            .builder()
                            .url(banner?.imagePath)
                            .imageView(view)
                            .isCrossFade(true)
                            .build())
            })

            setDelegate{_, _, _, positoin ->
                launchActivity(Intent(_mActivity,WebViewActivity::class.java).apply {
                    putExtras(Bundle().apply {
                        putSerializable("bannerdata",banners[positoin])
                        putString("tag","banner")
                    })
                })

            }
            setData(banners,null)
        }

        //将Banner添加到Recyclerview的头部
        if(binding.swiperecyclerview.headerCount == 0){
            binding.swiperecyclerview.addHeaderView(view)
        }
    }


    //获取文章数据成功
    override fun requestArticleSuccess(articles: ApiPagerResponse<MutableList<ArticleResponse>>) {
        binding.swipeRefreshLayout.isRefreshing = false
        if(pageNo == initPageNo && articles.datas.size == 0){
            //如果是第一页，并且没有数据，页面提示空布局
            loadSir.showCallback(EmptyCallback::class.java)
        }else if(pageNo == initPageNo){
            loadSir.showSuccess()
            //如果是刷新的话，FloatButton就要隐藏了，因为这个时候肯定在顶部
            binding.floatbtn.visibility = View.INVISIBLE
            adapter.setNewData(articles.datas)
        }else{
            //不是第一页
            loadSir.showSuccess()
            adapter.addData(articles.datas)
        }
        pageNo++
        if(articles.pageCount >= pageNo){
            //如果总条数大于当前页数是，还有更多数据
            binding.swiperecyclerview.loadMoreFinish(false,true)
        }else{
            //没有更多数据啦
            //没有更多数据
            binding.swiperecyclerview.postDelayed({
                //解释一下为什么这里要延时0.2秒操作。。。
                //因为上面的adapter.addData(data) 数据刷新了适配器，是需要时间的，还没刷新完，这里就已经执行了没有更多数据
                //所以在界面上会出现一个小bug，刷新最后一页的时候，没有更多数据啦提示先展示出来了，然后才会加载出请求到的数据
                //暂时还没有找到好的方法，就用这个处理一下，如果觉得没什么影响的可以去掉这个延时操作，或者有更好的解决方式可以告诉我一下
                binding.swiperecyclerview.loadMoreFinish(false,false)
            }, 200)

        }


    }

    /**
     * 获取文章数据失败
     */
    override fun requestArticleFailed(errorMsg: String) {
        binding.swipeRefreshLayout.isRefreshing = false
        if(pageNo == initPageNo){
            loadSir.setCallBack(ErrorCallback::class.java){_, view ->
                view.findViewById<TextView>(R.id.error_text).text = errorMsg
            }
            // 设置错误
            loadSir.showCallback(ErrorCallback::class.java)
        }else{
            //页码不是0，说明加载更多时出现了错误，设置Recyclerview加载错误
            binding.swiperecyclerview.loadMoreError(0, errorMsg)
        }
    }


    /**
     * 收藏文章回调
     */
    override fun collect(collected: Boolean, position: Int) {
        CollectEvent(collected,adapter.data[position].id).post()
    }

    /**
     * 接收到登录或退出的EventBus,刷新数据
     */
    @SuppressLint("NotifyDataSetChanged")
    @Subscribe
    fun freshLogin(event:LoginFreshEvent){
        if(event.login){
            event.collectIds.forEach {
                for(item in adapter.data){
                    if(item.id == it.toInt()){
                        item.collect = true
                        break
                    }
                }
            }
        }else{
            //推出了，把所有的收藏变成未收藏
            for(item in adapter.data){
                item.collect = false
            }
        }
        adapter.notifyDataSetChanged()
    }

    /**
     * 接收到收藏文章的event
     */
    @Subscribe
    fun collectChange(event:CollectEvent){
        //使用协程做耗时操作
        GlobalScope.launch {
            async {
                var indexResult = -1
                for(index in adapter.data.indices){
                    if(adapter.data[index].id == event.id){
                        adapter.data[index].collect = event.collect
                        indexResult = index
                        break
                    }
                }
                indexResult
            }.run{
                if(await() != -1){
                    adapter.notifyItemChanged(await())
                }
            }
        }
    }

    /**
     * 接收到event时，重新设置当前界面控件的主题颜色和其他的一些配置
     */
    @Subscribe
    fun settingEvent(event:SettingChangeEvent){
        //
    }
}