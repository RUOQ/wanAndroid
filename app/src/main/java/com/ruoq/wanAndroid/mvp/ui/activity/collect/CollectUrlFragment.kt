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
import com.ruoq.wanAndroid.app.utils.SettingUtil
import com.ruoq.wanAndroid.app.utils.SpaceItemDecoration
import com.ruoq.wanAndroid.app.weight.CollectView
import com.ruoq.wanAndroid.app.weight.loadCallBack.EmptyCallback
import com.ruoq.wanAndroid.app.weight.loadCallBack.ErrorCallback
import com.ruoq.wanAndroid.app.weight.loadCallBack.LoadingCallBack
import com.ruoq.wanAndroid.databinding.FragmentListBinding
import com.ruoq.wanAndroid.di.component.collect.DaggerCollectUrlComponent
import com.ruoq.wanAndroid.di.module.collect.CollectUrlModule
import com.ruoq.wanAndroid.mvp.contract.collect.CollectUrlContract
import com.ruoq.wanAndroid.mvp.model.entity.CollectUrlResponse
import com.ruoq.wanAndroid.mvp.presenter.collect.CollectUrlPresenter
import com.ruoq.wanAndroid.mvp.ui.activity.main.web.WebViewActivity
import com.ruoq.wanAndroid.mvp.ui.adapter.CollectUrlAdapter
import com.ruoq.wanAndroid.mvp.ui.base.BaseFragment
import net.lucode.hackware.magicindicator.buildins.UIUtil
import org.greenrobot.eventbus.Subscribe

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2023/1/4 17:42
 * @Description : 收藏
 */
class CollectUrlFragment: BaseFragment<CollectUrlPresenter>(),CollectUrlContract.View{
    lateinit var loadSir:LoadService<Any>
    lateinit var adapter:CollectUrlAdapter
    private var _binding:FragmentListBinding ?= null
    private val binding get() = _binding!!

    companion object{
        fun  newInstance():CollectUrlFragment{
            return CollectUrlFragment()
        }
    }
    override fun setupFragmentComponent(appComponent: AppComponent) {
         DaggerCollectUrlComponent.builder()
             .appComponent(appComponent)
             .collectUrlModule(CollectUrlModule(this))
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
            mPresenter?.getCollectUrlData()
        }.apply {
            SettingUtil.setLoadingColor(_mActivity,this)
        }
        return binding.root
    }

    override fun initData(savedInstanceState: Bundle?) {
        with(binding) {
            swipeRefreshLayout.run {
                setColorSchemeColors(SettingUtil.getColor(_mActivity))
                //设置刷新监听回调
                setOnRefreshListener {
                    mPresenter?.getCollectUrlData()
                }
            }

            floatbtn.run {
                backgroundTintList = SettingUtil.getOneColorStateList(_mActivity)
                setOnClickListener {
                    val layoutManager = swiperecyclerview.layoutManager as LinearLayoutManager
                    if (layoutManager.findLastVisibleItemPosition() >= 40) {
                        swiperecyclerview.scrollToPosition(0)
                    } else {
                        swiperecyclerview.smoothScrollToPosition(0)
                    }
                }
            }

            //初始化Recyclerview
            swiperecyclerview.run {
                layoutManager = LinearLayoutManager(_mActivity)
                setHasFixedSize(true)
                //设置Item的行间距
                addItemDecoration(SpaceItemDecoration(0, UIUtil.dip2px(_mActivity, 8.0)))
                //监听recyclerview滑动到顶部的守，需要把向上返回顶部的按钮隐藏
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (!canScrollVertically(-1)) {
                            floatbtn.visibility = View.INVISIBLE
                        }
                    }
                }
                )
            }

            //初始化adapter
            adapter = CollectUrlAdapter(arrayListOf()).apply {
                if (SettingUtil.getListMode(_mActivity) != 0) {
                    openLoadAnimation(SettingUtil.getListMode(_mActivity))
                } else {
                    closeLoadAnimation()
                }

                //点击爱心的收藏执行操作
                setOnCollectViewClickListener(object : CollectUrlAdapter.OnCollectViewClickListener {
                    override fun onClick(helper: BaseViewHolder, v: CollectView, position: Int) {
                        mPresenter?.unCollect(adapter.data[position].id,position)
                    }
                })

                //点击了整行
                setOnItemClickListener{_, view, position ->
                    val intent = Intent(_mActivity, WebViewActivity::class.java)
                    val bundle = Bundle().apply {
                        putSerializable("collectUrl",adapter.data[position])
                        putString("tag",this@CollectUrlFragment::class.java.simpleName)
                        putInt("position",position)
                    }
                    intent.putExtras(bundle)
                    startActivity(intent)
                }


            }

        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        binding.swiperecyclerview.adapter= adapter
        loadSir.showCallback(LoadingCallBack::class.java)
        mPresenter?.getCollectUrlData() //请求数据
    }

    override fun requestDataUrlSuccess(apiPagerResponse: MutableList<CollectUrlResponse>) {
        binding.swipeRefreshLayout.isRefreshing = false
        if(apiPagerResponse.size == 0){
            //如果没有数据，页面提示空布局
            loadSir.showCallback(EmptyCallback::class.java)
        }else{
            //有数据
            loadSir.showSuccess()
            adapter.setNewData(apiPagerResponse)
        }
    }

    override fun requestDataFailed(errorMsg: String) {
        binding.swipeRefreshLayout.isRefreshing = false
        loadSir.setCallBack(ErrorCallback::class.java) { _, view ->
            //设置错误页文字错误提示
            view.findViewById<TextView>(R.id.error_text).text = errorMsg
            //设置错误
            loadSir.showCallback(ErrorCallback::class.java)
        }
    }

    override fun unCollect(position: Int) {
        //当前收藏数据大于1条的时候，直接删除
        if (adapter.data.size > 1) {
            adapter.remove(position)
        } else {
            //小于等于1条时，不要删除了，直接给界面设置成空数据
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
        binding.swipeRefreshLayout.isRefreshing = true
        mPresenter?.getCollectUrlData()
    }


}