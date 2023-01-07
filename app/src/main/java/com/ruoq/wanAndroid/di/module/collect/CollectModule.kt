package com.ruoq.wanAndroid.di.module.collect

import com.jess.arms.di.scope.FragmentScope
import com.ruoq.wanAndroid.mvp.contract.collect.CollectContract
import com.ruoq.wanAndroid.mvp.model.collect.CollectModel
import dagger.Module
import dagger.Provides

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2023/1/5 10:54
 * @Description : 文件描述
 */
@Module
class CollectModule (private val view:CollectContract.View){
    @FragmentScope
    @Provides
    fun provideCollectView():CollectContract.View{
        return this.view
    }
    @FragmentScope
    @Provides
    fun providerCollectModel(model: CollectModel):CollectContract.Model{
        return model
    }
}