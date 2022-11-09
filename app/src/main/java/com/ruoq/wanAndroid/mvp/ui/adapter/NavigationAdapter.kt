package com.ruoq.wanAndroid.mvp.ui.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ruoq.wanAndroid.R
import com.ruoq.wanAndroid.app.utils.ColorUtil
import com.ruoq.wanAndroid.mvp.model.entity.ArticleResponse
import com.ruoq.wanAndroid.mvp.model.entity.NavigationResponse
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout


/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/8 23:18
 * @Description : 导航 adapter
 */
class NavigationAdapter(data: MutableList<NavigationResponse>?) :
    BaseQuickAdapter<NavigationResponse, BaseViewHolder>(R.layout.item_system, data) {

    private var tagClickListener: TagClickListener? = null

    override fun convert(helper: BaseViewHolder?, item: NavigationResponse?) {
        item?.let {
            helper?.setText(R.id.item_system_title, it.name)
            helper?.getView<TagFlowLayout>(R.id.item_system_flowlayout)?.run {
                adapter = object : TagAdapter<ArticleResponse>(it.articles) {
                    override fun getView(
                        parent: FlowLayout?,
                        position: Int,
                        hotSearchBean: ArticleResponse?
                    ): View {
                        val layout = LayoutInflater.from(parent?.context)
                            .inflate(R.layout.flow_layout, this@run, false)
                        layout.findViewById<TextView>(R.id.flow_tag).apply {
                            text = Html.fromHtml(hotSearchBean?.title)
                            setTextColor(ColorUtil.randomColor())
                        }
                        return layout

                    }
                }
                setOnTagClickListener { _, position, _ ->
                    tagClickListener?.onClick(helper.adapterPosition, position)
                    false
                }
            }
        }
    }


    fun setTagClickListener(tagClickListener: TagClickListener) {
        this.tagClickListener = tagClickListener
    }

    interface TagClickListener {
        fun onClick(position: Int, childPosition: Int)
    }


}