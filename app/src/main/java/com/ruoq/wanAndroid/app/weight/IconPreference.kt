package com.ruoq.wanAndroid.app.weight

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.ruoq.wanAndroid.R
import com.ruoq.wanAndroid.app.utils.SettingUtil

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2023/1/10 13:57
 * @Description : 文件描述
 */
class IconPreference(context: Context, attrs:AttributeSet):Preference(context, attrs) {
    var circleImageView:MyColorCircleView ?= null
    init {
        widgetLayoutResource = R.layout.item_icon_preference_preview
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        val color = SettingUtil.getColor(context)
        circleImageView = holder?.itemView?.findViewById(R.id.iv_preview)
        circleImageView?.color = color
        circleImageView?.border = color
    }

    fun setView(){
        val color = SettingUtil.getColor(context)
        circleImageView?.color = color
        circleImageView?.border = color
    }
}