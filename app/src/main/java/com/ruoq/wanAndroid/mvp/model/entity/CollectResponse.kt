package com.ruoq.wanAndroid.mvp.model.entity

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/1 18:47
 * @Description : 文件描述
 */
import java.io.Serializable

data class CollectResponse(var chapterId: Int,
                           var author: String,
                           var chapterName: String,
                           var courseId: Int,
                           var desc: String,
                           var envelopePic: String,
                           var id: Int,
                           var link: String,
                           var niceDate: String,
                           var origin: String,
                           var originId: Int,
                           var publishTime: Long,
                           var title: String,
                           var userId: Int,
                           var visible: Int,
                           var zan: Int):Serializable