package com.example.liedetector.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.liedetector.fragment.IntroOneFragment
import com.example.liedetector.fragment.IntroTwoFragment

class IntroAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {


    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> IntroOneFragment()

            else -> IntroTwoFragment()
        }
    }

}
