package com.ruoq.wanAndroid.mvp.model.api

import com.ruoq.wanAndroid.mvp.model.entity.*
import io.reactivex.Observable
import io.reactivex.Observer
import retrofit2.http.*

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/1 22:07
 * @Description : 文件描述
 */
interface Api {
    companion object {
        const val APP_DOMAIN = "https://www.wanandroid.com"
    }

    /**
     * 登录
     * */
    @FormUrlEncoded
    @POST("/user/login")
    fun login(@Field("username") username:String,
         @Field("password") pwd:String,
    ):Observable<ApiResponse<UserInfoResponse>>


    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("/user/register")
    fun register(@Field("username") username: String,
                 @Field("password") pwd: String,
                 @Field("repassword") rpwd:String
    ):Observable<ApiResponse<Any>>

    /**
     * 获取banner的数据
     */
    @GET("/banner/json")
    fun getBanner():Observable<ApiResponse<MutableList<BannerResponse>>>

    /**
     * 获取置顶文章的数据
     */
    @GET("/article/top/json")
    fun getTopArticleList():Observable<ApiResponse<MutableList<ArticleResponse

            >>>


    /**
     * 获取首页文章数据
     */
    @GET("article/list/{page}/json")
    fun getArticleList(@Path("Page") PageNo:Int):Observable
    <ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>>

    /**
     * 收藏
     */
    @POST("/lg/collect/{id}/json")
    fun collect(@Path("id") id:Int):Observable<ApiResponse<Any>>

    /**
     * 取消收藏
     */
    @POST("/lg/unCollect_originId/{id}/json")
    fun unCollect(@Path("id") id:Int):Observable<ApiResponse<Any>>

    /**
     * 项目分类
     */
    @GET("/project/tree/json")
    fun getProjectTypes():Observable<ApiResponse<MutableList<ClassifyResponse>>>

    /**
     * 根据分类id 获取项目数据
     */
    @GET("/project/list/{page}/json")
    fun getProjectDataByType(@Path("page") pageNo:Int):
            Observable<ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>>


    /**
     * 公众号分类
     */
    @GET("/wxarticle/list/{id}/{page}/json")
    fun getPublicNewData(@Path("page") pageNo:Int,@Path("id") id:Int)
:Observable<ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>>


    /**
     * 获取热门搜索数据
     */
    @POST("/article/query/{page}/json")
    fun getSearchDataByKey(@Path("page")pageNo: Int,
                           @Query("K") searchKey:String):
            Observable<ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>>

    /**
     * 获取体系数据
     */
    @GET("/tree/json")
    fun getSystemData():Observable<ApiResponse<MutableList<SystemResponse>>>

    /**
     * 知识体系下的文章数据
     */
    @GET("/article/list/{page}/json")
    fun getArticleDataByTree(@Path("page") pageNo:Int,
                             @Query("cid") cid:Int
    ):Observable<ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>>

    /**
     * 获取导航数据
     */
    @GET("/navi/json")
    fun getNavigationData():Observable<ApiResponse<MutableList<NavigationResponse>>>

    /**
     * 获取收藏文章数据
     */
    @GET("/lg/collect/list/{page}/json")
    fun getCollectData(@Path("page") pageNo: Int):
            Observable<ApiResponse<ApiPagerResponse<MutableList<CollectUrlResponse>>>>

    /**
     * 获取收藏网址的数据
     */
    @GET("/lg/collect/usertools/json")
    fun getCollectUrlData():Observable<ApiResponse<MutableList<CollectUrlResponse>>>

    /**
     * 收藏网址
     */
    @POST("/lg/collect/addtool/json")
    fun collectUrl(@Query("name") name:String,@Query("link") link:String):
            Observable<ApiResponse<CollectUrlResponse>>

    /**
     * 取消收藏网址
     */
    @POST("/lg/collect/deletetool/json")
    fun deleteTool(@Query("id") id:Int):Observable<ApiResponse<Any>>


}
