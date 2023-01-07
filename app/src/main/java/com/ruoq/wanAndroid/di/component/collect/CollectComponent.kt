package com.ruoq.wanAndroid.di.component.collect

import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.FragmentScope
import com.ruoq.wanAndroid.di.module.collect.CollectModule
import com.ruoq.wanAndroid.mvp.ui.activity.collect.CollectArticleFragment
import dagger.Component

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2023/1/5 10:50
 * @Description : 文件描述
 */
@FragmentScope
@Component(modules = arrayOf(CollectModule::class),dependencies = arrayOf(AppComponent::class))

interface CollectComponent {
    fun inject(fragment:CollectArticleFragment)
}