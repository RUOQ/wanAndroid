package com.ruoq.wanAndroid.app.utils

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/12/20 22:18
 * @Description : 文件描述
 */
//leftRight 为横向间的距离，topBottom为纵向间的距离
class SpaceItemDecoration(
    private val leftRight:Int,private val topBottom:Int
) : RecyclerView.ItemDecoration() {
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
     val layoutManager = parent.layoutManager as LinearLayoutManager?
        //竖直方向的
        if(layoutManager!!.orientation == LinearLayoutManager.VERTICAL){
            //最后一项需要bottom
            if(parent.getChildAdapterPosition(view) == layoutManager.itemCount - 1){
                outRect.bottom = topBottom
            }
            outRect.top = topBottom
            outRect.left = leftRight
            outRect.right = leftRight
        }else{
            //最后一项需要right
            if(parent.getChildAdapterPosition(view) != layoutManager.itemCount - 1){
                outRect.right = leftRight
            }
            outRect.top = topBottom
            outRect.left = 0
            outRect.bottom = topBottom
        }

    }
}