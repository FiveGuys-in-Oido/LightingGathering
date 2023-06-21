package com.tuk.lightninggathering

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tuk.lightninggathering.BuildConfig.MAPS_API_KEY
import com.tuk.lightninggathering.databinding.ActivityMapBinding
import java.util.Locale

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var editSearch: EditText
    private lateinit var fabCurrentLocation: FloatingActionButton
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Places.initialize(applicationContext, MAPS_API_KEY)

        // ViewBinding 초기화
        val binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewBinding을 통해 필요한 뷰 참조
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

        // AutocompleteSupportFragment 초기화
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        // 반환할 장소 데이터 유형 지정
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))

        // PlaceSelectionListener 설정하여 응답 처리
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // 선택된 장소에 대한 정보 가져오기
                Log.i("TAG", "Place: ${place.name}, ${place.id}")
                // 검색된 장소의 위치로 지도 이동
                val searchedLocation = place.latLng
                searchedLocation?.let {
                    googleMap.clear() // 기존 마커 제거
                    val markerOptions = MarkerOptions().position(searchedLocation).title(place.name)
                    googleMap.addMarker(markerOptions)
                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(searchedLocation, 15f)
                    googleMap.moveCamera(cameraUpdate)
                }
            }

            override fun onError(status: Status) {
                // 에러 처리
                Log.i("TAG", "An error occurred: $status")
            }
        })

        // 버튼 클릭 이벤트 처리
        val changeLocationButton = findViewById<Button>(R.id.changelocationbtn)
        changeLocationButton.setOnClickListener {
            // 마커의 위치 정보를 가져와서 이전 컴포넌트에 전달
            val markerLocation = googleMap.cameraPosition.target
            val geocoder = Geocoder(this, Locale.getDefault()) // Geocoder 인스턴스 초기화
            val addresses: List<Address>? = geocoder.getFromLocation(markerLocation.latitude, markerLocation.longitude, 1)
            val address = addresses?.get(0)?.getAddressLine(0)
            val intent = Intent()
            intent.putExtra("marker_location", markerLocation)
            intent.putExtra("marker_address", address)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        // 지도가 준비되면 여기서 추가 초기화 및 설정을 수행할 수 있습니다.
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

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}
