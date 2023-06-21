package com.tuk.lightninggathering

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SearchFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var postList: MutableList<Post>
    private var selectedCategory: Button? = null
    private var categoryBtn = arrayOfNulls<Button>(6)
    private val categoryNames = arrayOf("운동", "공부", "외식", "게임", "택시", "배달")
    private val meetingKeysList = ArrayList<String>()
    private var currentCategory: String? = null
    private var currentSearchText: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        init(view)
        recyclerView = view.findViewById(R.id.searchRecyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        postList = ArrayList()
        return view
    }

    private fun onClickCategoryButton(button: Button, categoryName: String) {
        selectedCategory?.setBackgroundResource(R.drawable.btn_corner)
        button.setBackgroundResource(R.drawable.btn_background)
        selectedCategory = button
        currentCategory = categoryName
        loadPostsByCategoryAndTitle()
    }

    private fun init(view: View) {
        for (i in categoryNames.indices) {
            categoryBtn[i] = view.findViewById(getButtonId(i))
            categoryBtn[i]?.setOnClickListener {
                categoryBtn[i]?.let { it1 -> onClickCategoryButton(it1, categoryNames[i]) }
            }
        }

        val searchButton = view.findViewById<ImageView>(R.id.imageView)
        searchButton.setOnClickListener {
            val searchText = view.findViewById<EditText>(R.id.editTextTextPersonName).text.toString()
            currentSearchText = searchText
            loadPostsByCategoryAndTitle()
        }
    }

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

    @SuppressLint("NotifyDataSetChanged")
    private fun loadPostsByCategoryAndTitle() {
        postList.clear()
        meetingKeysList.clear()
        FirebaseApp.initializeApp(requireContext())
        val db = FirebaseDatabase.getInstance()
        val query = db.getReference("meetings")

        if (currentCategory != null && currentSearchText != null) {
            query.orderByChild("category")
                .equalTo(currentCategory)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (meetingSnapshot in dataSnapshot.children) {
                            val title = meetingSnapshot.child("title").getValue(String::class.java)
                            if (title != null && title.contains(currentSearchText!!, ignoreCase = true)) {
                                val date = meetingSnapshot.child("date").getValue(String::class.java)
                                val location = meetingSnapshot.child("location").getValue(String::class.java)
                                val curMemberCount = meetingSnapshot.child("memberKeys")
                                    .getValue(object : GenericTypeIndicator<List<String>>() {})
                                val maxMemberCount =
                                    meetingSnapshot.child("maxMemberCount").getValue(Int::class.java)
                                val meetingKeys = meetingSnapshot.key
                                if (meetingKeys != null) {
                                    meetingKeysList.add(meetingKeys)
                                }
                                val post =
                                    Post(title, date, location, curMemberCount, maxMemberCount)
                                postList.add(post)
                            }
                        }
                        postAdapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle the error
                    }
                })
        } else if (currentCategory != null) {
            query.orderByChild("category")
                .equalTo(currentCategory)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (meetingSnapshot in dataSnapshot.children) {
                            val title = meetingSnapshot.child("title").getValue(String::class.java)
                            val date = meetingSnapshot.child("date").getValue(String::class.java)
                            val location = meetingSnapshot.child("location").getValue(String::class.java)
                            val curMemberCount = meetingSnapshot.child("memberKeys")
                                .getValue(object : GenericTypeIndicator<List<String>>() {})
                            val maxMemberCount =
                                meetingSnapshot.child("maxMemberCount").getValue(Int::class.java)
                            val meetingKeys = meetingSnapshot.key
                            if (meetingKeys != null) {
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
        } else if (currentSearchText != null) {
            query.orderByChild("title")
                .startAt(currentSearchText)
                .endAt(currentSearchText + "\uf8ff")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (meetingSnapshot in dataSnapshot.children) {
                            val title = meetingSnapshot.child("title").getValue(String::class.java)
                            if (title != null && title.contains(currentSearchText!!, ignoreCase = true)) {
                                val date = meetingSnapshot.child("date").getValue(String::class.java)
                                val location = meetingSnapshot.child("location").getValue(String::class.java)
                                val curMemberCount = meetingSnapshot.child("memberKeys")
                                    .getValue(object : GenericTypeIndicator<List<String>>() {})
                                val maxMemberCount =
                                    meetingSnapshot.child("maxMemberCount").getValue(Int::class.java)
                                val meetingKeys = meetingSnapshot.key
                                if (meetingKeys != null) {
                                    meetingKeysList.add(meetingKeys)
                                }
                                val post =
                                    Post(title, date, location, curMemberCount, maxMemberCount)
                                postList.add(post)
                            }
                        }
                        postAdapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle the error
                    }
                })
        }
        postAdapter = PostAdapter(postList, requireContext(), meetingKeysList)
        recyclerView.adapter = postAdapter
        postAdapter.setOnItemClickListener { _, meetingKeys ->
            val intent = Intent(requireContext(), GatheringDetailActivity::class.java)
            intent.putExtra("meetingKeys", meetingKeys)
            startActivity(intent)
        }
    }

    private fun clearLists() {
        postList.clear()
        meetingKeysList.clear()
    }
}
