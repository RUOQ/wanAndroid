package com.ruoq.wanAndroid.mvp.model.entity

/**
 * 收藏的网址类
 * @Author:        RUOQ
 * @CreateDate:     2019/8/31 10:36
 */
import java.io.Serializable
data class CollectUrlResponse(var icon: String,
                              var id: Int,
                              var link: String,
                              var name: String,
                              var order: Int,
                              var userId: Int,
                              var visible: Int):Serializable