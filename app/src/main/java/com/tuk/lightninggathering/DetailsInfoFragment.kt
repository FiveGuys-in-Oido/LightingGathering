package com.tuk.lightninggathering

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class DetailsInfoFragment : Fragment(), OnMapReadyCallback {

    private var mapView: MapView? = null
    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val date = arguments?.getString("date")
        val location = arguments?.getString("location")
        val description = arguments?.getString("description")
        val category = arguments?.getString("category")

        val showDate = view.findViewById<TextView>(R.id.showDate)
        val showLocation = view.findViewById<TextView>(R.id.showLocation)
        val showMemo = view.findViewById<TextView>(R.id.showMemo)
        val showCategory = view.findViewById<TextView>(R.id.showCategory)

        showDate.text = date
        showLocation.text = location
        showMemo.text = description
        showCategory.text = category

        mapView = view.findViewById(R.id.map)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val latitude = arguments?.getDouble("latitude")
        val longitude = arguments?.getDouble("longitude")

        val location = LatLng(latitude!!, longitude!!)
        googleMap?.addMarker(MarkerOptions().position(location))
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
    }
}
