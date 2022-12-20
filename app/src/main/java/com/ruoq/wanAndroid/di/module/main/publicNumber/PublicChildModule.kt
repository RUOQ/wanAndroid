package com.ruoq.wanAndroid.di.module.main.publicNumber

import com.jess.arms.di.scope.FragmentScope
import com.ruoq.wanAndroid.mvp.contract.main.publicNumber.PublicChildContract
import com.ruoq.wanAndroid.mvp.model.main.publicNumber.PublicChildModel
import dagger.Module
import dagger.Provides

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/16 16:47
 * @Description : 文件描述
 */
@Module
//构建PublicChildModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class PublicChildModule(private val view: PublicChildContract.View) {
    @FragmentScope
    @Provides
    fun providePublicChildView(): PublicChildContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun providePublicChildModel(model: PublicChildModel): PublicChildContract.Model {
        return model
    }
}