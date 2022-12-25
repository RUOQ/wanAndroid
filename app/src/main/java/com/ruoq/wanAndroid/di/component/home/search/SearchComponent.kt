package com.ruoq.wanAndroid.di.component.home.search

import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.ActivityScope
import com.ruoq.wanAndroid.di.module.main.home.search.SearchModule
import com.ruoq.wanAndroid.mvp.ui.activity.main.home.search.SearchActivity
import dagger.Component

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/25 13:47
 * @Description : 文件描述
 */
@ActivityScope
@Component(modules = arrayOf(SearchModule::class),
dependencies = arrayOf(AppComponent::class) )
interface SearchComponent {
    fun inject(activity:SearchActivity)
}