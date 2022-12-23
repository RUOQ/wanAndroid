package com.ruoq.wanAndroid.mvp.ui.activity.main.tree

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jess.arms.di.component.AppComponent
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.ruoq.wanAndroid.app.utils.SettingUtil
import com.ruoq.wanAndroid.app.utils.SpaceItemDecoration
import com.ruoq.wanAndroid.app.weight.loadCallBack.ErrorCallback
import com.ruoq.wanAndroid.app.weight.loadCallBack.LoadingCallBack
import com.ruoq.wanAndroid.databinding.FragmentListBinding
import com.ruoq.wanAndroid.di.component.tree.DaggerSystemComponent
import com.ruoq.wanAndroid.di.module.main.tree.SystemModule
import com.ruoq.wanAndroid.mvp.contract.main.tree.SystemContract
import com.ruoq.wanAndroid.mvp.model.entity.SystemResponse
import com.ruoq.wanAndroid.mvp.ui.activity.main.tree.treeInfo.TreeInfoActivity
import com.ruoq.wanAndroid.mvp.ui.adapter.SystemAdapter
import com.ruoq.wanAndroid.mvp.ui.base.BaseFragment
import me.hegj.wandroid.mvp.presenter.main.tree.SystemPresenter
import net.lucode.hackware.magicindicator.buildins.UIUtil

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/8 17:07
 * @Description : 文件描述
 */
class SystemFragment: BaseFragment<SystemPresenter>(),SystemContract.View {
    lateinit var loadSir: LoadService<Any>
    lateinit var adapter: SystemAdapter
    private var _binding :FragmentListBinding ?= null
    private val binding:FragmentListBinding get() = _binding!!

    companion object{
        fun newInstance():SystemFragment{
            return SystemFragment()
        }

    }
    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerSystemComponent
            .builder()
            .appComponent(appComponent)
            .systemModule(SystemModule(this))
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
            mPresenter?.getSystemData()
        }.apply {
            SettingUtil.setLoadingColor(_mActivity,this)
        }
        return binding.root
    }

    override fun initData(savedInstanceState: Bundle?) {
        //初始化swipeRefreshLayout
        with(binding){
            swipeRefreshLayout.run{
                //设置颜色
                setColorSchemeColors(SettingUtil.getColor(_mActivity))
                //设置刷新监听回调
                setOnRefreshListener {
                    mPresenter?.getSystemData()
                }
            }

            floatbtn.run{
                backgroundTintList = SettingUtil.getOneColorStateList(_mActivity)
                setOnClickListener {
                    val layoutManager =swiperecyclerview.layoutManager as LinearLayoutManager
                    //如果当前Recycleview最后一个视图的索引大于40，则迅速返回顶部，否则带有滚动效果返回顶部
                    if(layoutManager.findLastVisibleItemPosition() >= 40){
                        swiperecyclerview.scrollToPosition(0)
                    }else{
                        swiperecyclerview.smoothScrollToPosition(0)
                    }
                }
            }

            //初始化Recycler view
            swiperecyclerview.run{
                layoutManager = LinearLayoutManager(_mActivity)
                setHasFixedSize(true)
                //设置item的行间距
                addItemDecoration(SpaceItemDecoration(0,UIUtil.dip2px(_mActivity,8.0)))
                //监听recycler View滑动到顶部的时候，需要把向上返回顶部的按钮隐藏
                addOnScrollListener(object:RecyclerView.OnScrollListener(){
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if(!canScrollVertically(-1)){
                            binding.floatbtn.visibility = View.INVISIBLE
                        }
                    }
                })
            }

            adapter = SystemAdapter(mutableListOf()).apply {
                if(SettingUtil.getListMode(_mActivity ) != 0){
                    openLoadAnimation()
                }else{
                    closeLoadAnimation()
                }

                setOnItemClickListener{_,view,position ->
                    launchActivity(Intent(_mActivity,TreeInfoActivity::class.java).apply {
                        putExtras(Bundle().apply {
                            putSerializable("data",adapter.data[position])
                            putInt("position",0)
                        })
                    })
                }

                //设置点击Tag的回调
                setTagClickListener(object:SystemAdapter.TagClicklistener{
                    override fun onClick(position: Int, childPosition: Int) {
                        launchActivity(Intent(_mActivity,TreeInfoActivity::class.java).apply {
                            putExtras(Bundle().apply {
                                putSerializable("data",adapter.data[position])
                                    putInt("position",childPosition)
                            })
                        })
                    }

                })
            }
        }

    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        binding.swiperecyclerview.adapter = adapter //设置适配器
        loadSir.showCallback(LoadingCallBack::class.java)  //设置加载中
        mPresenter?.getSystemData() //请求数据
    }

    override fun getSystemDataSucc(data: MutableList<SystemResponse>) {
        binding.floatbtn.visibility = View.INVISIBLE
        if (data.size == 0) {
            //集合大小为0 说明肯定是第一次请求数据并且请求失败了，因为只要请求成功过一次就会有缓存数据
            loadSir.showCallback(ErrorCallback::class.java)
        } else {
            binding.swipeRefreshLayout.isRefreshing = false
            loadSir.showCallback(SuccessCallback::class.java)
            adapter.setNewData(data)
        }
    }
}