package com.tuk.lightninggathering

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class GatheringDetailActivity : AppCompatActivity() {

    private lateinit var title: String
    private lateinit var memo: String
    private lateinit var date: String
    private lateinit var placeName: String
    private lateinit var placeAddress: String
    private lateinit var category: String
    private var currentPeople: Int = 4
    private var maxPeople: Int = 8
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    val bundle = Bundle().apply {
                        putString("memo", memo)
                        putString("date", date)
                        putString("placeName", placeName)
                        putString("placeAddress", placeAddress)
                        putString("category", category)
                        putDouble("latitude", latitude)
                        putDouble("longitude", longitude)
                    }

                    DetailsInfoFragment().apply {
                        arguments = bundle
                    }
                }
                1 -> MemberFragment()
                2 -> ReviewFragment()
                else -> throw IllegalStateException("Unexpected position: $position")
            }
        }

        override fun getCount() = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gathering_detail)
        val showTitle: TextView = findViewById(R.id.showTitle);
        val showPeopleNum: TextView = findViewById(R.id.showPeoplenum);
        val toolbar: Toolbar? = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        title = "롤 5:5 하실분 구합니다!" // intent.getStringExtra("title")!!
        memo = "실버 이상, 플래티넘 이하 분들로 구합니다!" // intent.getStringExtra("memo")!!
        date = "2023년 6월 1일" // intent.getStringExtra("date")!!
        placeName = "정왕동 보보스 PC방" // intent.getStringExtra("placeName")!!
        placeAddress = "정왕동 1302-5" // intent.getStringExtra("placeAddress")!!
        category = "리그오브레전드" // intent.getStringExtra("category")!!
        currentPeople = 6 // intent.getIntExtra("currentPeople")!!
        maxPeople = 10 // intent.getIntExtra("maxPeople")!!
        latitude = 5.0 // intent.getDoubleExtra("latitude", 0.0)
        longitude = 5.0 // intent.getDoubleExtra("longitude", 0.0)

        showTitle.text = title
        showPeopleNum.text = "인원: ${currentPeople} / ${maxPeople}"

        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.container)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        tabs.getTabAt(0)?.text = getString(R.string.details)
        tabs.getTabAt(1)?.text = getString(R.string.member)
        tabs.getTabAt(2)?.text = getString(R.string.reviews)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
