package com.setianjay.movieapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.setianjay.movieapp.fragment.NowPlayingFragment
import com.setianjay.movieapp.fragment.PopularFragment

class TabLayoutAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager,lifecycle) {

    // Kita isi fragmentnya ada apa ajj
    val fragments: List<Fragment> = listOf(
        NowPlayingFragment(),
        PopularFragment()
    )
    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]


}