package com.ruoq.wanAndroid.mvp.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/10/28 20:05
 * @Description : 文件描述
 */
@Parcelize
data class UserInfoResponse(var admin:Boolean,
                            var chapterTops:List<String>,
                            var collectIds:MutableList<String>,
                            var email:String,
                            var icon:String,
                            var id:String,
                            var nickname:String,
                            var password:String,
                            var token:String,
                            var type:Int,
                            var username:String

):Parcelable