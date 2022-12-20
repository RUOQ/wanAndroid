package com.ruoq.wanAndroid.mvp.ui.activity.main.project

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.jess.arms.di.component.AppComponent
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.ruoq.wanAndroid.R
import com.ruoq.wanAndroid.app.event.SettingChangeEvent
import com.ruoq.wanAndroid.app.utils.SettingUtil
import com.ruoq.wanAndroid.app.weight.ScaleTransitionPagerTitleView
import com.ruoq.wanAndroid.app.weight.loadCallBack.ErrorCallback
import com.ruoq.wanAndroid.app.weight.loadCallBack.LoadingCallBack
import com.ruoq.wanAndroid.databinding.FragmentViewpagerBinding
import com.ruoq.wanAndroid.di.component.project.DaggerProjectComponent
import com.ruoq.wanAndroid.di.module.main.project.ProjectModule
import com.ruoq.wanAndroid.mvp.contract.main.project.ProjectContract
import com.ruoq.wanAndroid.mvp.model.entity.ClassifyResponse
import com.ruoq.wanAndroid.mvp.presenter.main.project.ProjectPresenter
import com.ruoq.wanAndroid.mvp.ui.adapter.ViewPagerAdapter
import com.ruoq.wanAndroid.mvp.ui.base.BaseFragment
import me.yokeyword.fragmentation.SupportFragment
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import org.greenrobot.eventbus.Subscribe

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/8 17:06
 * @Description : 文件描述
 */
class ProjectFragment:BaseFragment<ProjectPresenter>(),ProjectContract.View {
    var mDataList:MutableList<ClassifyResponse> = mutableListOf()
    var fragments:MutableList<SupportFragment> = mutableListOf()
    internal var pagerAdapter:ViewPagerAdapter ?= null
    private var _binding:FragmentViewpagerBinding ?= null
    private  val binding get() = _binding!!
    lateinit var loadService:LoadService<Any>

    companion object {
        fun newInstance():ProjectFragment{
            return ProjectFragment()
        }

    }
    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerProjectComponent
            .builder()
            .appComponent(appComponent)
            .projectModule(ProjectModule(this))
            .build()
            .inject(this)
    }

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewpagerBinding.inflate(inflater,container,false)
        loadService = LoadSir.getDefault().register(binding.includeLayout.viewPager){
            loadService.showCallback(LoadingCallBack::class.java)
            mPresenter?.getProjectTitles()
        }.apply {
            SettingUtil.setLoadingColor(_mActivity,this)
        }
        return binding.root
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        binding.includeLayout.viewpagerLinear.setBackgroundColor(SettingUtil.getColor(_mActivity))
        //初始化的时候就请求数据，就是说一进入主页的Activity就会请求
        //不然用户进来这个Fragment的时候再请求的话，界面头部啥也没有，这就不好看了
        mPresenter?.getProjectTitles()
    }


    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        pagerAdapter = ViewPagerAdapter(childFragmentManager,fragments)
        binding.includeLayout.viewPager.adapter = pagerAdapter
        val commonNavigator = CommonNavigator(_mActivity)
        commonNavigator.adapter = object :CommonNavigatorAdapter(){
            override fun getCount(): Int {
                return mDataList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                return ScaleTransitionPagerTitleView(context).apply {
                    text = Html.fromHtml(mDataList[index].name)
                    textSize = 17f
                    normalColor = Color.WHITE
                    selectedColor = Color.WHITE
                    setOnClickListener{
                        binding.includeLayout.viewPager.setCurrentItem(index,false)
                    }
                }
            }

            override fun getIndicator(context: Context?): IPagerIndicator {
                return LinePagerIndicator(context).apply {
                    mode = LinePagerIndicator.MODE_EXACTLY
                    //线条的高度
                    lineHeight = UIUtil.dip2px(context,3.0).toFloat()
                    lineWidth = UIUtil.dip2px(context,30.0).toFloat()
                    //线条的圆角
                    roundRadius = UIUtil.dip2px(context,6.0).toFloat()
                    startInterpolator = AccelerateInterpolator()
                    endInterpolator = DecelerateInterpolator(2.0f)
                    //线条的颜色
                    setColors(Color.WHITE)
                }
            }

        }

        binding.includeLayout.magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(binding.includeLayout.magicIndicator,binding.includeLayout.viewPager)
    }

    override fun requestTitleSuccess(titles: MutableList<ClassifyResponse>) {
        //请求到项目分类头部标题集合
        if(titles.size == 0){
            loadService.showCallback(ErrorCallback::class.java)
        }else{
            loadService.showSuccess()
            this.mDataList = titles
            if(fragments.size == 0){
                //防止重复添加，出现Cannot change tag of fragment xxx bug
                for(i in titles.indices){
                    //分类页码从1开始
                    fragments.add(ProjectChildFragment.newInstance(titles[i].id,1))
                }
                //在第一个添加最新的Fragment
                this.mDataList.add(0,
                    ClassifyResponse(
                        arrayListOf(),
                        0,
                        0,
                        "最新项目",
                        0,
                        0,
                        false,0))
                fragments.add(0,ProjectChildFragment.newInstance(true,0))//最新项目页码从0开始
            }
            //如果viewpager 和magicIndicator 不为空的话，刷新他们，为空的话说明，用户还没有进入这个Fragment
            pagerAdapter?.notifyDataSetChanged()
            binding.includeLayout.magicIndicator.navigator?.notifyDataSetChanged()
            binding.includeLayout.viewPager.offscreenPageLimit = fragments.size
        }

        /**
         * 接收到event的时候，重新设置当前界面的控件的主题颜色和一些其他配置
         *
         */
        @Subscribe
        fun settingEvent(event:SettingChangeEvent){
//            setUiTheme(_mActivity, listOf(binding.includeLayout.viewpagerLinear,loadService))
        }
    }
}