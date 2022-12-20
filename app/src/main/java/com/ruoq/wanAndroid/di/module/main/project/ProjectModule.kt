package com.ruoq.wanAndroid.di.module.main.project

import com.jess.arms.di.scope.FragmentScope
import com.ruoq.wanAndroid.mvp.contract.main.project.ProjectContract
import com.ruoq.wanAndroid.mvp.model.main.project.ProjectModel
import dagger.Module
import dagger.Provides

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/16 16:45
 * @Description : 文件描述
 */@Module
//构建ProjectModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class ProjectModule(private val view: ProjectContract.View) {
    @FragmentScope
    @Provides
    fun provideProjectView(): ProjectContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideProjectModel(model: ProjectModel): ProjectContract.Model {
        return model
    }
}