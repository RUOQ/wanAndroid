package com.ruoq.wanAndroid.di.component.share

import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.ActivityScope
import com.ruoq.wanAndroid.di.module.share.ShareByIdModule
import com.ruoq.wanAndroid.mvp.ui.activity.share.ShareByIdActivity
import dagger.Component

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/10 23:15
 * @Description : 文件描述
 */
@ActivityScope
@Component(modules = [ShareByIdModule::class], dependencies = [AppComponent::class])
interface ShareByIdComponent {
    fun inject(activity: ShareByIdActivity)
}
