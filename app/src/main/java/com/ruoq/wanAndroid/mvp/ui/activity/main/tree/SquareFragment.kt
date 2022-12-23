package com.ruoq.wanAndroid.mvp.ui.activity.main.tree

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.jess.arms.di.component.AppComponent
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.ruoq.wanAndroid.R
import com.ruoq.wanAndroid.app.event.AddEvent
import com.ruoq.wanAndroid.app.event.CollectEvent
import com.ruoq.wanAndroid.app.event.LoginFreshEvent
import com.ruoq.wanAndroid.app.event.SettingChangeEvent
import com.ruoq.wanAndroid.app.utils.RecyclerViewUtils
import com.ruoq.wanAndroid.app.utils.SettingUtil
import com.ruoq.wanAndroid.app.weight.DefineLoadMoreView
import com.ruoq.wanAndroid.app.weight.loadCallBack.EmptyCallback
import com.ruoq.wanAndroid.app.weight.loadCallBack.ErrorCallback
import com.ruoq.wanAndroid.app.weight.loadCallBack.LoadingCallBack
import com.ruoq.wanAndroid.databinding.FragmentListBinding
import com.ruoq.wanAndroid.di.component.tree.DaggerSquareComponent
import com.ruoq.wanAndroid.di.module.main.tree.SquareModule
import com.ruoq.wanAndroid.mvp.contract.main.tree.SquareContract
import com.ruoq.wanAndroid.mvp.model.entity.ApiPagerResponse
import com.ruoq.wanAndroid.mvp.model.entity.ArticleResponse
import com.ruoq.wanAndroid.mvp.presenter.main.tree.SquarePresenter
import com.ruoq.wanAndroid.mvp.ui.adapter.ArticleAdapter
import com.ruoq.wanAndroid.mvp.ui.base.BaseFragment
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/8 17:07
 * @Description : 广场
 */
class SquareFragment: BaseFragment<SquarePresenter>(),SquareContract.View {
    private var initPageNO = 0 //主要广场的页码是从0开始的
    var pageNo = initPageNO  //当前页码
    lateinit var loadSir : LoadService<Any>
    lateinit var adapter :ArticleAdapter
    lateinit var footView:DefineLoadMoreView
    private var _binding:FragmentListBinding ?= null
    private val binding get() = _binding!!

