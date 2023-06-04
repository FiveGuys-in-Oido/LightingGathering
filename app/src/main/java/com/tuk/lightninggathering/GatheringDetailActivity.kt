package com.tuk.lightninggathering

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GatheringDetailActivity : AppCompatActivity() {

    // ViewPager를 위한 Adapter
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        private val fragmentList = ArrayList<Fragment>()

        fun addFragment(fragment: Fragment) {
            fragmentList.add(fragment)
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount() = fragmentList.size
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gathering_detail)

        val meetingKey = intent.getStringExtra("meetingKey")
//        val meetingKey = "-NX5hZ_4p0E2oWYV7atq"

        FirebaseApp.initializeApp(this)
        val db = FirebaseDatabase.getInstance()
        val meetingsRef = db.getReference("meetings")
        val query = meetingsRef.orderByKey().equalTo(meetingKey)

        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (meetingSnapshot in dataSnapshot.children) {
                    val meeting = meetingSnapshot.getValue(Post::class.java)

                    val showTitle: TextView = findViewById(R.id.showTitle)
                    val showPeoplenum: TextView = findViewById(R.id.showPeoplenum)
                    showTitle.text = meeting?.title
                    showPeoplenum.text = "인원 수: ${meeting?.memberKeys?.size} / ${meeting?.maxMemberCount}"

                    // DetailsFragment에 전달할 데이터를 Bundle에 넣습니다.
                    val bundle = Bundle().apply {
                        putString("date", meeting?.date)
                        putString("location",  meeting?.location)
                        putDouble("latitude", meeting?.latitude!!)
                        putDouble("longitude", meeting?.longitude!!)
                        putString("description",  meeting?.description)
                        putString("category",  meeting?.category)
                    }

                    val detailsInfoFragment = DetailsInfoFragment()
                    detailsInfoFragment.arguments = bundle

                    // ViewPager에 DetailsInfoFragment 추가
                    sectionsPagerAdapter.addFragment(detailsInfoFragment)
                }

                // ViewPager 설정
                sectionsPagerAdapter.addFragment(MemberFragment())
                val viewPager: ViewPager = findViewById(R.id.container)
                viewPager.adapter = sectionsPagerAdapter
                val tabs: TabLayout = findViewById(R.id.tabs)
                tabs.setupWithViewPager(viewPager)
                tabs.getTabAt(0)?.text = getString(R.string.details)
                tabs.getTabAt(1)?.text = getString(R.string.member)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error
            }
        })
    }
}
