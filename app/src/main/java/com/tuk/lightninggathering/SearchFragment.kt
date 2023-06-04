package com.tuk.lightninggathering

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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
    var selectedCategory: Button? = null
    var categoryBtn = arrayOfNulls<Button>(9)

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

        // 어댑터 설정
        //postAdapter = PostAdapter(postList, requireContext())
        recyclerView.adapter = postAdapter

        return view
    }

    // 카테고리 버튼 클릭 리스너
    private fun onClickCategoryButton(button: Button) {
        if (selectedCategory == button) {
            // 같은 버튼 클릭시 선택 해제
            selectedCategory?.setBackgroundResource(R.drawable.btn_corner)
            selectedCategory = null
        } else {
            // 다른 버튼 클릭시 선택 변경
            selectedCategory?.setBackgroundResource(R.drawable.btn_corner)
            button.setBackgroundResource(R.drawable.btn_background)
            selectedCategory = button
        }
    }
    private fun init(view: View) {
        // onCreateView 메서드에서 버튼에 클릭 리스너 설정
        for (i in 0..8) {
            categoryBtn[i] = view.findViewById(getButtonId(i))
            categoryBtn[i]?.setOnClickListener {
                categoryBtn[i]?.let { it1 -> onClickCategoryButton(it1) }
            }
        }
    }
    // onCreateView 메서드에서 버튼에 클릭 리스너 설정


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