    companion object{
        fun newInstance():SquareFragment{
            return SquareFragment()
        }
    }
    override fun setupFragmentComponent(appComponent: AppComponent) {
        //如果找不到该类，请编译一下项目
        DaggerSquareComponent
            .builder()
            .appComponent(appComponent)
            .squareModule(SquareModule(this))
            .build()
            .inject(this)
    }

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater,container,false)
        loadSir = LoadSir.getDefault().register(binding.swipeRefreshLayout){
            loadSir.showCallback(LoadingCallBack::class.java)
            //点击重试时请求
            mPresenter?.getSquareData(pageNo)

        }.apply {
            SettingUtil.setLoadingColor(_mActivity,this)
        }
        return binding.root
    }

    override fun initData(savedInstanceState: Bundle?) {
        binding.swipeRefreshLayout.run{
            //设置颜色
            setColorSchemeColors(SettingUtil.getColor(_mActivity))
            //设置刷新监听回调
            setOnRefreshListener {
                pageNo = initPageNO
                mPresenter?.getSquareData(pageNo)
            }
        }

        binding.floatbtn.run{
          backgroundTintList = SettingUtil.getOneColorStateList(_mActivity)
          setOnClickListener {
              val layoutManager = binding.swiperecyclerview.layoutManager as LinearLayoutManager
              //如果当前Recyclerview最后一个视图位置的索引大于等于40，则迅速返回顶部
              //否则带有滚动效果返回顶部
              if(layoutManager.findLastVisibleItemPosition() >= 40){
                  binding.swiperecyclerview.scrollToPosition(0)
              }else{
                  binding.swiperecyclerview.smoothScrollToPosition(0)
              }
          }
        }


        //初始化Recyclerview
        footView = RecyclerViewUtils().initRecyclerView(_mActivity,binding.swiperecyclerview,SwipeRecyclerView.LoadMoreListener {
            //加载更多
            mPresenter?.getSquareData(pageNo)
        }).apply {
            setLoadViewColor(SettingUtil.getOneColorStateList(_mActivity))
        }

        binding.swiperecyclerview.run{
            layoutManager = LinearLayoutManager(_mActivity)
            setHasFixedSize(true)
            //监听Recyclerview滑动到顶部的时候，需要把向上返回的顶部的按钮隐藏
            addOnScrollListener(object: RecyclerView.OnScrollListener(){
                @SuppressLint("RestrictedApi")
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if(!canScrollVertically(-1)){
                        binding.floatbtn.visibility = View.INVISIBLE
                    }
                }
            })
        }

        //初始化适配器
        adapter = ArticleAdapter(mutableListOf(),true).apply {
            if(SettingUtil.getListMode(_mActivity) != 0){
                openLoadAnimation(SettingUtil.getListMode(_mActivity))
            }else{
                closeLoadAnimation()
            }

            setOnItemClickListener{_, view, position ->
//                launchActivity(Intent(_mActivity,WebViewActivity::class.java).apply {
//                    putExtras(Bundle().apply{
//                        putSerializable("data",this@SquareFragment.adapter.data[position])
//                        putString("tag",this@SquareFragment::class.java.simpleName)
//                        putInt("position",position)
//                    })
//                })
            }
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        //设置适配器
        binding.swiperecyclerview.adapter = adapter
        //设置加载中
        loadSir.showCallback(LoadingCallBack::class.java)
        //请求数据
        mPresenter?.getSquareData(pageNo)
    }


    override fun requestDataSuccess(apiPagerResponse: ApiPagerResponse<MutableList<ArticleResponse>>) {
        binding.swipeRefreshLayout.isRefreshing = false
        if(pageNo == initPageNO && apiPagerResponse.datas.size == 0){
            //如果是第一页并且没有数据，则显示空布局
            loadSir.showCallback(EmptyCallback::class.java)
        }else if(pageNo == initPageNO){
            loadSir.showSuccess()
            //如果是刷新的话，floatButton就要隐藏了，因为这时候肯定是要在顶部的
            binding.floatbtn.visibility = View.INVISIBLE
            adapter.setNewData(apiPagerResponse.datas)
        }else{
            //不是第一页，且有数据
            loadSir.showSuccess()
            adapter.addData(apiPagerResponse.datas)
        }

        pageNo ++
        if(apiPagerResponse.pageCount >= pageNo){
            //如果总条数大于等于当前页数时，还有更多数据
            binding.swiperecyclerview.loadMoreFinish(false,true)
        }else{
            //没有更多数据
            //解释一下为什么这里要延时0.2秒操作。。。
            //因为上面的adapter.addData(data) 数据刷新了适配器，是需要等待时间的，还没刷新完，这里就已经执行了没有更多数据
            //所以在界面上会出现一个小bug，刷新最后一页的时候，没有更多数据啦提示先展示出来了，然后才会加载出请求到的数据
            //暂时还没有找到好的方法，就用这个处理一下，如果觉得没什么影响的可以去掉这个延时操作，或者有更好的解决方式可以告诉我一下
            binding.swiperecyclerview.postDelayed({
                binding.swiperecyclerview.loadMoreFinish(false,false)
            },200)
        }


    }


    override fun requestDataFailed(errorMsg: String) {
        with(binding){
            swipeRefreshLayout.isRefreshing = false
            if(pageNo == initPageNO){
                //如果页码是初始页，说明是刷新，界面切换成错误页
                loadSir.setCallBack(ErrorCallback::class.java){_, view ->
                    //设置错误页文字提示
                    view.findViewById<TextView>(R.id.error_text).text = errorMsg

                }
                //设置错误
                loadSir.showCallback(ErrorCallback::class.java)

            }else{
                //页码不是0，说明加载更多时出现加载错误，设置Recyclerview加载错误
                binding.swiperecyclerview.loadMoreError(0,errorMsg)
            }
        }
    }

    override fun collect(collected: Boolean, position: Int) {
        CollectEvent(collected,adapter.data[position].id).post()
    }

    /**
     * 接收到登录或退出的EventBus 刷新数据
     */
    @SuppressLint("NotifyDataSetChanged")
    @Subscribe
    fun freshLogin(event:LoginFreshEvent){
        //如果是登录了，当前界面的数据与账号收藏集合id匹配的值需要设置已经收藏
        if(event.login){
            event.collectIds.forEach{
                for(item in adapter.data){
                    if(item.id == it.toInt()){
                        item.collect = true
                        break
                    }
                }
            }
        }else{
            //退出了，把所有的收藏全部变成未收藏
            for(item in adapter.data){
                item.collect = false
            }
        }
        adapter.notifyDataSetChanged()
    }

    /**
     * 在详情中收藏是，接收到Event Bus
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
            }.run {
                if(await() != -1){
                    adapter.notifyItemChanged(await())
                }
            }
        }
    }

    /**
     * 接收到添加了分享文章的通知
     */
    @Subscribe
    fun addEvent(event: AddEvent){
        if(event.code== AddEvent.SHARE_CODE ||event.code==AddEvent.DELETE_CODE){
            //刷新
            binding.swipeRefreshLayout.isRefreshing = true
            pageNo = initPageNO
            mPresenter?.getSquareData(pageNo)
        }
    }

    /**
     * 接收到event时，重新设置当前界面控件的主题颜色和一些其他配置
     */
//    @Subscribe
//    fun settingEvent(event: SettingChangeEvent) {
//        setUiTheme(_mActivity, listOf(floatbtn,swipeRefreshLayout,loadsir,footView,adapter))
//    }
}