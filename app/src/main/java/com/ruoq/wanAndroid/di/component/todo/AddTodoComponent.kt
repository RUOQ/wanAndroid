package com.ruoq.wanAndroid.di.component.todo

import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.ActivityScope
import com.ruoq.wanAndroid.di.module.todo.AddTodoModule
import com.ruoq.wanAndroid.mvp.ui.activity.todo.AddTodoActivity
import dagger.Component

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/30 23:32
 * @Description : 文件描述
 */
@ActivityScope
@Component(modules = arrayOf(AddTodoModule::class), dependencies = arrayOf(AppComponent::class))
interface AddTodoComponent {
    fun inject(activity: AddTodoActivity)
}
