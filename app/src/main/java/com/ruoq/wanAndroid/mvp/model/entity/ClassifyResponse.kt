package com.ruoq.wanAndroid.mvp.model.entity

import java.io.Serializable

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/10/31 19:51
 * @Description : 文件描述
 */

data class ClassifyResponse(var children:List<Any>,
                            var courseId:Int,
                            var id:Int,
                            var name:String,
                            var order:Int,
                            var parentChapterId:Int,
                            var userControlSetTop:Boolean,
                            var visible:Int
): Serializable