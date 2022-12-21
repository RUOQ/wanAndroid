package com.ruoq.wanAndroid.di.component.home

import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.FragmentScope
import com.ruoq.wanAndroid.di.module.main.home.HomeModule
import com.ruoq.wanAndroid.mvp.ui.activity.main.home.HomeFragment
import dagger.Component

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/22 1:01
 * @Description : 文件描述
 */
@FragmentScope
@Component(modules = arrayOf(HomeModule::class),
dependencies = arrayOf(AppComponent::class))
interface HomeComponent {
    fun inject(fragment:HomeFragment)
}