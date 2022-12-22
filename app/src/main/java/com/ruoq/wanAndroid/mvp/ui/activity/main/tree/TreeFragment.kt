package com.ruoq.wanAndroid.mvp.ui.activity.main.tree

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.viewpager.widget.ViewPager
import com.jess.arms.di.component.AppComponent
import com.jess.arms.mvp.IPresenter
import com.ruoq.wanAndroid.R
import com.ruoq.wanAndroid.app.event.SettingChangeEvent
import com.ruoq.wanAndroid.app.utils.SettingUtil
import com.ruoq.wanAndroid.app.weight.ScaleTransitionPagerTitleView
import com.ruoq.wanAndroid.databinding.FragmentViewpagerBinding
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
 * @Time : 2022/12/8 17:08
 * @Description : 文件描述
 */
class TreeFragment: BaseFragment<IPresenter>() {
    val mDataList = arrayListOf("广场","体系","导航")
    val fragments :MutableList<SupportFragment> = mutableListOf()
    internal var pagerAdapter :ViewPagerAdapter ?= null
    private var _binding:FragmentViewpagerBinding ?= null
    private val binding get() =_binding!!
    companion object{
        fun newInstance():TreeFragment{
            return TreeFragment()
        }
    }
    override fun setupFragmentComponent(appComponent: AppComponent) {

    }

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewpagerBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        binding.includeViewpagerToolbar.apply {
            setBackgroundColor(SettingUtil.getColor(_mActivity))
            inflateMenu(R.menu.todo_menu)
            setOnMenuItemClickListener {
                when(it.itemId){
//                    R.id.todo_add -> launchActivity(Intent(_mActivity,ShareAr))
                }
                true
            }
        }
        binding.viewpagerLinear.setBackgroundColor(SettingUtil.getColor(_mActivity))
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        fragments.run{
            add(SquareFragment.newInstance())
            add(SystemFragment.newInstance())
            add(NavigationFragment.newInstance())
        }

        pagerAdapter = ViewPagerAdapter(childFragmentManager,fragments)
        binding.viewPager.apply {
            adapter = pagerAdapter
            offscreenPageLimit = fragments.size
            addOnPageChangeListener(object:ViewPager.OnPageChangeListener{
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                    if(position != 0){
                        binding.includeViewpagerToolbar.menu.clear()
                    }else{
                        binding.includeViewpagerToolbar.menu.hasVisibleItems().let {
                            if(!it){
                                binding.includeViewpagerToolbar.inflateMenu(R.menu.todo_menu)
                            }
                        }
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {

                }

            })
        }

        val commonNavigator = CommonNavigator(_mActivity)
        commonNavigator.adapter = object :CommonNavigatorAdapter(){
            override fun getCount(): Int {
               return mDataList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                return ScaleTransitionPagerTitleView(context).apply {
                    text = mDataList[index]
                    textSize = 18f
                    normalColor = Color.WHITE
                    selectedColor = Color.WHITE
                    setOnClickListener {
                        binding.viewPager.setCurrentItem(index,false)
                        if(index != 0){
                            binding.includeViewpagerToolbar.menu.clear()
                        }else{
                            binding.includeViewpagerToolbar.menu.hasVisibleItems().let {
                                if(!it){
                                    binding.includeViewpagerToolbar.inflateMenu(R.menu.todo_menu)
                                }
                            }
                        }
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
                   setColors((Color.WHITE))
               }
            }
        }

        binding.magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(binding.magicIndicator,binding.viewPager)
    }


    /**
     * 接收到event时，重新设置当前页面的控件的主题颜色，和其他的配置
     */
    @Subscribe
    fun settingEvent(event:SettingChangeEvent){

    }


}