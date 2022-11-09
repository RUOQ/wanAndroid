package com.ruoq.wanAndroid.mvp.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ruoq.wanAndroid.R
import com.ruoq.wanAndroid.app.utils.DatetimeUtil
import com.ruoq.wanAndroid.app.utils.SettingUtil
import com.ruoq.wanAndroid.mvp.model.entity.IntegralHistoryResponse

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/8 23:14
 * @Description : 积分获取历史
 */
class IntegralHistoryAdapter(data: ArrayList<IntegralHistoryResponse>?) :
    BaseQuickAdapter<IntegralHistoryResponse, BaseViewHolder>(R.layout.item_integral_history) {
    override fun convert(helper: BaseViewHolder, item: IntegralHistoryResponse?) {
        //赋值
        item?.run {
            val descStr =
                if (desc.contains("积分")) desc.subSequence(desc.indexOf("积分"), desc.length) else ""
            helper.setText(R.id.item_integralhistory_title, reason + descStr)
            helper.setText(
                R.id.item_integralhistory_date,
                DatetimeUtil.formatDate(date, DatetimeUtil.DATE_PATTERN_SS)
            )
            helper.setText(R.id.item_integralhistory_count, "+$coinCount")
            helper.setTextColor(R.id.item_integralhistory_count, SettingUtil.getColor(mContext))
        }
    }
}
