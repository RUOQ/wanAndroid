package com.ruoq.wanAndroid.di.module.main.project

import com.jess.arms.di.scope.FragmentScope
import com.ruoq.wanAndroid.mvp.contract.main.project.ProjectChildContract
import com.ruoq.wanAndroid.mvp.model.main.project.ProjectChildModel
import dagger.Module
import dagger.Provides

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/16 16:46
 * @Description : 文件描述
 */
@Module
//构建ProjectChildModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class ProjectChildModule(private val view: ProjectChildContract.View) {
    @FragmentScope
    @Provides
    fun provideProjectChildView(): ProjectChildContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideProjectChildModel(model: ProjectChildModel): ProjectChildContract.Model {
        return model
    }
}