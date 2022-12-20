package com.ruoq.wanAndroid.di.module.main.me

import com.jess.arms.di.scope.FragmentScope
import com.ruoq.wanAndroid.mvp.contract.main.me.MeContract
import com.ruoq.wanAndroid.mvp.model.main.me.MeModel
import dagger.Module
import dagger.Provides

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/16 16:59
 * @Description : 文件描述
 */
@Module
//构建MeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class MeModule(private val view: MeContract.View) {
    @FragmentScope
    @Provides
    fun provideMeView(): MeContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideMeModel(model: MeModel): MeContract.Model {
        return model
    }
}
