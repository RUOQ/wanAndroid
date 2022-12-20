package com.ruoq.wanAndroid.di.component.project

import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.FragmentScope
import com.ruoq.wanAndroid.di.module.main.project.ProjectModule
import com.ruoq.wanAndroid.mvp.ui.activity.main.project.ProjectFragment
import dagger.Component

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/16 17:08
 * @Description : 文件描述
 */
@FragmentScope
@Component(modules = arrayOf(ProjectModule::class),dependencies = arrayOf(AppComponent::class))
interface ProjectComponent {
    fun inject(fragment: ProjectFragment)
}