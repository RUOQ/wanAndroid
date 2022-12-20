package com.ruoq.wanAndroid.di.module.main.tree

import com.jess.arms.di.scope.FragmentScope
import com.ruoq.wanAndroid.mvp.contract.main.tree.SquareContract
import com.ruoq.wanAndroid.mvp.model.main.tree.SquareModel
import dagger.Module
import dagger.Provides

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/16 16:50
 * @Description : 文件描述
 */
@Module
//构建SquareModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class SquareModule(private val view: SquareContract.View) {
    @FragmentScope
    @Provides
    fun provideSquareView(): SquareContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideSquareModel(model: SquareModel): SquareContract.Model {
        return model
    }
}