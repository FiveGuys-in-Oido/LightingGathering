package com.tuk.lightninggathering

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var postList: MutableList<Post>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search)

        // RecyclerView 초기화
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(LinearLayoutManager(this))

        // 게시물 데이터 생성
        postList = ArrayList()
        postList.add(Post("게시물 제목 1", "2023-05-30", "장소 1", "참여인원/현재인원 1"))
        postList.add(Post("게시물 제목 2", "2023-05-31", "장소 2", "참여인원/현재인원 2"))
        postList.add(Post("게시물 제목 3", "2023-06-01", "장소 3", "참여인원/현재인원 3"))
        postList.add(Post("게시물 제목 4", "2023-06-02", "장소 4", "참여인원/현재인원 4"))

        // 어댑터 설정
        postAdapter = PostAdapter(postList, this)
        recyclerView.adapter = postAdapter
    }
}