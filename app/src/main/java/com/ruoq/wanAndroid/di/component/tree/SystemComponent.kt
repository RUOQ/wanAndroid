package com.ruoq.wanAndroid.di.component.tree

import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.FragmentScope
import com.ruoq.wanAndroid.di.module.main.tree.SystemModule
import com.ruoq.wanAndroid.mvp.ui.activity.main.tree.SystemFragment
import dagger.Component

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/20 21:53
 * @Description : 文件描述
 */

@FragmentScope
@Component(modules = arrayOf(SystemModule::class),
dependencies = arrayOf(AppComponent::class))
interface SystemComponent {
    fun inject(fragment: SystemFragment)
}