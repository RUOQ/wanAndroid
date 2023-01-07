package com.ruoq.wanAndroid.mvp.ui.activity.collect

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseViewHolder
import com.jess.arms.di.component.AppComponent
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.ruoq.wanAndroid.R
import com.ruoq.wanAndroid.app.event.CollectEvent
import com.ruoq.wanAndroid.app.utils.RecyclerViewUtils
import com.ruoq.wanAndroid.app.utils.SettingUtil
import com.ruoq.wanAndroid.app.weight.CollectView
import com.ruoq.wanAndroid.app.weight.DefineLoadMoreView
import com.ruoq.wanAndroid.app.weight.loadCallBack.EmptyCallback
import com.ruoq.wanAndroid.app.weight.loadCallBack.ErrorCallback
import com.ruoq.wanAndroid.app.weight.loadCallBack.LoadingCallBack
import com.ruoq.wanAndroid.databinding.FragmentListBinding
import com.ruoq.wanAndroid.di.component.collect.DaggerCollectComponent
import com.ruoq.wanAndroid.di.module.collect.CollectModule
import com.ruoq.wanAndroid.mvp.contract.collect.CollectContract
import com.ruoq.wanAndroid.mvp.model.entity.ApiPagerResponse
import com.ruoq.wanAndroid.mvp.model.entity.CollectResponse
import com.ruoq.wanAndroid.mvp.presenter.collect.CollectPresenter
import com.ruoq.wanAndroid.mvp.ui.activity.main.web.WebViewActivity
import com.ruoq.wanAndroid.mvp.ui.adapter.CollectAdapter
import com.ruoq.wanAndroid.mvp.ui.base.BaseFragment
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import org.greenrobot.eventbus.Subscribe

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2023/1/4 17:42
 * @Description : 文件描述
 */
class CollectArticleFragment: BaseFragment<CollectPresenter>(),CollectContract.View {
    lateinit var loadSir: LoadService<Any>
    lateinit var adapter: CollectAdapter
    var initPageNo = 0  //分页页码初始值，收藏文章列表页码从0开始
    var pageNo = initPageNo
    private var footView :DefineLoadMoreView ?= null
    private var _binding:FragmentListBinding ?= null
    private val binding get() = _binding!!


