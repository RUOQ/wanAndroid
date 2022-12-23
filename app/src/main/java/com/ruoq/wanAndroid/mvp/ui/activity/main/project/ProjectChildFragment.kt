package com.ruoq.wanAndroid.mvp.ui.activity.main.project

import android.annotation.SuppressLint
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
import com.ruoq.wanAndroid.app.event.LoginFreshEvent
import com.ruoq.wanAndroid.app.event.SettingChangeEvent
import com.ruoq.wanAndroid.app.utils.RecyclerViewUtils
import com.ruoq.wanAndroid.app.utils.SettingUtil
import com.ruoq.wanAndroid.app.utils.setUiTheme
import com.ruoq.wanAndroid.app.weight.CollectView
import com.ruoq.wanAndroid.app.weight.DefineLoadMoreView
import com.ruoq.wanAndroid.app.weight.loadCallBack.EmptyCallback
import com.ruoq.wanAndroid.app.weight.loadCallBack.ErrorCallback
import com.ruoq.wanAndroid.app.weight.loadCallBack.LoadingCallBack
import com.ruoq.wanAndroid.databinding.FragmentListBinding
import com.ruoq.wanAndroid.di.component.project.DaggerProjectChildComponent
import com.ruoq.wanAndroid.di.module.main.project.ProjectChildModule
import com.ruoq.wanAndroid.mvp.contract.main.project.ProjectChildContract
import com.ruoq.wanAndroid.mvp.model.entity.ApiPagerResponse
import com.ruoq.wanAndroid.mvp.model.entity.ArticleResponse
import com.ruoq.wanAndroid.mvp.presenter.main.project.ProjectChildPresenter
import com.ruoq.wanAndroid.mvp.ui.activity.main.web.WebViewActivity
import com.ruoq.wanAndroid.mvp.ui.adapter.ArticleAdapter
import com.ruoq.wanAndroid.mvp.ui.base.BaseFragment
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/8 17:06
 * @Description : 文件描述
 */
