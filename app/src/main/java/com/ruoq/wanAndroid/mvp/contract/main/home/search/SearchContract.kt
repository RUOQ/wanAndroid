package com.ruoq.wanAndroid.mvp.contract.main.home.search

import com.jess.arms.mvp.IModel
import com.jess.arms.mvp.IView
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import com.ruoq.wanAndroid.mvp.model.entity.SearchResponse
import io.reactivex.Observable

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/3 15:24
 * @Description : 文件描述
 */
interface SearchContract {
    interface View:IView{
        fun requestSearchSuccess(tagData:MutableList<SearchResponse>)
    }

    interface Model: IModel {
        fun getHotData():Observable<ApiResponse<MutableList<SearchResponse>>>
    }
}