package com.ruoq.wanAndroid.di.module.share

import com.jess.arms.di.scope.ActivityScope
import com.ruoq.wanAndroid.mvp.contract.share.ShareByIdContract
import com.ruoq.wanAndroid.mvp.model.main.share.ShareByIdModel
import dagger.Module
import dagger.Provides

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/10 0:36
 * @Description : 文件描述
 */
@Module
//构建ShareByIdModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class ShareByIdModule(private val view: ShareByIdContract.View) {
    @ActivityScope
    @Provides
    fun provideShareByIdView(): ShareByIdContract.View {
        return this.view
    }

    @ActivityScope
    @Provides
    fun provideShareByIdModel(model: ShareByIdModel): ShareByIdContract.Model {
        return model
    }
}