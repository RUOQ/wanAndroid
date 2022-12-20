package com.ruoq.wanAndroid.di.component.project

import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.FragmentScope
import com.ruoq.wanAndroid.di.module.main.project.ProjectChildModule
import com.ruoq.wanAndroid.mvp.ui.activity.main.project.ProjectChildFragment
import dagger.Component

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/17 23:31
 * @Description : 文件描述
 */
@FragmentScope
@Component(modules = arrayOf(ProjectChildModule::class),
    dependencies = arrayOf(AppComponent::class
))
interface ProjectChildComponent {
    fun inject(fragment:ProjectChildFragment)
}