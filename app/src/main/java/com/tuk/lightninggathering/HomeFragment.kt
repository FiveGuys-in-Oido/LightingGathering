package com.tuk.lightninggathering

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var postList: MutableList<Post>
    val meetingKeysList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // RecyclerView 초기화 및 어댑터 설정
        recyclerView = view.findViewById(R.id.showRecyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 게시물 데이터 생성
        postList = ArrayList()

        FirebaseApp.initializeApp(requireContext())
        val db = FirebaseDatabase.getInstance()
        val query = db.getReference("meetings")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                postList.clear() // 기존 게시물 리스트 초기화
                meetingKeysList.clear()

                for (meetingSnapshot in dataSnapshot.children.reversed()) {
                    val title = meetingSnapshot.child("title").getValue(String::class.java)
                    val date = meetingSnapshot.child("date").getValue(String::class.java)
                    val location = meetingSnapshot.child("location").getValue(String::class.java)
                    val curMemberCount = meetingSnapshot.child("memberKeys")
                        .getValue(object : GenericTypeIndicator<List<String>>() {})
                    val maxMemberCount = meetingSnapshot.child("maxMemberCount").getValue(Int::class.java)
                    val meetingKeys = meetingSnapshot.key
                    if (meetingKeys != null) {
                        meetingKeysList.add(meetingKeys)
                    }

                    val post = Post(title, date, location, curMemberCount,maxMemberCount)
                    postList.add(post)
                }
                postAdapter.notifyDataSetChanged() // 어댑터에 데이터 변경을 알림
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error
            }
        })

        // 어댑터 설정
        postAdapter = PostAdapter(postList, requireContext(), meetingKeysList)
        recyclerView.adapter = postAdapter

        postAdapter.setOnItemClickListener { _, meetingKeys ->
            val intent = Intent(requireContext(), GatheringDetailActivity::class.java)
            intent.putExtra("meetingKeys", meetingKeys)
            startActivity(intent)
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}