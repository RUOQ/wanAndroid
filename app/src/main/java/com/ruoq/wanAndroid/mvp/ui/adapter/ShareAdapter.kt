package com.ruoq.wanAndroid.mvp.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ruoq.wanAndroid.R
import com.ruoq.wanAndroid.mvp.model.entity.ArticleResponse

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/9 15:20
 * @Description : 分享的文章 adapter
 */

/**
 * 分享的文章 adapter
 * @Author:         hegaojian
 * @CreateDate:     2019/9/1 9:52
 */
class ShareAdapter(data: ArrayList<ArticleResponse>?) : BaseQuickAdapter<ArticleResponse, BaseViewHolder>(R.layout.item_share_article, data) {

    override fun convert(helper: BaseViewHolder, item: ArticleResponse?) {
        //赋值
        item?.run {
            helper.setText(R.id.item_share_title, title)
            helper.setText(R.id.item_share_date, niceDate)
            helper.addOnClickListener(R.id.item_share_del)
        }
    }
}
