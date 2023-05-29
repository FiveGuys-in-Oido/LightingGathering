package com.tuk.lightninggathering

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class DetailsInfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 데이터를 받아옵니다.
        val memo = arguments?.getString("memo")
        val date = arguments?.getString("date")
        val placeName = arguments?.getString("placeName")
        val placeAddress = arguments?.getString("placeAddress")
        val latitude = arguments?.getDouble("latitude")
        val longitude = arguments?.getDouble("longitude")

        // 데이터를 화면에 표시하는 로직을 여기에 작성하세요.
    }
}
