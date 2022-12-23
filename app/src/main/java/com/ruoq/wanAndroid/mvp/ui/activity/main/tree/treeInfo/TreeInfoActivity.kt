package com.ruoq.wanAndroid.mvp.ui.activity.main.tree.treeInfo

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.jess.arms.di.component.AppComponent
import com.jess.arms.mvp.IPresenter
import com.ruoq.wanAndroid.R
import com.ruoq.wanAndroid.app.utils.SettingUtil
import com.ruoq.wanAndroid.app.weight.ScaleTransitionPagerTitleView
import com.ruoq.wanAndroid.databinding.ActivityTreeinfoBinding
import com.ruoq.wanAndroid.mvp.model.entity.SystemResponse
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
 * @Time : 2022/12/8 17:08
 * @Description : 主页体系--根据类型查询体系结果的Activity
 */
class TreeInfoActivity: BaseActivity<IPresenter>() {
    var positon  = 0 //从上级体系中，点击tag的索引
    lateinit var systemResponse:SystemResponse //从上级体系中得到的data
    var fragments:MutableList<SupportFragment> = mutableListOf()
    internal var pagerAdapter: ViewPagerAdapter ?= null
    private var _binding:ActivityTreeinfoBinding ?= null
    private val binding get() = _binding!!
    override fun setupActivityComponent(appComponent: AppComponent) {

    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return 0
    }

    override fun initData(savedInstanceState: Bundle?) {
        _binding = ActivityTreeinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.run{
            systemResponse = getSerializableExtra("data") as SystemResponse
            positon = getIntExtra("position", 0)
        }

        binding.toolBar.run{
            setSupportActionBar(this)
            title = systemResponse.name
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener {
                finish()
            }
        }

        //根据集合循环添加Fragment
        for(i in systemResponse.children.indices){
            fragments.add(TreeInfoFragment.newInstance(systemResponse.children[i].id))
        }
        binding.viewpagerLinear.setBackgroundColor(SettingUtil.getColor(this))
        pagerAdapter = ViewPagerAdapter(supportFragmentManager,fragments)
        val commonNavigator = CommonNavigator(this)
        commonNavigator.adapter = object :CommonNavigatorAdapter(){
            override fun getCount(): Int {
                return systemResponse.children.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                return ScaleTransitionPagerTitleView(context).apply {
                    text = Html.fromHtml(systemResponse.children[index].name)
                    textSize = 17f
                    normalColor = Color.WHITE
                    selectedColor = Color.WHITE
                    setOnClickListener {
                        binding.viewPager.setCurrentItem(index,false)
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
        binding.viewPager.apply {
            adapter = pagerAdapter
            offscreenPageLimit = fragments.size
            setCurrentItem(positon,false)
        }
    }

}