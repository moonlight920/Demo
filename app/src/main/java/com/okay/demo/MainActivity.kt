package com.okay.demo

import android.database.DataSetObserver
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.AbsListView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val demo1Fragment = Demo1Fragment.newInstance()
    private val demo2Fragment = Demo2Fragment.newInstance()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                showFragment(demo1Fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                showFragment(demo2Fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
//                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        showFragment(demo1Fragment)

        val adapter = ArrayAdapter(
            this, android.R.layout.simple_list_item_1,
            (0..100).toList()
        )
        adapter.registerDataSetObserver(object : DataSetObserver() {
            override fun onChanged() {
                Log.d("syf", "onChange")
            }

            override fun onInvalidated() {
                Log.d("syf", "onInvalidated")
            }
        })
        listView.adapter = adapter
        listView.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(
                view: AbsListView?,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {
                Log.d("syf", "firstVisiblePosition${listView.firstVisiblePosition}")
                Log.d("syf", "lastVisiblePosition${listView.lastVisiblePosition}")
            }

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {

            }
        })
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }
}
