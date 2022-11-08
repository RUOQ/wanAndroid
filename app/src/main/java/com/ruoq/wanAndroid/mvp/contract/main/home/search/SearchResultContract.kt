package com.ruoq.wanAndroid.mvp.contract.main.home.search

import com.jess.arms.mvp.IModel
import com.jess.arms.mvp.IView
import com.ruoq.wanAndroid.mvp.model.entity.ApiPagerResponse
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import com.ruoq.wanAndroid.mvp.model.entity.ArticleResponse
import io.reactivex.Observable

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/3 15:33
 * @Description : 文件描述
 */
interface SearchResultContract {
    interface View: IView {
        fun requestArticleListSuccess(articles:ApiPagerResponse<MutableList<ArticleResponse>>)
        fun requestArticleFailed(errorMsg:String)
        fun collect(collect:Boolean,position:Int)

    }

    interface Model:IModel{
        fun getArticleList(pageNo:Int,searchKey:String):Observable<ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>>

        fun collect(id:Int):Observable<ApiResponse<Any>>
        fun unCollect(id:Int):Observable<ApiResponse<Any>>
    }
}