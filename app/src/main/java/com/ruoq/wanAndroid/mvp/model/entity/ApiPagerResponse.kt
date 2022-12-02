package com.ruoq.wanAndroid.mvp.model.entity

import java.io.Serializable

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/10/31 20:11
 * @Description : 文件描述
 */
data class ApiPagerResponse<T>(var datas:T,
                              var curPage:Int,
                               var offset:Int,
                               var over:Boolean,
                               var pageCount:Int,
                               var size:Int,
                               var total:Int):Serializable


