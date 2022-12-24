package com.ruoq.wanAndroid.di.component.publicNumber

import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.FragmentScope
import com.ruoq.wanAndroid.di.module.main.publicNumber.PublicChildModule
import com.ruoq.wanAndroid.mvp.ui.activity.main.publicNumber.PublicChildFragment
import dagger.Component

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/24 22:02
 * @Description : 文件描述
 */
@FragmentScope
@Component(modules = arrayOf(PublicChildModule::class),
dependencies = arrayOf(AppComponent::class))
interface PublicChildComponent {
    fun inject(fragment:PublicChildFragment)
}