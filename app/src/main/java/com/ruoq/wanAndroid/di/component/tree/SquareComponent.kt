package com.ruoq.wanAndroid.di.component.tree

import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.FragmentScope
import com.ruoq.wanAndroid.di.module.main.tree.SquareModule
import com.ruoq.wanAndroid.mvp.ui.activity.main.tree.SquareFragment
import dagger.Component

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/20 20:14
 * @Description : 文件描述
 */
@FragmentScope
@Component(modules = arrayOf(SquareModule::class),dependencies =
arrayOf(AppComponent::class))
interface SquareComponent {
    fun inject(fragment:SquareFragment)
}