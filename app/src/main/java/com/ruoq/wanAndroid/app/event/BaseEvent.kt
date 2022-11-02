package com.ruoq.wanAndroid.app.event

import org.greenrobot.eventbus.EventBus

open class BaseEvent {
    fun post(){
        EventBus.getDefault().post(this)
    }
}