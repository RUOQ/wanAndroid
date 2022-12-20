package com.ruoq.wanAndroid.di.module.main.tree

import com.jess.arms.di.scope.FragmentScope
import com.ruoq.wanAndroid.mvp.contract.main.tree.SystemContract
import com.ruoq.wanAndroid.mvp.model.main.tree.SystemModel
import dagger.Module
import dagger.Provides

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/16 16:51
 * @Description : 文件描述
 */
@Module
//构建SystemModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class SystemModule(private val view: SystemContract.View) {
    @FragmentScope
    @Provides
    fun provideSystemView(): SystemContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideSystemModel(model: SystemModel): SystemContract.Model {
        return model
    }
}