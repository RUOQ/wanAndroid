package com.ruoq.wanAndroid.mvp.model.entity

import java.io.Serializable

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/10/31 20:14
 * @Description : 文件描述
 */
open class ApiResponse<T>(
    var data:T,
    var errorCode:Int,
    var errorMsg:String,
) :Serializable{
    fun isSuccess():Boolean{
        return errorCode == 0
    }
}