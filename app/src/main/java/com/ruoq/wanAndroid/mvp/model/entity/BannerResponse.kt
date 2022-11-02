package com.ruoq.wanAndroid.mvp.model.entity

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/1 18:45
 * @Description : 文件描述
 */
import java.io.Serializable
/**
 * 轮播图
 */
data class BannerResponse(
    var desc: String,
    var id: Int,
    var imagePath: String,
    var isVisible: Int,
    var order: Int,
    var title: String,
    var type: Int,
    var url: String):Serializable