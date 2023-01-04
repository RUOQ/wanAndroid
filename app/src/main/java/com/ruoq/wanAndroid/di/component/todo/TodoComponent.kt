package com.ruoq.wanAndroid.di.component.todo

import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.ActivityScope
import com.ruoq.wanAndroid.di.module.todo.TodoModule
import com.ruoq.wanAndroid.mvp.ui.activity.todo.TodoActivity
import dagger.Component

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/30 23:30
 * @Description : 文件描述
 */
@ActivityScope
@Component(modules = arrayOf(TodoModule::class),dependencies = arrayOf(AppComponent::class))
interface TodoComponent {
    fun inject(activity:TodoActivity)
}