package com.ruoq.wanAndroid.mvp.contract.start

import com.jess.arms.mvp.IModel
import com.jess.arms.mvp.IView
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import com.ruoq.wanAndroid.mvp.model.entity.UserInfoResponse
import io.reactivex.Observable

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/3 15:51
 * @Description : 文件描述
 */
interface LoginContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView {
        fun onSuccess(userinfo: UserInfoResponse)
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model : IModel {
        fun login(username:String,password:String): Observable<ApiResponse<UserInfoResponse>>
        fun register(username:String,password:String,password1:String):Observable<ApiResponse<Any>>
    }

}
