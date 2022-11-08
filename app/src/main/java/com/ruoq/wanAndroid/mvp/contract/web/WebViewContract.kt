package com.ruoq.wanAndroid.mvp.contract.web

import com.jess.arms.mvp.IModel
import com.jess.arms.mvp.IView
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import com.ruoq.wanAndroid.mvp.model.entity.CollectUrlResponse
import io.reactivex.Observable

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/3 21:26
 * @Description : 文件描述
 */
interface WebViewContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView {
        fun  collect(collected:Boolean)
        fun  collectUrlSucc(collected:Boolean,data: CollectUrlResponse)
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model : IModel {
        fun collect(id:Int): Observable<ApiResponse<Any>>
        fun collectUrl(name:String,link:String): Observable<ApiResponse<CollectUrlResponse>>
        fun unCollect(id:Int): Observable<ApiResponse<Any>>
        fun unCollectList(id:Int,originId:Int): Observable<ApiResponse<Any>>
        fun unCollectUrl(id:Int): Observable<ApiResponse<Any>>
    }

}