    companion object{
        fun newInstance():CollectArticleFragment{
            return CollectArticleFragment()
        }
    }

    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerCollectComponent.builder()
            .appComponent(appComponent)
            .collectModule(CollectModule(this))
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
                pageNo = initPageNo
            mPresenter?.getCollectDataByType(pageNo)
        }.apply {
            SettingUtil.setLoadingColor(_mActivity,this)
        }
        return binding.root
    }


    override fun initData(savedInstanceState: Bundle?) {
        with(binding){
            swipeRefreshLayout.run{
                //设置颜色
                setColorSchemeColors(SettingUtil.getColor(_mActivity))
                //设置刷新监听回调
                setOnRefreshListener {
                    pageNo = initPageNo
                    mPresenter?.getCollectDataByType(pageNo)
                }
            }

            floatbtn.run{
                backgroundTintList = SettingUtil.getOneColorStateList(_mActivity)
                setOnClickListener {
                    val layoutManager = swiperecyclerview.layoutManager as LinearLayoutManager
                    if(layoutManager.findLastVisibleItemPosition() >= 40){
                        swiperecyclerview.scrollToPosition(0)
                    }else{
                        swiperecyclerview.smoothScrollToPosition(0)
                    }
                }
            }

            //初始化footView
            footView = RecyclerViewUtils().initRecyclerView(_mActivity,swiperecyclerview,SwipeRecyclerView.LoadMoreListener {
                mPresenter?.getCollectDataByType(pageNo)
            })

            //初始化Recyclerview
            swiperecyclerview.run{
                layoutManager = LinearLayoutManager(_mActivity)
                setHasFixedSize(true)
                //监听Recyclerview滑动到顶部的时候，需要把向上的顶部按钮隐藏
                addOnScrollListener(object:RecyclerView.OnScrollListener(){
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if(!canScrollVertically(-1)){
                            floatbtn.visibility = View.INVISIBLE
                        }
                    }
                })
            }

            //初始化adapter
            adapter = CollectAdapter(arrayListOf()).apply {
                if(SettingUtil.getListMode(_mActivity) != 0){
                    openLoadAnimation(SettingUtil.getListMode(_mActivity))
                }else{
                    closeLoadAnimation()
                }

                //点击爱心收藏执行操作
                setOnCollectViewClickListener(object:CollectAdapter.OnCollectViewClickListener{
                    override fun onClick(helper: BaseViewHolder, v: CollectView, position: Int) {
                        mPresenter?.uncollect(
                            adapter.data[position].id,
                            adapter.data[position].originId,
                            position)
                    }
                })

                setOnItemClickListener { _, view, position ->
                    val intent = Intent(_mActivity,WebViewActivity::class.java)
                    val bundle = Bundle().apply {
                        putSerializable("collect", adapter.data[position])
                        putString("tag",this@CollectArticleFragment::class.java.simpleName)
                        putInt("position",position)
                    }
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
            }
        }
    }


    //当视图对用户可见时调用，进行数据的懒加载，避免请求过多数据
    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        binding.swiperecyclerview.adapter = adapter //设置适配器
        loadSir.showCallback(LoadingCallBack::class.java) //设置加载中
        mPresenter?.getCollectDataByType(pageNo) //请求数据
    }


    override fun requestDataSuccess(apiPagerResponse: ApiPagerResponse<MutableList<CollectResponse>>) {
        with(binding){
            swipeRefreshLayout.isRefreshing = false
            if(pageNo == initPageNo && apiPagerResponse.datas.size == 0){
                //如果是第一页，并且没有数据，页面提示空布局
                loadSir.showCallback(EmptyCallback::class.java)
            }else if(pageNo == initPageNo){
                loadSir.showSuccess()
                //如果是刷新的话，floatButton就要隐藏了，因为这个时候肯定要在顶部的
                floatbtn.visibility = View.INVISIBLE
                adapter.setNewData(apiPagerResponse.datas)
            }else{
                //不是第一页，并且有数据
                loadSir.showSuccess()
                adapter.addData(apiPagerResponse.datas)
            }
            pageNo++

            if (apiPagerResponse.pageCount >= pageNo) {
                //如果总条数大于等于当前页数时 还有更多数据
                swiperecyclerview.loadMoreFinish(false, true)
            } else {
                //没有更多数据
                swiperecyclerview.postDelayed({
                    //解释一下为什么这里要延时0.2秒操作。。。
                    //因为上面的adapter.addData(data) 数据刷新了适配器，是需要等待时间的，还没刷新完，这里就已经执行了没有更多数据
                    //所以在界面上会出现一个小bug，刷新最后一页的时候，没有更多数据啦提示先展示出来了，然后才会加载出请求到的数据
                    //暂时还没有找到好的方法，就用这个处理一下，如果觉得没什么影响的可以去掉这个延时操作，或者有更好的解决方式可以告诉我一下
                    swiperecyclerview.loadMoreFinish(false, false)
                }, 200)
            }
        }
    }

    override fun requestDataFailed(errorMsg: String) {
        binding.swipeRefreshLayout.isRefreshing = false
        if(pageNo == initPageNo){
            //如果页码是初始页，说明是刷新，界面切换成错误页
            loadSir.setCallBack(ErrorCallback::class.java){_, view ->
                //设置错误页文字错误提示
                view.findViewById<TextView>(R.id.error_text).text = errorMsg
            }

            //设置错误
            loadSir.showCallback(ErrorCallback::class.java)
        }
    }


    override fun unCollect(position: Int) {
        //通知点其他页面刷新一下这个数据
        CollectEvent(false,adapter.data[position].originId,this::class.java.simpleName).post()
        //当收藏数据大于1条的时候直接删除
        if(adapter.data.size > 1){
            adapter.remove(position)
        }else{
            //小于或者等于1条时，不要删除了，直接给页面设置成空页面
            loadSir.showCallback(EmptyCallback::class.java)
        }
    }

    override fun unCollectFailed(position: Int) {
        adapter.notifyItemChanged(position)
    }


    /**
     * 在详情中收藏时，接收到EventBus
     */
    @Subscribe
    fun collectChange(event: CollectEvent) {
        //如果tag不是当前类名，需要刷新
        if (this::class.java.simpleName != event.tag) {
            binding.swipeRefreshLayout.isRefreshing = true
            pageNo = initPageNo
            mPresenter?.getCollectDataByType(pageNo)
        }
    }
}