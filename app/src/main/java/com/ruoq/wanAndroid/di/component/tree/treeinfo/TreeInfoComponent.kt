package com.ruoq.wanAndroid.di.component.tree.treeinfo

import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.FragmentScope
import com.ruoq.wanAndroid.di.module.main.tree.treeInfo.TreeInfoModule
import com.ruoq.wanAndroid.mvp.ui.activity.main.tree.treeInfo.TreeInfoFragment
import dagger.Component

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/24 16:13
 * @Description : 文件描述
 */
@FragmentScope
@Component(modules = arrayOf(TreeInfoModule::class),
dependencies = arrayOf(AppComponent::class))
interface TreeInfoComponent {
    fun inject(fragment:TreeInfoFragment)
}