package com.ruoq.wanAndroid.di.module.main.tree

import com.jess.arms.di.scope.FragmentScope
import com.ruoq.wanAndroid.mvp.contract.main.tree.NavigationContract
import com.ruoq.wanAndroid.mvp.model.main.tree.NavigationModel
import dagger.Module
import dagger.Provides

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/16 16:50
 * @Description : 文件描述
 */
@Module
class NavigationModule(private val view:NavigationContract.View) {
    @FragmentScope
    @Provides
    fun provideNavigationView():NavigationContract.View{
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideNavigationModel(model: NavigationModel):NavigationContract.Model{
        return model
    }
}