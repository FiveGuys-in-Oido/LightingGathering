package com.tuk.lightninggathering

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tuk.lightninggathering.databinding.ActivityMapBinding
import java.util.Locale

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var editSearch: EditText
    private lateinit var fabCurrentLocation: FloatingActionButton
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 초기화
        val binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewBinding을 통해 필요한 뷰 참조
        editSearch = binding.editSearchMap
        fabCurrentLocation = binding.fabCurrentLocation

        // FusedLocationProviderClient 초기화
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // SupportMapFragment를 가져와서 지도가 준비되면 onMapReady 콜백이 호출되도록 설정
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // FloatingActionButton의 클릭 이벤트 처리
        fabCurrentLocation.setOnClickListener {
            // 현재 위치를 가져와서 지도 카메라 이동
            getCurrentLocation()
        }

        // EditText에서 엔터 키 누를 때 검색 이벤트 처리
        editSearch.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                searchLocation()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        // 버튼 클릭 이벤트 처리
        val changeLocationButton = findViewById<Button>(R.id.changelocationbtn)
        changeLocationButton.setOnClickListener {
            // 마커의 위치 정보를 가져와서 이전 컴포넌트에 전달
            val markerLocation = googleMap.cameraPosition.target
            val intent = Intent()
            intent.putExtra("marker_location", markerLocation)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 위치 권한이 없을 경우 권한 요청
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // 위치 권한이 있을 경우 현재 위치 가져오기
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        val currentLocation = LatLng(location.latitude, location.longitude)
                        googleMap.clear() // 기존 마커 제거
                        googleMap.addMarker(
                            MarkerOptions().position(currentLocation).title("Current Location")
                        )
                        googleMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(currentLocation, 15f)
                        )
                    }
                }
        }
    }

    private fun searchLocation() {
        val locationName = editSearch.text.toString()
        if (locationName.isNotEmpty()) {
            val geocoder = Geocoder(this, Locale.KOREA)
            val addressList = geocoder.getFromLocationName(locationName, 1)
            if (addressList != null) {
                if (addressList.isNotEmpty()) {
                    val address = addressList[0]
                    val searchedLocation = LatLng(address.latitude, address.longitude)
                    googleMap.clear() // 기존 마커 제거
                    googleMap.addMarker(
                        MarkerOptions().position(searchedLocation).title(locationName)
                    )
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(searchedLocation, 15f))
                }
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

}
