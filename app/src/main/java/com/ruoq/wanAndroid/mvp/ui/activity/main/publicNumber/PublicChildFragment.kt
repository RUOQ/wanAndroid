package com.ruoq.wanAndroid.mvp.ui.activity.main.publicNumber

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
import com.ruoq.wanAndroid.di.component.publicNumber.DaggerPublicChildComponent
import com.ruoq.wanAndroid.di.module.main.publicNumber.PublicChildModule
import com.ruoq.wanAndroid.mvp.contract.main.publicNumber.PublicChildContract
import com.ruoq.wanAndroid.mvp.model.entity.ApiPagerResponse
import com.ruoq.wanAndroid.mvp.model.entity.ArticleResponse
import com.ruoq.wanAndroid.mvp.ui.activity.main.web.WebViewActivity
import com.ruoq.wanAndroid.mvp.ui.adapter.ArticleAdapter
import com.ruoq.wanAndroid.mvp.ui.base.BaseFragment
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import me.hegj.wandroid.mvp.presenter.main.publicNumber.PublicChildPresenter
import org.greenrobot.eventbus.Subscribe

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/8 17:06
 * @Description : 文件描述
 */
class PublicChildFragment : BaseFragment<PublicChildPresenter>(),PublicChildContract.View {

    lateinit var loadSir :LoadService<Any>
    lateinit var adapter :ArticleAdapter
    private var initPageNo = 1 //注意公众号页码从1开始的
    private var pageNo:Int = initPageNo
    private var cid :Int = 0
    lateinit var footView:DefineLoadMoreView
    private var _binding:FragmentListBinding ?= null
    private val binding get()= _binding!!

