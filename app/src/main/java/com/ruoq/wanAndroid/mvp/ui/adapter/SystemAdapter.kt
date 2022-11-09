package com.ruoq.wanAndroid.mvp.ui.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ruoq.wanAndroid.R
import com.ruoq.wanAndroid.app.utils.ColorUtil
import com.ruoq.wanAndroid.mvp.model.entity.ClassifyResponse
import com.ruoq.wanAndroid.mvp.model.entity.SystemResponse
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/9 15:21
 * @Description : 文件描述
 */
class SystemAdapter(data: MutableList<SystemResponse>?) :
    BaseQuickAdapter<SystemResponse, BaseViewHolder>(R.layout.item_system, data) {
    var tagClicklistener: TagClicklistener? = null

    override fun convert(helper: BaseViewHolder?, item: SystemResponse?) {
        item?.let {
            helper?.setText(R.id.item_system_title, Html.fromHtml(it.name))
            helper?.getView<TagFlowLayout>(R.id.item_system_flowlayout)?.run {
                adapter = object : TagAdapter<ClassifyResponse>(it.children) {
                    override fun getView(
                        parent: FlowLayout?,
                        position: Int,
                        hotSearchBean: ClassifyResponse?
                    ): View {
                        return LayoutInflater.from(parent?.context)
                            .inflate(R.layout.flow_layout, this@run, false)
                            .apply {
                                findViewById<TextView>(R.id.flow_tag).apply {
                                    text = Html.fromHtml(hotSearchBean?.name)
                                    setTextColor(ColorUtil.randomColor())
                                }
                            }
                    }
                }
                setOnTagClickListener { view, position, parent ->
                    tagClicklistener?.onClick(helper.adapterPosition, position)
                    false
                }
            }
        }
    }

    fun setTagClickListener(tagClicklistener: TagClicklistener) {
        this.tagClicklistener = tagClicklistener
    }

    interface TagClicklistener {
        fun onClick(position: Int, childPosition: Int)
    }


}