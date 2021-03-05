package com.setianjay.movieapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TableLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.setianjay.movieapp.R
import com.setianjay.movieapp.adapter.TabLayoutAdapter

class HomeActivity : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager2
    lateinit var tabAdapter: TabLayoutAdapter // Adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initView()
        setUpFragment()
    }

    private fun initView() {
        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)
    }

    private fun setUpFragment(){
        tabAdapter = TabLayoutAdapter(supportFragmentManager,lifecycle)
        viewPager.adapter = tabAdapter

        val tabTitles = listOf("Now Playing","Popular")
        TabLayoutMediator(tabLayout,viewPager){title,position ->
            title.text = tabTitles[position]
        }.attach()
    }
}