    companion object {
        fun newInstance(cid: Int): PublicChildFragment {
            val args = Bundle()
            args.putInt("cid", cid)
            return PublicChildFragment().apply {
                arguments = args
            }
        }
    }
    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerPublicChildComponent
            .builder()
            .appComponent(appComponent)
            .publicChildModule(PublicChildModule(this))
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
            mPresenter?.getPublicDataByType(pageNo,cid)
        }.apply {
            SettingUtil.setLoadingColor(_mActivity,this)
        }
        return binding.root
    }

    /**
     * Fragment初始化会调用该方法
     */
    override fun initData(savedInstanceState: Bundle?) {
       cid = arguments?.getInt("cid") ?: 0
        //初始化swipeRefreshLayout
        binding.swipeRefreshLayout.run {
            setColorSchemeColors(SettingUtil.getColor(_mActivity))
            setOnRefreshListener {
                pageNo = initPageNo
                mPresenter?.getPublicDataByType(pageNo,cid)
            }
        }

        //初始化Adapter
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
            setOnItemClickListener{_, view, position ->
                val intent = Intent(_mActivity,WebViewActivity::class.java)
                val bundle = Bundle().apply {
                    putSerializable("data",adapter.data[position])
                    putString("tag",this@PublicChildFragment::class.java.simpleName)
                    putInt("position",position)
                    putInt("tab",cid)
                }
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }

        binding.floatbtn.run{
            backgroundTintList = SettingUtil.getOneColorStateList(_mActivity)
            setOnClickListener {
                val layoutManager = binding.swiperecyclerview.layoutManager as LinearLayoutManager
                if(layoutManager.findLastVisibleItemPosition() >= 40){
                    binding.swiperecyclerview.scrollToPosition(0)
                }else{
                    binding.swiperecyclerview.smoothScrollToPosition(0)
                }
            }
        }

        //初始化Recyclerview
        footView = RecyclerViewUtils().initRecyclerView(_mActivity,
            binding.swiperecyclerview,
            SwipeRecyclerView.LoadMoreListener {
            //加载更多
                mPresenter?.getPublicDataByType(pageNo,cid)
        }).apply {
            setLoadViewColor(SettingUtil.getOneColorStateList(_mActivity))
        }

        binding.swiperecyclerview.run{
            //监听recyclerview滑动到顶部时侯，需要把向上返回到顶部的按钮隐藏
            addOnScrollListener(object:RecyclerView.OnScrollListener(){
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
     * 懒加载，只有该Fragment获得视图才会调用
     */
    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        loadSir.showCallback(LoadingCallBack::class.java)
        binding.swiperecyclerview.adapter = adapter
        mPresenter?.getPublicDataByType(pageNo,cid)
    }

    override fun requestDataSucc(apiPagerResponse: ApiPagerResponse<MutableList<ArticleResponse>>) {
        binding.swipeRefreshLayout.isRefreshing = false
        if(pageNo == initPageNo && apiPagerResponse.datas.size == 0){
            //如果是第一页，并且没有数据，页面提示空布局
            loadSir.showCallback(EmptyCallback::class.java)
        }else if(pageNo == initPageNo){
            loadSir.showSuccess()
            //如果是刷新的话，FloatButton需要隐藏
            binding.floatbtn.visibility = View.INVISIBLE
            adapter.setNewData(apiPagerResponse.datas)
        }else{
            //不是第一页，且有数据
            loadSir.showSuccess()
            adapter.addData(apiPagerResponse.datas)
        }
        pageNo++
        if(apiPagerResponse.pageCount >= pageNo){
            //如果总条数大于或等于当前页数，表示还有更多数据
            binding.swiperecyclerview.loadMoreFinish(false,true)
        }else{
            //没有更多数据
            binding.swiperecyclerview.postDelayed({
                //解释一下为什么这里要延时0.2秒操作。。。
                //因为上面的adapter.addData(data) 数据刷新了适配器，是需要等待时间的，还没刷新完，这里就已经执行了没有更多数据
                //所以在界面上会出现一个小bug，刷新最后一页的时候，没有更多数据啦提示先展示出来了，然后才会加载出请求到的数据
                //暂时还没有找到好的方法，就用这个处理一下，如果觉得没什么影响的可以去掉这个延时操作，或者有更好的解决方式可以告诉我一下
                binding.swiperecyclerview.loadMoreFinish(false, false)
            }, 200)
        }
    }

    override fun requestDataFaild(errorMsg: String) {
        binding.swipeRefreshLayout.isRefreshing = false
        if(pageNo == initPageNo){
            //如果页码是初始页，说明是刷新，界面切换成错误页面
            loadSir.setCallBack(ErrorCallback::class.java){_, view ->
                view.findViewById<TextView>(R.id.error_text).text =errorMsg
            }

            //设置错误
            loadSir.showCallback(ErrorCallback::class.java)
        }else{
            //页码不是1，说明加载更多时出现了错误，设置Recyclerview加载错误
            binding.swiperecyclerview.loadMoreError(0,errorMsg)
        }
    }

    /**
     * 收藏回调
     */
    override fun collect(collected: Boolean, position: Int) {
       CollectEvent(collected,adapter.data[position].id).post()
    }

    /**
     * 接收到登录或退出的EventBus刷新数据
     */
    @SuppressLint("NotifyDataSetChanged")
    @Subscribe
    fun freshLogin(event: LoginFreshEvent) {
        //如果是登录了， 当前界面的数据与账户收藏集合id匹配的值需要设置已经收藏
        if (event.login) {
            event.collectIds.forEach {
                for (item in adapter.data) {
                    if (item.id == it.toInt()) {
                        item.collect = true
                        break
                    }
                }
            }
        } else {
            //退出了，把所有的收藏全部变为未收藏
            for (item in adapter.data) {
                item.collect = false
            }
        }
        adapter.notifyDataSetChanged()
    }

    /**
     * 在详情中收藏时，接收到EventBus
     */
    @DelicateCoroutinesApi
    @Subscribe
    fun collectChange(event: CollectEvent) {
        //使用协程做耗时操作
        GlobalScope.launch{
            async {
                var indexResult = -1
                for (index in adapter.data.indices) {
                    if (adapter.data[index].id == event.id) {
                        adapter.data[index].collect = event.collect
                        indexResult = index
                        break
                    }
                }
                indexResult
            }.run {
                if(await()!=-1){
                    adapter.notifyItemChanged(await())
                }
            }
        }
    }
    /**
     * 接收到event时，重新设置当前界面控件的主题颜色和一些其他配置
     */
    @Subscribe
    fun settingEvent(event: SettingChangeEvent) {
        setUiTheme(_mActivity, listOf(binding.floatbtn,binding.swipeRefreshLayout,loadSir,footView,adapter))
    }
}