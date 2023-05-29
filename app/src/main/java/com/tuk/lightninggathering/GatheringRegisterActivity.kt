package com.tuk.lightninggathering

//import com.flashmob_team.usr.flashmob_project.Application.ApplicationController
//import com.flashmob_team.usr.flashmob_project.Network.NetworkService
//import com.flashmob_team.usr.flashmob_project.R

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.travijuu.numberpicker.library.NumberPicker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class GatheringRegisterActivity : AppCompatActivity() {

    // View 변수 선언
    lateinit var toolbar: Toolbar
    lateinit var titleEt: EditText
    lateinit var calendarBtn: ImageButton
    lateinit var selectedDateTv: TextView
    lateinit var timeBtn: ImageButton
    lateinit var selectedTimeTv: TextView
    lateinit var numberPicker: NumberPicker
    lateinit var memoEt: EditText
    lateinit var locationBtn: ImageButton
    lateinit var selectedLocationTv: TextView
    lateinit var createBtn: Button

    // 카테고리 버튼 배열과 카테고리 선택 상태 변수 선언
    var categoryBtn = arrayOfNulls<Button>(9)
    var cList = ArrayList<String>()
    var categoryCheck = false

    // 날짜, 장소 관련 변수 선언
    var date: Date? = null
    var placeName: CharSequence? = null
    var placeAddress: CharSequence? = null
    var placeLatLng: LatLng? = null

    // 장소 및 네트워크 서비스 변수 선언
    var PLACE_REQUEST_CODE = 1
    var AUTOCOMPLETE_REQUEST_CODE = 2
//    lateinit var service: NetworkService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gathering_register)

        // Places API 초기화
        Places.initialize(applicationContext, "YOUR_API_KEY")

        // View 초기화
        init()

        // 완료 버튼을 클릭했을 때
        createBtn.setOnClickListener { view: View ->
            Log.d("Log", "create click")
            val title = titleEt.text.toString() // 제목
            val place = selectedLocationTv.text.toString() // 장소
            val peoplenum = numberPicker.value // 인원
            val memo = memoEt.text.toString() // 메모

            val meetPost = MeetPost()
            meetPost.meet_title = title
            meetPost.meet_date = date.toString()
            meetPost.meet_people_num = peoplenum
            meetPost.meet_memo = memo
            meetPost.meet_place_address = placeAddress.toString()
            meetPost.meet_place_latitude = placeLatLng?.latitude
            meetPost.meet_place_longitude = placeLatLng?.longitude
            meetPost.meet_place_name = placeName.toString()
            meetPost.leader_id = 1
            meetPost.cate_id = 1
            meetPost.loca_id = 1

//            val meetPostResultCall = service.getMeetPostResult(meetPost)
//            meetPostResultCall.enqueue(object : Callback<MeetPostResult> {
//
//                override fun onResponse(call: Call<MeetPostResult>, response: Response<MeetPostResult>) {
//                    if (response.isSuccessful) {
//                        when (response.body()?.message) {
//                            "Sucessful Register Meeting" -> {
//                                Log.d("Log", "와 제발 성공해라")
//                                val intent = Intent(this@GatheringRegisterActivity, InfoActivity::class.java)
//                                intent.putExtra("title", title)
//                                startActivity(intent)
//                                finish()
//                            }
//                            "NULL Value" -> {
//                                Log.d("Log", "null")
//                            }
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<MeetPostResult>, t: Throwable) {
//                    Log.d("Log", "와 실패")
//                }
//            })
        }

        locationBtn.setOnClickListener { v: View ->
            // 장소 선택 버튼 클릭 시 AutocompleteActivity 실행
            val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(this@GatheringRegisterActivity)
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_REQUEST_CODE && resultCode == RESULT_OK) {
            // 장소 선택 후 결과 처리
            val place = Autocomplete.getPlaceFromIntent(data!!)
            placeName = place.name
            placeAddress = place.address
            placeLatLng = place.latLng

            selectedLocationTv.text = placeAddress

            val toastMsg = String.format("Place: %s", place.name)
            Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show()
        }
    }

    fun onClick(v: View) {
        val newButton = v as Button
        for (tempButton in categoryBtn) {
            if (tempButton == newButton) {
                if (!categoryCheck) {
                    tempButton.setBackgroundResource(R.drawable.btn_background)
                    cList.add(tempButton.text.toString())
                    categoryCheck = true
                } else {
                    tempButton.setBackgroundResource(R.drawable.btn_corner)
                    cList.remove(tempButton.text.toString())
                    categoryCheck = false
                }
            }
        }
    }

    // View 초기화 함수
    fun init() {
        titleEt = findViewById(R.id.titleEt)
        calendarBtn = findViewById(R.id.calendarBtn)
        selectedDateTv = findViewById(R.id.selectedDateTv)
        timeBtn = findViewById(R.id.timeBtn)
        selectedTimeTv = findViewById(R.id.selectedTimeTv)
        numberPicker = findViewById(R.id.numberPicker)
        memoEt = findViewById(R.id.memoEt)
        locationBtn = findViewById(R.id.locationBtn)
        selectedLocationTv = findViewById(R.id.selectedLocationTv)
        createBtn = findViewById(R.id.createBtn)

        // 네트워크 서비스 초기화
        //service = ApplicationController.instance.networkService

        // 카테고리 버튼 초기화
        for (i in 0..8) {
            categoryBtn[i] = findViewById(getButtonId(i))
        }

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.setDisplayShowTitleEnabled(false)

        val calendar = Calendar.getInstance()
        date = calendar.time
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH시 mm분", Locale.getDefault())
        selectedDateTv.text = dateFormat.format(date)
        selectedTimeTv.text = timeFormat.format(date)

        calendarBtn.setOnClickListener { v: View ->
            // 날짜 선택 버튼 클릭 시 DatePickerDialog 실행
            val datePickerDialog = DatePickerDialog(
                this@GatheringRegisterActivity,
                { view: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.time = date
                    selectedCalendar.set(year, month, dayOfMonth)
                    date = selectedCalendar.time
                    selectedDateTv.text = dateFormat.format(date)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        timeBtn.setOnClickListener { v: View ->
            // 시간 선택 버튼 클릭 시 TimePickerDialog 실행
            val timePickerDialog = TimePickerDialog(
                this@GatheringRegisterActivity,
                { view: TimePicker, hourOfDay: Int, minute: Int ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.time = date
                    selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedCalendar.set(Calendar.MINUTE, minute)
                    date = selectedCalendar.time
                    selectedTimeTv.text = timeFormat.format(date)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
            timePickerDialog.show()
        }

        memoEt.addTextChangedListener(object : TextWatcher {
            var previousString = ""

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                previousString = s.toString()
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                if (memoEt.lineCount >= 5) {
                    memoEt.setText(previousString)
                    memoEt.setSelection(memoEt.length())
                }
            }
        })
    }

    // 카테고리 버튼 ID 반환 함수
    private fun getButtonId(index: Int): Int {
        return when (index) {
            0 -> R.id.cBtn1
            1 -> R.id.cBtn2
            2 -> R.id.cBtn3
            3 -> R.id.cBtn4
            4 -> R.id.cBtn5
            5 -> R.id.cBtn6
            else -> 0
        }
    }
}

