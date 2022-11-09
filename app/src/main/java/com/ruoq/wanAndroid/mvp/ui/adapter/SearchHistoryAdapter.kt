package com.ruoq.wanAndroid.mvp.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ruoq.wanAndroid.R

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/9 15:09
 * @Description : 文件描述
 */
class SearchHistoryAdapter(data: MutableList<String>?) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_history,data) {
    override fun convert(helper: BaseViewHolder?, item: String?) {
        item?.let {
            helper?.setText(R.id.item_history_text,it)
            helper?.addOnClickListener(R.id.item_history_del)
        }
    }

}