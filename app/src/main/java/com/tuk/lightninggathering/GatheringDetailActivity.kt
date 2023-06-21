package com.tuk.lightninggathering

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GatheringDetailActivity : AppCompatActivity() {
    private var meeting: Post? = null  // Add this line

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

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 활성화
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val meetingKey = intent.getStringExtra("meetingKeys")

        FirebaseApp.initializeApp(this)
        val db = FirebaseDatabase.getInstance()
        val meetingsRef = db.getReference("meetings")
        val query = meetingsRef.orderByKey().equalTo(meetingKey)

        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (meetingSnapshot in dataSnapshot.children) {
                    meeting = meetingSnapshot.getValue(Post::class.java)

                    val showTitle: TextView = findViewById(R.id.showTitle)
                    val showPeoplenum: TextView = findViewById(R.id.showPeoplenum)
                    showTitle.text = meeting?.title
                    println(meeting?.memberKeys)
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

                    val joinBtn: Button = findViewById(R.id.joinBtn)

                    val firebaseAuth = FirebaseAuth.getInstance()
                    val user = firebaseAuth.currentUser
                    val userId = user?.uid ?: ""
                    if (meeting?.memberKeys?.contains(userId) == true) {
                        joinBtn.isEnabled = false
                    } else {
                        joinBtn.setOnClickListener {
                            joinMeeting(meetingSnapshot.key!!)
                        }
                    }

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

    private fun joinMeeting(meetingKey: String) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        val userId = user?.uid ?: ""
        val joinBtn: Button = findViewById(R.id.joinBtn)

        if (userId.isNotEmpty() && meeting != null) {
            // Check if the meeting has reached the maximum number of members
            if (meeting!!.memberKeys.size >= meeting!!.maxMemberCount) {
                Toast.makeText(this, "모임의 인원이 최대치입니다!", Toast.LENGTH_SHORT).show()
            } else {
                // Add user ID to memberKeys
                val memberKeys = meeting!!.memberKeys
                memberKeys.add(userId)

                // Update the database
                val db = FirebaseDatabase.getInstance()
                val meetingsRef = db.getReference("meetings")
                meetingsRef.child(meetingKey).child("memberKeys").setValue(memberKeys)
                    .addOnSuccessListener {
                        Toast.makeText(this, "모임 참여에 성공하였습니다!", Toast.LENGTH_SHORT).show()

                        // Update the number of members on the screen
                        val showPeoplenum: TextView = findViewById(R.id.showPeoplenum)
                        showPeoplenum.text = "인원 수: ${meeting?.memberKeys?.size} / ${meeting?.maxMemberCount}"

                        // Disable the join button after successfully joining the meeting
                        joinBtn.isEnabled = false
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "모임 참여에 실패하였습니다!", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> { // 뒤로가기 버튼이 눌렸을 때
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
