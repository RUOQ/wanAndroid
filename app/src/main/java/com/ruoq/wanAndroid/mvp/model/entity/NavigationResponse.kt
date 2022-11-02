package com.ruoq.wanAndroid.mvp.model.entity

import java.io.Serializable

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/10/31 20:07
 * @Description : 文件描述
 */
data class NavigationResponse(var articles: MutableList<AriticleResponse>,
                              var cid: Int,
                              var name: String):Serializable