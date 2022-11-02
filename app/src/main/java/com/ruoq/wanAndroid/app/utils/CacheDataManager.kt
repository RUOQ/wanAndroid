package com.ruoq.wanAndroid.app.utils

import android.content.Context
import android.os.Environment
import com.jess.arms.integration.AppManager
import com.jess.arms.utils.DataHelper.deleteDir
import java.io.File
import java.lang.Exception
import java.math.BigDecimal

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/10/27 22:38
 * @Description : 文件描述
 */
object CacheDataManager {

    /**
     * 获取App文件缓存大小*/
    fun getTotalCacheSize(context: Context):String{
        var cacheSize = getFolderSize(context.cacheDir)
        if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED){
            cacheSize += getFolderSize(context.externalCacheDir)
        }
        return getFormatSize(cacheSize.toDouble())
    }



    /**
     * 清除所有的缓存数据
     * */

    fun clearAllCache(context: Context){
        deleteDir(context.cacheDir)
        if(Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED){
            if(context.externalCacheDir == null){
                AppManager.getAppManager().currentActivity?.let {
                    ShowUtils.showDialog(it,"清理缓存失败")
                }
                return
            }
            context.externalCacheDir?.let {
                deleteDir(it)
            }
        }
    }

    private fun deleteDir(dir: File): Boolean {
        if (dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        return dir.delete()
    }


    /**
     * 获取文件
     *Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/
     * 目录，一般放一些长时间保存的数据
     * Context.getExternalCacheDir() -->
     * SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    * */
    fun getFolderSize(file: File?):Long{
        var size:Long = 0
        file?.run{
            try{
                val fileList = listFiles()
                fileList?.let{
                    for(index in fileList.indices){
                        size += if(fileList[index].isDirectory){
                            getFolderSize(fileList[index])
                        }else{
                            fileList[index].length()
                        }
                    }
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        return size
    }

    /**
     * 格式化单位
     * */
    fun getFormatSize(size:Double):String{
        val kiloByte = size / 1024
        if(kiloByte < 1){
            return size.toString() + "Byte"
        }
        val megaByte = kiloByte / 1024

        if(megaByte < 1){
            val resultMega = BigDecimal(kiloByte.toString())
            return resultMega.setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString() + "KB"
        }

        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(java.lang.Double.toString(megaByte))
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
        }
        val teraBytes = gigaByte / 1024

        if (teraBytes < 1) {
            val result3 = BigDecimal(java.lang.Double.toString(gigaByte))
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
        }

        val result4 = BigDecimal(teraBytes)
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }
}