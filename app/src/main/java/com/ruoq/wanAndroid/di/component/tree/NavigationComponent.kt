package com.ruoq.wanAndroid.di.component.tree

import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.FragmentScope
import com.ruoq.wanAndroid.di.module.main.tree.NavigationModule
import com.ruoq.wanAndroid.mvp.ui.activity.main.tree.NavigationFragment
import dagger.Component

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/20 23:52
 * @Description : 文件描述
 */
@FragmentScope
@Component(modules = arrayOf(NavigationModule::class),
dependencies = arrayOf(AppComponent::class))
interface NavigationComponent {
    fun inject(fragment: NavigationFragment)
}