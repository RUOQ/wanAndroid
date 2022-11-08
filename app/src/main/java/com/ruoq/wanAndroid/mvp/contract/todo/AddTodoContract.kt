package com.ruoq.wanAndroid.mvp.contract.todo

import com.jess.arms.mvp.IModel
import com.jess.arms.mvp.IView
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import io.reactivex.Observable

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/3 21:24
 * @Description : 文件描述
 */
interface AddTodoContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView {
        fun addTodoSucc()
        fun addTodoFaild(errorMsg: String)
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model : IModel {
        fun addTodo(title: String, content: String, date: String, type: Int, priority: Int): Observable<ApiResponse<Any>>
        fun updateTodo(title: String, content: String, date: String, type: Int, priority: Int,id:Int): Observable<ApiResponse<Any>>
    }

}
