package com.ruoq.wanAndroid.mvp.contract.main.me

import com.jess.arms.mvp.IModel
import com.jess.arms.mvp.IView
import com.ruoq.wanAndroid.mvp.model.entity.ApiResponse
import com.ruoq.wanAndroid.mvp.model.entity.IntegralResponse
import io.reactivex.Observable

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/3 15:37
 * @Description : 文件描述
 */
interface MeContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView {
        //获取积分回调
        fun getIntegralSuccess(integral: IntegralResponse)
        fun getIntegralFailed(errorMsg: String)
    }
    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model : IModel {
        fun getIntegral(): Observable<ApiResponse<IntegralResponse>>
    }

}