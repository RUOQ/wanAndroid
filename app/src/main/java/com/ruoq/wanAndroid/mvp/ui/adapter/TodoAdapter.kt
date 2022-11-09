package com.ruoq.wanAndroid.mvp.ui.adapter

import android.util.TypedValue
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ruoq.wanAndroid.R
import com.ruoq.wanAndroid.app.utils.DatetimeUtil
import com.ruoq.wanAndroid.app.utils.SettingUtil
import com.ruoq.wanAndroid.mvp.model.entity.TodoResponse
import com.ruoq.wanAndroid.mvp.model.entity.enums.TodoType

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/9 15:26
 * @Description : 文件描述
 */
class TodoAdapter(data:ArrayList<TodoResponse>?):
    BaseQuickAdapter<TodoResponse,BaseViewHolder>(R.layout.item_todo,data){
    override fun convert(helper: BaseViewHolder, item: TodoResponse?) {
        //赋值
        item?.run {
            helper.setText(R.id.item_todo_title, title)
            helper.setText(R.id.item_todo_content, content)
            helper.setText(R.id.item_todo_date, dateStr)
            helper.addOnClickListener(R.id.item_todo_setting)

            if (status == 1) {
                //已完成
                helper.setVisible(R.id.item_todo_status, true)
                helper.setImageResource(R.id.item_todo_status, R.drawable.ic_done)
                helper.getView<CardView>(R.id.item_todo_cardview).foreground = mContext.getDrawable(R.drawable.forground_shap)
            } else {
                if (date < DatetimeUtil.nows.time) {
                    //未完成并且超过了预定完成时间
                    helper.setVisible(R.id.item_todo_status, true)
                    helper.setImageResource(R.id.item_todo_status, R.drawable.ic_yiguoqi)
                    helper.getView<CardView>(R.id.item_todo_cardview).foreground = mContext.getDrawable(R.drawable.forground_shap)
                } else {
                    //未完成
                    helper.setVisible(R.id.item_todo_status, false)
                    TypedValue().apply {
                        mContext.theme.resolveAttribute(R.attr.selectableItemBackground, this, true)
                    }.run {
                        helper.getView<CardView>(R.id.item_todo_cardview).foreground = mContext.getDrawable(resourceId)
                    }
                }
            }
            helper.getView<ImageView>(R.id.item_todo_tag).imageTintList = SettingUtil.getOneColorStateList(
                TodoType.byType(priority).color)
        }
    }

}