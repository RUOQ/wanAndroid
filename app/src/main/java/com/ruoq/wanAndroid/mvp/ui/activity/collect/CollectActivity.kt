package com.ruoq.wanAndroid.mvp.ui.activity.collect

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator

import com.jess.arms.di.component.AppComponent
import com.jess.arms.mvp.IPresenter
import com.ruoq.wanAndroid.R
import com.ruoq.wanAndroid.app.utils.SettingUtil
import com.ruoq.wanAndroid.app.weight.ScaleTransitionPagerTitleView
import com.ruoq.wanAndroid.databinding.ActivityCollectBinding
import com.ruoq.wanAndroid.mvp.ui.adapter.ViewPagerAdapter
import com.ruoq.wanAndroid.mvp.ui.base.BaseActivity
import me.yokeyword.fragmentation.SupportFragment
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2023/1/4 17:41
 * @Description : 文件描述
 */
class CollectActivity: BaseActivity<IPresenter>() {
    var mDateList = arrayListOf<String>("文章","网址")
    var fragments:MutableList<SupportFragment> = mutableListOf()
    internal var pagerAdapter : ViewPagerAdapter?= null
    private var _binding: ActivityCollectBinding?= null
    private val binding get() = _binding!!
    override fun setupActivityComponent(appComponent: AppComponent) {

    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return  0
    }

    override fun initData(savedInstanceState: Bundle?) {
        _binding = ActivityCollectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding){
            toolBar.run{
                setSupportActionBar(this)
                title = ""
                setNavigationOnClickListener {
                    finish()
                }
                setNavigationIcon(R.drawable.ic_back)
            }

            fragments.run{
                add(CollectArticleFragment.newInstance())
                add(CollectUrlFragment.newInstance())
            }
            collectViewpagerLinear.setBackgroundColor(SettingUtil.getColor(this@CollectActivity))
            pagerAdapter = ViewPagerAdapter(supportFragmentManager,fragments)
            viewPager.adapter = pagerAdapter
            val commonNavigator = CommonNavigator(this@CollectActivity)
            commonNavigator.adapter = object:CommonNavigatorAdapter(){
                override fun getCount(): Int {
                   return mDateList.size
                }

                override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                    return ScaleTransitionPagerTitleView(context).apply {
                        text = mDateList[index]
                        textSize = 18f
                        normalColor = Color.WHITE
                        selectedColor = Color.WHITE
                        setOnClickListener {
                            viewPager.setCurrentItem(index,false)
                        }
                    }
                }

                override fun getIndicator(context: Context): IPagerIndicator {
                    return LinePagerIndicator(context).apply {
                        mode = LinePagerIndicator.MODE_EXACTLY
                        lineWidth = UIUtil.dip2px(context,30.0).toFloat()
                        lineHeight  = UIUtil.dip2px(context,3.0).toFloat()
                        roundRadius = UIUtil.dip2px(context,6.0).toFloat()
                        startInterpolator = AccelerateInterpolator()
                        endInterpolator = DecelerateInterpolator(2.0f)
                        setColors(Color.WHITE)
                    }
                }
            }

            magicIndicator.navigator = commonNavigator
            ViewPagerHelper.bind(magicIndicator,viewPager)
        }
    }
}