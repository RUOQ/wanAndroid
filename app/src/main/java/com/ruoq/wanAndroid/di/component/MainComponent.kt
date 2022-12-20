package com.ruoq.wanAndroid.di.component

import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.FragmentScope
import com.ruoq.wanAndroid.di.module.MainModule
import com.ruoq.wanAndroid.mvp.ui.activity.main.MainFragment
import dagger.Component

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/9 18:23
 * @Description : 文件描述
 */
@FragmentScope
@Component(modules = arrayOf(MainModule::class),dependencies = arrayOf(AppComponent::class))
interface MainComponent {
    fun inject(fragment:MainFragment)
}