class ProjectChildFragment: BaseFragment<ProjectChildPresenter>(),
    ProjectChildContract.View {
    lateinit var loadSir: LoadService<Any>
    lateinit var adapter:ArticleAdapter
    private var cid:Int = 0 //项目分类Id
    private var isNew = false //是否最新项目
    private var initPageNo:Int = 1 //初始化页码，因为最新项目和其他分类的初始页码不一样，最新为0，分类项目为1
    private var pageNo:Int = 1 //当前页码
    lateinit var footView:DefineLoadMoreView
    private  var _binding:FragmentListBinding ?= null
    private val binding get() = _binding!!
    companion object{
        fun newInstance(cid:Int,initPageNo:Int):ProjectChildFragment{
            val args = Bundle()
            args.putInt("cid",cid)
            args.putInt("initPageNo",initPageNo)
            val fragment = ProjectChildFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(isNew:Boolean,initPageNo: Int):ProjectChildFragment{
            val args = Bundle()
            args.putBoolean("isNew",isNew)
            args.putInt("initPageNo",initPageNo)
            val fragment = ProjectChildFragment()
            fragment.arguments = args
            return fragment
        }
    }
    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerProjectChildComponent
            .builder()
            .appComponent(appComponent)
            .projectChildModule(ProjectChildModule(this))
            .build()
            .inject(this)
    }

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater,container,false)
        //绑定loadSir
        loadSir = LoadSir.getDefault().register(binding.swipeRefreshLayout){
            loadSir.showCallback(LoadingCallBack::class.java)
            pageNo = initPageNo
            if(isNew){
                mPresenter?.getProjectNewData(pageNo)
            }else{
                mPresenter?.getProjectDataByType(pageNo,cid)
            }
        }.apply {
            SettingUtil.setLoadingColor(_mActivity,this)
        }
        return binding.root
    }

    override fun initData(savedInstanceState: Bundle?) {
        cid =  arguments?.getInt("cid") ?: 0
        initPageNo = arguments?.getInt("initPageNo") ?: 1
        pageNo = initPageNo
        isNew = arguments?.getBoolean("isNew") ?:false

        //初始化 swipeRefreshLayout
        binding.swipeRefreshLayout.run {
            setColorSchemeColors(SettingUtil.getColor(_mActivity))
            setOnRefreshListener {
                //刷新
                pageNo = initPageNo
                if(isNew){
                     mPresenter?.getProjectNewData(pageNo)
                }else{
                    mPresenter?.getProjectDataByType(pageNo,cid = cid)
                }
            }
        }

        //初始化 Adapter
        adapter = ArticleAdapter(arrayListOf()).apply {
            if(SettingUtil.getListMode(_mActivity) != 0){
                openLoadAnimation(SettingUtil.getListMode(_mActivity))
            }else{
                closeLoadAnimation()
            }

            //点击爱心收藏执行操作
            setOnCollectViewClickListener(object:ArticleAdapter.OnCollectViewClickListener{
                override fun onClick(helper: BaseViewHolder, v: CollectView, position: Int) {
                    if(v.isChecked){
                        mPresenter?.uncollect(adapter.data[position].id,position)
                    }else{
                        mPresenter?.collect(adapter.data[position].id,position)
                    }
                }

            })

            //点击了整行
            setOnItemClickListener{_,view, position ->
                val intent = Intent(_mActivity, WebViewActivity::class.java)
                val bundle = Bundle().apply {
                    putSerializable("data", adapter.data[position])
                    putString("tag", this@ProjectChildFragment::class.java.simpleName)
                    putInt("position", position)
                    putInt("tab", cid)
                }
                intent.putExtras(bundle)
                startActivity(intent)

            }
        }

        //
        binding.floatbtn.run{
            backgroundTintList = SettingUtil.getOneColorStateList(_mActivity)
            setOnClickListener{
                val layoutManager = binding.swiperecyclerview.layoutManager as LinearLayoutManager
                //如果当前Recyclerview的最后一个视图索引大于等于40，则迅速返回顶部，否则带有滚动动画效果返回顶部
                if(layoutManager.findLastVisibleItemPosition() >= 40){
                    //没有动画迅速返回到顶部
                    binding.swiperecyclerview.scrollToPosition(0)
                }else{
                    //有滚动动画返回到顶部
                    binding.swiperecyclerview.smoothScrollToPosition(0)
                }
            }
        }

        //初始化Recyclerview
       footView = RecyclerViewUtils().initRecyclerView(_mActivity,
           binding.swiperecyclerview
       ) {
           if (isNew) {
               mPresenter?.getProjectNewData(pageNo)
           } else {
               mPresenter?.getProjectDataByType(pageNo, cid)
           }
       }.apply {
            setLoadViewColor(SettingUtil.getOneColorStateList(_mActivity))
       }

        binding.swiperecyclerview.run{
            //监听recyclerview的滑动，滑动到顶部时候，需要将向上返回的按钮隐藏
            addOnScrollListener(object: RecyclerView.OnScrollListener(){
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
        loadSir.showCallback(LoadingCallBack::class.java)  //默认设置界面加载中
        binding.swiperecyclerview.adapter = adapter //设置适配器
        if(isNew){
            mPresenter?.getProjectNewData(pageNo)
        }else{
            mPresenter?.getProjectDataByType(pageNo,cid)
        }
    }

    override fun requestDataSucc(apiPagerResponse: ApiPagerResponse<MutableList<ArticleResponse>>) {
       binding.swipeRefreshLayout.isRefreshing  = false
        if(pageNo == initPageNo && apiPagerResponse.datas.size == 0){
            //如果是第一页，并且没有数据，页面提示空布局
            loadSir.showCallback(EmptyCallback::class.java)
        }else if(pageNo == initPageNo){
            loadSir.showSuccess()
            //如果刷新的话，floatButton就要隐藏了，因为这时候肯定是要在顶部的
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
            binding.swiperecyclerview.postDelayed({
                //解释一下为什么这里要延时0.2秒操作。。。
                //因为上面的adapter.addData(data) 数据刷新了适配器，是需要时间的，还没刷新完，这里就已经执行了没有更多数据
                //所以在界面上会出现一个小bug，刷新最后一页的时候，没有更多数据啦提示先展示出来了，然后才会加载出请求到的数据
                //暂时还没有找到好的方法，就用这个处理一下，如果觉得没什么影响的可以去掉这个延时操作，或者有更好的解决方式可以告诉我一下
                binding.swiperecyclerview.loadMoreFinish(false,false)
                                                           },200)
        }
    }

    override fun requestDataFaild(errorMsg: String) {
        with(binding){
            swipeRefreshLayout.isRefreshing = false
            //如果页码是初始页，说明是刷新，界面切换成错误页
            if(pageNo == initPageNo){
                loadSir.setCallBack(ErrorCallback::class.java){_,view ->
                    //设置错误页面的文字错误提示
                    view.findViewById<TextView>(R.id.error_text).text  = errorMsg
                }
                loadSir.showCallback(ErrorCallback::class.java)
            }else{
                //页码不是0，说明加载更多时出现错误，设置Recyclerview加载错误
               swiperecyclerview.loadMoreError(0,errorMsg)
            }
        }
    }

    /**
     * 收藏回调
     */
    override fun collect(collected: Boolean, position: Int) {
        CollectEvent(collected,adapter.data[position].id).post()
    }

    /**
     * 接收到登录或者退出的EventBus刷新数据
     *
     */
    @SuppressLint("NotifyDataSetChanged")
    @Subscribe
    fun freshLogin(event:LoginFreshEvent){
        //如果是登陆了，当前界面的数据与账户收藏集合id 匹配的值需要设置已经收藏
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
            //退出了，把所有的收藏变成未收藏
            for(item in adapter.data){
                item.collect = false
            }
        }
        adapter.notifyDataSetChanged()
    }

    /**
     * 详情收藏时，接收到EventBus刷新相关数据
     */
    @DelicateCoroutinesApi
    @Subscribe
    fun collectChange(event:CollectEvent){
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
        setUiTheme(_mActivity, listOf(binding.floatbtn,binding.swipeRefreshLayout,loadSir,footView,adapter))
    }

}