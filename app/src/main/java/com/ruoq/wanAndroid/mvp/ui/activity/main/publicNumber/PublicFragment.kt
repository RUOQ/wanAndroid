package com.ruoq.wanAndroid.mvp.ui.activity.main.publicNumber

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.jess.arms.di.component.AppComponent
import com.jess.arms.mvp.IPresenter
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.ruoq.wanAndroid.app.event.SettingChangeEvent
import com.ruoq.wanAndroid.app.utils.SettingUtil
import com.ruoq.wanAndroid.app.utils.setUiTheme
import com.ruoq.wanAndroid.app.weight.ScaleTransitionPagerTitleView
import com.ruoq.wanAndroid.app.weight.loadCallBack.ErrorCallback
import com.ruoq.wanAndroid.app.weight.loadCallBack.LoadingCallBack
import com.ruoq.wanAndroid.databinding.FragmentViewpagerBinding
import com.ruoq.wanAndroid.di.component.publicNumber.DaggerPublicComponent
import com.ruoq.wanAndroid.di.module.main.publicNumber.PublicModule
import com.ruoq.wanAndroid.mvp.contract.main.project.ProjectContract
import com.ruoq.wanAndroid.mvp.contract.main.publicNumber.PublicContract
import com.ruoq.wanAndroid.mvp.model.entity.ClassifyResponse
import com.ruoq.wanAndroid.mvp.presenter.main.project.ProjectPresenter
import com.ruoq.wanAndroid.mvp.presenter.main.publicNumber.PublicPresenter
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
 * @Time : 2022/12/8 17:07
 * @Description : 文件描述
 */
class PublicFragment : BaseFragment<PublicPresenter>(), PublicContract.View {

    var mDataList: MutableList<ClassifyResponse> = mutableListOf()
    var fragments: MutableList<SupportFragment> = mutableListOf()
    internal var pagerAdapter: ViewPagerAdapter? = null
    lateinit var loadSir: LoadService<Any>
    private var _binding:FragmentViewpagerBinding ?= null
    private val binding get() = _binding!!

    companion object {
        @SuppressLint("LogNotTimber")
        fun newInstance(): PublicFragment {
            return PublicFragment()
        }
    }

    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerPublicComponent
            .builder()
            .appComponent(appComponent)
            .publicModule(PublicModule(this))
            .build()
            .inject(this)
    }

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewpagerBinding.inflate(inflater,container,false)
        //绑定loadSir
        loadSir = LoadSir.getDefault().register(binding.viewPager){
            loadSir.showCallback(LoadingCallBack::class.java)
            mPresenter?.getProjectTitles()
        }.apply {
            SettingUtil.setLoadingColor(_mActivity,this)
        }
        return binding.root
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        binding.viewpagerLinear.setBackgroundColor(SettingUtil.getColor(_mActivity))

        //初始化的时候请求数据，就是说一进入主页Activity这个就会请求，不然，用户
        //进来这个Fragment的时候再请求，界面头部什么都没有，这就不好看了
        mPresenter?.getProjectTitles()

    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        pagerAdapter = ViewPagerAdapter(childFragmentManager,fragments)
        binding.viewPager.adapter = pagerAdapter
        val commonNavigator = CommonNavigator(_mActivity)
        commonNavigator.adapter = object:CommonNavigatorAdapter(){
            override fun getCount(): Int {
                return mDataList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                return ScaleTransitionPagerTitleView(context).apply {
                    text = Html.fromHtml(mDataList[index].name)
                    textSize = 17f
                    normalColor = Color.WHITE
                    selectedColor = Color.WHITE
                    setOnClickListener {
                        binding.viewPager.setCurrentItem(index,true)
                    }
                }
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                return LinePagerIndicator(context).apply {
                    mode = LinePagerIndicator.MODE_EXACTLY
                    lineHeight = UIUtil.dip2px(context,3.0).toFloat()
                    lineWidth = UIUtil.dip2px(context,30.0).toFloat()
                    roundRadius = UIUtil.dip2px(context,6.0).toFloat()
                    startInterpolator = AccelerateInterpolator()
                    endInterpolator = DecelerateInterpolator(2.0f)
                    setColors(Color.WHITE)
                }
            }

        }
        binding.magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(binding.magicIndicator,binding.viewPager)
    }
    override fun requestTitleSuccess(titles: MutableList<ClassifyResponse>) {
        //请求到 项目分类头部标题集合
        if (titles.size == 0) {
            //没有数据，说明肯定出错了 这种情况只有第一次会出现，因为第一次请求成功后，会做本地保存操作，下次就算请求失败了，也会从本地取出来
            loadSir.showCallback(ErrorCallback::class.java)
        } else {
            loadSir.showSuccess()
            this.mDataList = titles
            if (fragments.size == 0) {
                //根据头部集合循环添加对应的Fragment
                for (i in titles.indices) {
                    fragments.add(PublicChildFragment.newInstance(titles[i].id))
                }
            }
            //如果viewpager和 magicindicator 不为空的话，刷新他们 为空的话说明 用户还没有进来 这个Fragment
            pagerAdapter?.notifyDataSetChanged()
            binding.magicIndicator.navigator?.notifyDataSetChanged()
            binding.viewPager.offscreenPageLimit = fragments.size
        }
    }


    /**
     * 接收到Event时，重新设置当前界面控件的主题颜色和其他的一些配置
     *
     */
    @Subscribe
    fun settingEvent(event:SettingChangeEvent){
        setUiTheme(_mActivity, listOf(binding.viewpagerLinear,loadSir))
    }
}