package com.ruoq.wanAndroid.mvp.model.start

import com.jess.arms.di.scope.ActivityScope
import com.ruoq.wanAndroid.mvp.contract.start.LoginContract
import com.ruoq.wanAndroid.mvp.model.main.start.LoginModel
import dagger.Module
import dagger.Provides

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/10 1:10
 * @Description : 文件描述
 */
@Module
//构建LoginModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class LoginModule(private val view: LoginContract.View) {
    @ActivityScope
    @Provides
    fun provideLoginView(): LoginContract.View {
        return this.view
    }

    @ActivityScope
    @Provides
    fun provideLoginModel(model: LoginModel): LoginContract.Model {
        return model
    }
}
