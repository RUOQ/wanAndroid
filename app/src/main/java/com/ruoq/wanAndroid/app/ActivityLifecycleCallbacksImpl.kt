package com.ruoq.wanAndroid.app

import android.app.Activity
import android.app.Application
import android.os.Bundle

import timber.log.Timber

/**
 * ================================================
 * 展示 [Application.ActivityLifecycleCallbacks] 的用法
 * Created by JessYan on 04/09/2017 17:14
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */

class ActivityLifecycleCallbacksImpl : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, p1: Bundle?) {
        Timber.i("$activity - onActivityCreated")

    }

    override fun onActivityStarted(activity: Activity) {
        Timber.i("$activity - onActivityStarted")

    }

    override fun onActivityResumed(activity: Activity) {
        Timber.i("$activity - onActivityResumed")

    }

    override fun onActivityPaused(activity: Activity) {
        Timber.i("$activity - onActivityPaused")

    }

    override fun onActivityStopped(activity: Activity) {
        Timber.i("$activity - onActivityStopped")

    }

    override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
        Timber.i("$activity - onActivitySaveInstanceState")

    }

    override fun onActivityDestroyed(activity: Activity) {
        Timber.i("$activity - onActivityDestroyed")

    }

}
