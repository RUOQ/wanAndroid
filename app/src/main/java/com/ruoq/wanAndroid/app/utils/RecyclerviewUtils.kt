package com.ruoq.wanAndroid.app.utils

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.ruoq.wanAndroid.app.weight.DefineLoadMoreView
import com.yanzhenjie.recyclerview.SwipeRecyclerView

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/21 0:16
 * @Description : 文件描述
 */
class RecyclerViewUtils {

    fun  initRecyclerView(context: Context, recyclerview: SwipeRecyclerView, loadmoreListener: SwipeRecyclerView.LoadMoreListener): DefineLoadMoreView {
        val footerView = DefineLoadMoreView(context)
        recyclerview.addFooterView(footerView)
        recyclerview.setLoadMoreView(footerView)//添加加载更多尾部
        recyclerview.layoutManager = LinearLayoutManager(context)
        recyclerview.setHasFixedSize(true)
        recyclerview.setLoadMoreListener(loadmoreListener)//设置加载更多回调
        footerView.setLoadMoreListener(SwipeRecyclerView.LoadMoreListener {
            //设置尾部点击回调
            footerView.onLoading()
            loadmoreListener.onLoadMore()
        })
        return footerView
    }

}