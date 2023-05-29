package com.tuk.lightninggathering

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabItem

class GatheringDetailActivity : AppCompatActivity() {

    // ViewPager를 위한 Adapter
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    // DetailsFragment에 전달할 데이터를 Bundle에 넣습니다.
                    val bundle = Bundle().apply {
                        // 아래의 "memo", "date" 등은 예시이므로 실제 앱에 필요한 데이터로 바꾸어주세요.
                        putString("memo", "example memo")
                        putString("date", "example date")
                        putString("placeName", "example placeName")
                        putString("placeAddress", "example placeAddress")
                        putDouble("latitude", 0.0)
                        putDouble("longitude", 0.0)
                    }

                    // DetailsFragment를 생성하고 Bundle을 설정합니다.
                    DetailsFragment().apply {
                        arguments = bundle
                    }
                }
                1 -> MemberFragment() // MemberFragment를 반환합니다.
                2 -> ReviewFragment() // ReviewFragment를 반환합니다.
                else -> throw IllegalStateException("Unexpected position: $position")
            }
        }

        override fun getCount() = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gathering_detail)

        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.container)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        tabs.getTabAt(0)?.text = getString(R.string.details)
        tabs.getTabAt(1)?.text = getString(R.string.member)
        tabs.getTabAt(2)?.text = getString(R.string.reviews)
    }
}
