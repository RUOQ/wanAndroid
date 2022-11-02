package com.ruoq.wanAndroid.app.event

import org.greenrobot.eventbus.EventBus

 class AddEvent():BaseEvent() {
    var code = TODO_CODE

     constructor(code:Int):this(){
         this.code = code
     }

     companion object{
         //添加清单
         const val TODO_CODE = 1
         const val SHARE_CODE = 2
         const val DELETE_CODE = 3
     }
}