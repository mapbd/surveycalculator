package org.map_bd.surveycalculator

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.clearFragmentResultListener
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.map_bd.surveycalculator.rod.FragmentOne

class ViewPagerFragmentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return FragmentOne()
            1 -> return FragmentTwo()
        }
        return FragmentOne()
    }

}