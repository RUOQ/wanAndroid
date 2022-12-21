package com.ruoq.wanAndroid.mvp.ui.activity.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jess.arms.di.component.AppComponent
import com.ruoq.wanAndroid.R
import com.ruoq.wanAndroid.app.event.SettingChangeEvent
import com.ruoq.wanAndroid.app.utils.SettingUtil
import com.ruoq.wanAndroid.databinding.FragmentMainBinding
import com.ruoq.wanAndroid.di.component.DaggerMainComponent
import com.ruoq.wanAndroid.di.module.MainModule
import com.ruoq.wanAndroid.mvp.contract.MainContract
import com.ruoq.wanAndroid.mvp.presenter.MainPresenter
import com.ruoq.wanAndroid.mvp.ui.activity.main.home.HomeFragment
import com.ruoq.wanAndroid.mvp.ui.activity.main.me.MeFragment
import com.ruoq.wanAndroid.mvp.ui.activity.main.project.ProjectFragment
import com.ruoq.wanAndroid.mvp.ui.activity.main.publicNumber.PublicFragment
import com.ruoq.wanAndroid.mvp.ui.activity.main.tree.TreeFragment
import com.ruoq.wanAndroid.mvp.ui.base.BaseFragment
import me.yokeyword.fragmentation.SupportFragment
import org.greenrobot.eventbus.Subscribe

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/8 17:09
 * @Description : 文件描述
 */
class MainFragment: BaseFragment<MainPresenter>(),MainContract.View {
    private val first = 0
    private val two = 1
    private val three = 2
    private val four = 3
    private val five = 4
    private val mFragments = arrayOfNulls<SupportFragment>(5)
    private var _binding :FragmentMainBinding ?= null
    private val binding:FragmentMainBinding get() = _binding!!
    companion object{
        fun newInstance():MainFragment{
            return MainFragment()
        }
    }

    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerMainComponent.builder()
            .appComponent(appComponent)
            .mainModule(MainModule(this))
            .build()
            .inject(this)
    }

    override fun initView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun initData(savedInstanceState: Bundle?) {
        val homeFragment = findChildFragment(HomeFragment::class.java)
        if(homeFragment == null){
            mFragments[first] = HomeFragment.newInstance() //主页
            mFragments[two] = ProjectFragment.newInstance()
            mFragments[three] = TreeFragment.newInstance()
//            mFragments[four] = PublicFragment.newInstance()
//            mFragments[five] = MeFragment.newInstance()
            loadMultipleRootFragment(
                R.id.main_frame, first, mFragments[first]
                , mFragments[two], mFragments[three])
        }else{
            mFragments[first] = homeFragment
            mFragments[two] = findChildFragment(ProjectFragment::class.java)
            mFragments[three] = findChildFragment(TreeFragment::class.java)
//            mFragments[four] = findChildFragment(PublicFragment::class.java)
//            mFragments[five] = findChildFragment(MeFragment::class.java)
        }

        binding.mainBnve.run {
            enableAnimation(false)
            enableShiftingMode(false)
            enableItemShiftingMode(false)
            itemIconTintList = SettingUtil.getColorStateList(_mActivity)
            itemTextColor = SettingUtil.getColorStateList(_mActivity)
            setIconSize(20F, 20F)
            setTextSize(12F)
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_main -> showHideFragment(mFragments[first])
                    R.id.menu_project -> showHideFragment(mFragments[two])
                    R.id.menu_system -> showHideFragment(mFragments[three])
//                    R.id.menu_public -> showHideFragment(mFragments[four])
//                    R.id.menu_me -> showHideFragment(mFragments[five])
                }
                true
            }
        }
    }

    @Subscribe
    fun settingEvent(event:SettingChangeEvent){
//        setU(_mActivity, listOf(main_bnve)
    }
}