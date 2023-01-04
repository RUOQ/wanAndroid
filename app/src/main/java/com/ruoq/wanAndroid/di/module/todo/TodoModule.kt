package com.ruoq.wanAndroid.di.module.todo

import com.jess.arms.di.scope.ActivityScope
import com.ruoq.wanAndroid.mvp.contract.todo.TodoContract
import com.ruoq.wanAndroid.mvp.model.main.todo.TodoModel
import dagger.Module
import dagger.Provides

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/30 23:33
 * @Description : 文件描述
 */
@Module
//构建TodoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class TodoModule(private val view: TodoContract.View) {
    @ActivityScope
    @Provides
    fun provideTodoView(): TodoContract.View {
        return this.view
    }

    @ActivityScope
    @Provides
    fun provideTodoModel(model: TodoModel): TodoContract.Model {
        return model
    }
}
