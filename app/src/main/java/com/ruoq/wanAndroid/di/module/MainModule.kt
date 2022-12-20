package com.ruoq.wanAndroid.di.module

import com.jess.arms.di.scope.FragmentScope
import com.ruoq.wanAndroid.mvp.contract.MainContract
import com.ruoq.wanAndroid.mvp.model.MainModel
import dagger.Module
import dagger.Provides


/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/9 18:33
 * @Description : 文件描述
 */
@Module
//构建MainModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class MainModule(private val view: MainContract.View) {
    @FragmentScope
    @Provides
    fun provideMainView(): MainContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideMainModel(model: MainModel): MainContract.Model {
        return model
    }
}