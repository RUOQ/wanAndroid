package com.ruoq.wanAndroid.mvp.contract.main.home

import com.jess.arms.mvp.IModel
import com.jess.arms.mvp.IView
import com.ruoq.wanAndroid.mvp.model.entity.ApiPagerResponse
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import com.ruoq.wanAndroid.mvp.model.entity.ArticleResponse
import com.ruoq.wanAndroid.mvp.model.entity.BannerResponse
import io.reactivex.Observable

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/3 14:11
 * @Description : 文件描述
 */
interface HomeContract {
    //对于经常使用给关于UI的方法可以定义到IView中，如显示隐藏进度条，和显示文字消息
    interface View:IView{
        fun requestBannerSuccess(banners:MutableList<BannerResponse>)
        fun requestArticleSuccess(articles:ApiPagerResponse<MutableList<ArticleResponse>>)
        fun requestArticleFailed(errorMsg:String)
        fun collect(collected:Boolean,position:Int)
    }

    /**
     * Model层定义接口，外部只关心Modle返回的数据，无需关心内部细节，以及是否使用缓存
     */
    interface Model:IModel{
        fun getArticleList(pageNo:Int) :Observable<ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>>
        fun getTopArticleList():Observable<ApiResponse<MutableList<ArticleResponse>>>
        fun getBannerList():Observable<ApiResponse<MutableList<BannerResponse>>>
        fun collect(id:Int):Observable<ApiResponse<Any>>
        fun unCollect(id:Int):Observable<ApiResponse<Any>>
    }
}