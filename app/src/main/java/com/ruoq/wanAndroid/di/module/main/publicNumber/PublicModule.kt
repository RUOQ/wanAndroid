package com.ruoq.wanAndroid.di.module.main.publicNumber

import com.jess.arms.di.scope.FragmentScope
import com.ruoq.wanAndroid.mvp.contract.main.publicNumber.PublicContract
import com.ruoq.wanAndroid.mvp.model.main.publicNumber.PublicModel
import dagger.Module
import dagger.Provides

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/16 16:47
 * @Description : 文件描述
 */
@Module
//构建PublicModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class PublicModule(private val view: PublicContract.View) {
    @FragmentScope
    @Provides
    fun providePublicView(): PublicContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun providePublicModel(model: PublicModel): PublicContract.Model {
        return model
    }
}
