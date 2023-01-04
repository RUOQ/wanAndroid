package com.ruoq.wanAndroid.di.module.todo

import com.jess.arms.di.scope.ActivityScope
import com.ruoq.wanAndroid.mvp.contract.todo.AddTodoContract
import com.ruoq.wanAndroid.mvp.model.main.todo.AddTodoModel
import dagger.Module
import dagger.Provides

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/30 23:33
 * @Description : 文件描述
 */
@Module
//构建AddTodoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class AddTodoModule(private val view: AddTodoContract.View) {
    @ActivityScope
    @Provides
    fun provideAddTodoView(): AddTodoContract.View {
        return this.view
    }

    @ActivityScope
    @Provides
    fun provideAddTodoModel(model: AddTodoModel): AddTodoContract.Model {
        return model
    }
}