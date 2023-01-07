package com.ruoq.wanAndroid.di.component.collect

import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.FragmentScope
import com.ruoq.wanAndroid.di.module.collect.CollectUrlModule
import com.ruoq.wanAndroid.mvp.ui.activity.collect.CollectUrlFragment
import dagger.Component

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2023/1/5 10:50
 * @Description : 文件描述
 */
@FragmentScope
@Component(modules = arrayOf(CollectUrlModule::class), dependencies = arrayOf(AppComponent::class))
interface CollectUrlComponent {
    fun inject(fragment: CollectUrlFragment)
}
