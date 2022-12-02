package com.ruoq.wanAndroid.di.component.start

import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.ActivityScope
import com.ruoq.wanAndroid.mvp.model.start.LoginModule
import com.ruoq.wanAndroid.mvp.ui.activity.start.LoginActivity
import dagger.Component

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/10 23:13
 * @Description : 文件描述
 */
@ActivityScope
@Component(modules = [LoginModule::class], dependencies = [AppComponent::class])
interface LoginComponent {
    fun inject(activity: LoginActivity)
//    fun inject1(activity: RegisterActivity)
}