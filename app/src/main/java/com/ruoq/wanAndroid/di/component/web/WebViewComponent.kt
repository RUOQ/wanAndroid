package com.ruoq.wanAndroid.di.component.web

import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.ActivityScope
import com.ruoq.wanAndroid.di.module.web.WebViewModule
import com.ruoq.wanAndroid.mvp.ui.activity.main.web.WebViewActivity
import dagger.Component

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/22 22:28
 * @Description : 文件描述
 */
@ActivityScope
@Component(modules = arrayOf(WebViewModule::class),dependencies = arrayOf(AppComponent::class))

interface WebViewComponent {
    fun inject(activity:WebViewActivity)
}