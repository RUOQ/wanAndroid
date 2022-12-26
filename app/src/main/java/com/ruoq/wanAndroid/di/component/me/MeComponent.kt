package com.ruoq.wanAndroid.di.component.me

import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.FragmentScope
import com.ruoq.wanAndroid.di.module.main.me.MeModule
import com.ruoq.wanAndroid.mvp.ui.activity.main.me.MeFragment
import dagger.Component

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/26 18:37
 * @Description : 文件描述
 */
@FragmentScope
@Component(modules= arrayOf(MeModule::class), dependencies = arrayOf(AppComponent::class))
interface MeComponent {
    fun inject(fragment: MeFragment)
}