package com.ruoq.wanAndroid.di.module.collect

import com.jess.arms.di.scope.FragmentScope
import com.ruoq.wanAndroid.mvp.contract.collect.CollectUrlContract
import com.ruoq.wanAndroid.mvp.model.collect.CollectUrlModel
import dagger.Module
import dagger.Provides

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2023/1/5 10:54
 * @Description : 文件描述
 */
@Module
//构建CollectModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class CollectUrlModule(private val view: CollectUrlContract.View) {
    @FragmentScope
    @Provides
    fun provideCollectView(): CollectUrlContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideCollectModel(model: CollectUrlModel): CollectUrlContract.Model {
        return model
    }
}