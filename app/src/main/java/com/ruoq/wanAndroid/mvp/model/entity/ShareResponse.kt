package com.ruoq.wanAndroid.mvp.model.entity


import java.io.Serializable

data class ShareResponse(var coinInfo: CoinInfo,
                         var shareArticles: ApiPagerResponse<MutableList<AriticleResponse>>
):Serializable