package com.tuk.lightninggathering

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

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
        val category = arguments?.getString("category")

        // 필요한 View 컴포넌트를 참조합니다.
        val memoView: TextView = view.findViewById(R.id.showMemo)
        val dateView: TextView = view.findViewById(R.id.showDate)
        val locationView: TextView = view.findViewById(R.id.location)
        val mapView: com.google.android.gms.maps.MapView = view.findViewById(R.id.map)
        val categoryView: TextView = view.findViewById(R.id.Category)

        // 받아온 데이터를 각 View에 설정합니다.
        memoView.text = memo
        dateView.text = date
        locationView.text = placeName ?: placeAddress
        categoryView.text = category;

        // Google Map을 설정합니다.
        mapView.onCreate(savedInstanceState)
        mapView.onResume() // 앱이 백그라운드에서 복구될 때 지도가 즉시 로드되도록 합니다.

        mapView.getMapAsync { googleMap ->
            val placeLocation = LatLng(latitude ?: 0.0, longitude ?: 0.0)
            googleMap.addMarker(MarkerOptions().position(placeLocation).title(placeName))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation, 16f))
        }
    }
}
