package com.tuk.lightninggathering

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tuk.lightninggathering.databinding.ActivityNaviBinding


class MainActivity : AppCompatActivity() {
    private companion object {
        const val TAG_CREATE = "create_fragment"
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
                R.id.tab_create -> setFragment(TAG_CREATE, CreateFragment())
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

        val create = manager.findFragmentByTag(TAG_CREATE)
        val home = manager.findFragmentByTag(TAG_HOME)
        val user = manager.findFragmentByTag(TAG_USER)
        val search = manager.findFragmentByTag(TAG_SEARCH)

        val fragments = arrayOf(create, home, user, search)
        fragments.forEach { frag ->
            frag?.let { fragTransaction.hide(it) }
        }

        when (tag) {
            TAG_CREATE -> if (create != null) fragTransaction.show(create)
            TAG_HOME -> if (home != null) fragTransaction.show(home)
            TAG_USER -> if (user != null) fragTransaction.show(user)
            TAG_SEARCH -> if (search != null) fragTransaction.show(search)
        }

        fragTransaction.commitAllowingStateLoss()
    }
}