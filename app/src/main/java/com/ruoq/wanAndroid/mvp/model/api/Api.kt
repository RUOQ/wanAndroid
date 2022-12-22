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
    @GET("/article/list/{page}/json")
    fun getArticleList(@Path("page") pageNo:Int):Observable
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
     * 取消收藏
     */
    @POST("/lg/uncollect/{id}/json")
    fun unCollectList(@Path("id") id:Int,@Query("originId") originId:Int):
            Observable<ApiResponse<Any>>

    /**
     * 项目分类
     */
    @GET("/project/tree/json")
    fun getProjectTypes():Observable<ApiResponse<MutableList<ClassifyResponse>>>

    /**
     * 根据分类id 获取项目数据
     */
    @GET("/project/list/{page}/json")
    fun getProjectDataByType(@Path("page") pageNo:Int,@Query("cid") cid:Int):
            Observable<ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>>


    /**
     * 获取最新项目数据
     */
    /**
     * 获取最新项目数据
     */
    @GET("/article/listproject/{page}/json")
    fun getProjectNewData(@Path("page") pageNo: Int): Observable<ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>>

    /**
     * 公众号分类
     */
    @GET("/wxarticle/chapters/json")
    fun getPublicTypes(): Observable<ApiResponse<MutableList<ClassifyResponse>>>

    /**
     * 获取公众号数据
     */
    @GET("/wxarticle/list/{id}/{page}/json")
    fun getPublicNewData(@Path("page") pageNo:Int,@Path("id") id:Int)
:Observable<ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>>


    /**
     * 获取热门搜索数据
     */
    @GET("/hotkey/json")
    fun getSearchData(): Observable<ApiResponse<MutableList<SearchResponse>>>

    /**
     * 根据关键词搜索数据
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
            Observable<ApiResponse<ApiPagerResponse<MutableList<CollectResponse>>>>

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

    /**
     * 获取当前账号的个人积分
     */
    @GET("/lg/coin/userinfo/json")
    fun getIntegral():Observable<ApiResponse<IntegralResponse>>

    /**
     * 获取积分排行榜
     */
    @GET("/coin/rank/{page}/json")
    fun getIntegralRank(@Path("page") page:Int):
            Observable<ApiResponse<ApiPagerResponse<MutableList<IntegralResponse>>>>

    /**
     * 获取积分历史
     */
    @GET("/lg/coin/list/{page}/json")
    fun getIntegralHistory(@Path("page") page:Int):
            Observable<ApiResponse<ApiPagerResponse<MutableList<IntegralHistoryResponse>>>>


    /**
     * 获取Todo列表数据，根据完成时间排序
     */
    @GET("/lg/todo/v2/list/{page}/json")
    fun getTodoData(@Path("page") page:Int):Observable<ApiResponse<ApiPagerResponse<MutableList<TodoResponse>>>>

    /**
     * 添加一个todo
     */
    @POST("/lg/todo/add/json")
    @FormUrlEncoded
    fun addTodo(@Field("title") title:String,
                @Field("content") content:String,
                @Field("date") date:String,
                @Field("type") type:Int,
                @Field("priority") priority:Int):Observable<ApiResponse<Any>>

    /**
     * 修改一个Todo
     */
    @POST("/lg/todo/update/{id}/json")
    @FormUrlEncoded
    fun updateTodo(@Field("title") title: String,
                   @Field("content") content:String,
                   @Field("date") date:String,
                   @Field("type") type:Int,
                   @Field("priority") priority:Int,
                   @Path("id") id:Int
                   ):Observable<ApiResponse<Any>>

    /**
     * 完成一个TODO
     */
    @POST("/lg/todo/done/{id}/json")
    @FormUrlEncoded
    fun doneTodo(@Path("id")id:Int,
                 @Field("status") status:Int
    ):Observable<ApiResponse<Any>>



    /**
     * 删除一个TODO
     */
    @POST("/lg/todo/delete/{id}/json")
    fun deleteTodo(@Path("id") id: Int): Observable<ApiResponse<Any>>

    /**
     * 广场列表数据
     */
    @GET("/user_article/list/{page}/json")
    fun getSquareData(@Path("page") page:Int) :
            Observable<ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>>


    /**
     * 获取分享文章列表数据
     */
    @GET("/user/lg/private_articles/{page}/json")
    fun getShareData(@Path("page") page:Int):Observable<ApiResponse<ShareResponse>>


    /**
     * 删除自己分享的文章
     */
    @POST("/lg/user_article/delete/{id}/json")
    fun deleteShareData(@Path("id") id:Int):Observable<ApiResponse<Any>>

    /**
     * 添加文章
     */
    @POST("/lg/user_article/add/json")
    @FormUrlEncoded
    fun addArticle(@Field("title") title:String,@Field("link") content: String):
            Observable<ApiResponse<Any>>

    /**
     * 获取分享文章列表数据
     *
     */
    @GET("/user/{id}/share_articles/{page}/json")
    fun getShareByidData(@Path("page") page:Int,@Path("id") id:Int)
    :Observable<ApiResponse<ShareResponse>>
}
