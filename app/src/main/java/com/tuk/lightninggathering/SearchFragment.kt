package com.tuk.lightninggathering

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var postList: MutableList<Post>
    private var selectedCategory: Button? = null
    private var categoryBtn = arrayOfNulls<Button>(6)
    private val categoryNames = arrayOf("운동", "공부", "외식", "게임", "택시", "배달")
    val meetingKeysList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        init(view)

        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.searchRecyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 게시물 데이터 생성
        postList = ArrayList()

        return view
    }

    // 카테고리 버튼 클릭 리스너
    private fun onClickCategoryButton(button: Button, categoryName: String) {
        selectedCategory?.setBackgroundResource(R.drawable.btn_corner)
        button.setBackgroundResource(R.drawable.btn_background)
        selectedCategory = button
        // 해당 카테고리의 게시물을 조회
        loadPostsByCategory(categoryName)
    }

    private fun init(view: View) {
        // onCreateView 메서드에서 버튼에 클릭 리스너 설정
        for (i in categoryNames.indices) {
            categoryBtn[i] = view.findViewById(getButtonId(i))
            categoryBtn[i]?.setOnClickListener {
                categoryBtn[i]?.let { it1 -> onClickCategoryButton(it1, categoryNames[i]) }
            }
        }
    }

    // 카테고리 버튼 ID 가져오기
    private fun getButtonId(index: Int): Int {
        return when (index) {
            0 -> R.id.categoryBtn1
            1 -> R.id.categoryBtn2
            2 -> R.id.categoryBtn3
            3 -> R.id.categoryBtn4
            4 -> R.id.categoryBtn5
            5 -> R.id.categoryBtn6
            else -> 0
        }
    }

    // 특정 카테고리의 게시물 조회
    @SuppressLint("NotifyDataSetChanged")
    private fun loadPostsByCategory(category: String) {
        postList.clear()



        FirebaseApp.initializeApp(requireContext())
        val db = FirebaseDatabase.getInstance()
        val query = db.getReference("meetings")
            .orderByChild("category")
            .equalTo(category)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (meetingSnapshot in dataSnapshot.children) {
                    val title = meetingSnapshot.child("title").getValue(String::class.java)
                    val date = meetingSnapshot.child("date").getValue(String::class.java)
                    val location = meetingSnapshot.child("location").getValue(String::class.java)
                    val curMemberCount = meetingSnapshot.child("memberKeys").getValue(object : GenericTypeIndicator<List<String>>() {})
                    val maxMemberCount = meetingSnapshot.child("maxMemberCount").getValue(Int::class.java)
                    val meetingKeys = meetingSnapshot.key
                    if (meetingKeys != null){
                        meetingKeysList.add(meetingKeys)
                    }

                    val post = Post(title, date, location, curMemberCount, maxMemberCount)
                    postList.add(post)
                }
                postAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle the error
            }
        })

        // 어댑터 초기화
        postAdapter = PostAdapter(postList, requireContext(), meetingKeysList)
        recyclerView.adapter = postAdapter

        // 아이템 클릭 리스너 설정
        postAdapter.setOnItemClickListener { _, meetingKeys ->
            val intent = Intent(requireContext(), GatheringDetailActivity::class.java)
            intent.putExtra("meetingKeys", meetingKeys)
            startActivity(intent)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
