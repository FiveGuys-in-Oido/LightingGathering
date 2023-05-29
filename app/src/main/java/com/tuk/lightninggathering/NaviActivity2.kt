package com.tuk.lightninggathering

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import com.tuk.lightninggathering.databinding.ActivityNaviBinding

class NaviActivity2 : AppCompatActivity() {
    private companion object {
        const val TAG_LIKE = "like_fragment"
        const val TAG_HOME = "home_fragment"
        const val TAG_SEARCH = "search_fragment"
        const val TAG_USER = "user_fragment"
    }

    private lateinit var binding: ActivityNaviBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNaviBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragment(TAG_HOME, HomeFragment())

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tab_home -> setFragment(TAG_HOME, HomeFragment())
                R.id.tab_user -> setFragment(TAG_USER, UserFragment())
                R.id.tab_search -> setFragment(TAG_SEARCH, SearchFragment())
            }
            true
        }
    }

    private fun setFragment(tag: String, fragment: Fragment) {
        val manager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null) {
            fragTransaction.add(R.id.mainFrameLayout, fragment, tag)
        }

        val like = manager.findFragmentByTag(TAG_LIKE)
        val home = manager.findFragmentByTag(TAG_HOME)
        val user = manager.findFragmentByTag(TAG_USER)
        val search = manager.findFragmentByTag(TAG_SEARCH)

        val fragments = arrayOf(like, home, user, search)
        fragments.forEach { frag ->
            frag?.let { fragTransaction.hide(it) }
        }

        when (tag) {
            TAG_LIKE -> if (like != null) fragTransaction.show(like)
            TAG_HOME -> if (home != null) fragTransaction.show(home)
            TAG_USER -> if (user != null) fragTransaction.show(user)
            TAG_SEARCH -> if (search != null) fragTransaction.show(search)
        }

        fragTransaction.commitAllowingStateLoss()
    }
}