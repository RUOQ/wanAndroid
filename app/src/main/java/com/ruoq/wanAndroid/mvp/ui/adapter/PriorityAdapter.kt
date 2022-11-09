package com.ruoq.wanAndroid.mvp.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ruoq.wanAndroid.R
import com.ruoq.wanAndroid.app.weight.MyColorCircleView
import com.ruoq.wanAndroid.mvp.model.entity.enums.TodoType

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/9 14:57
 * @Description : 文件描述
 */
class PriorityAdapter(data:ArrayList<TodoType>?):
BaseQuickAdapter<TodoType,BaseViewHolder>(R.layout.item_todo_dialog,data){
    var checkType= TodoType.TodoType1.type
    constructor(data:ArrayList<TodoType>?,checkType:Int):this(data){
        this.checkType = checkType
    }
    override fun convert(helper: BaseViewHolder, item: TodoType?) {
        item?.run{
            helper.setText(R.id.item_todo_dialog_name,item.content)
            if(checkType == item.type){
                helper.getView<MyColorCircleView>(R.id.item_todo_dialog_icon).setViewSelect(item.color)
            }else{
                helper.getView<MyColorCircleView>(R.id.item_todo_dialog_icon).setView(item.color)
            }
        }
    }
}