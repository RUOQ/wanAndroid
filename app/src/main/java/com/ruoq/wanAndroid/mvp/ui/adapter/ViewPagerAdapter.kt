package com.ruoq.wanAndroid.mvp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import me.yokeyword.fragmentation.SupportFragment

/**
 * @ProjectName : wanAndroid
 * @Author : RUOQ
 * @Time : 2022/11/9 15:34
 * @Description : 文件描述
 */
class ViewPagerAdapter(fm: FragmentManager, private val fragments: List<SupportFragment>) : FragmentPagerAdapter(fm) {

    override fun getItem(i: Int): Fragment {
        return fragments[i]
    }

    override fun getCount(): Int {
        return fragments.size
    }
}