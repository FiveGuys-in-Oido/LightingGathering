package com.tuk.lightninggathering

//import com.flashmob_team.usr.flashmob_project.Application.ApplicationController
//import com.flashmob_team.usr.flashmob_project.Network.NetworkService
//import com.flashmob_team.usr.flashmob_project.R

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.maps.model.LatLng
import com.travijuu.numberpicker.library.NumberPicker
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
    var selectedCategory: Button? = null

    // 날짜, 장소 관련 변수 선언
    var date: Date? = null
    var latitude: Double? = null
    var longitude: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gathering_register)

        // View 초기화
        init()

        // 완료 버튼을 클릭했을 때
        createBtn.setOnClickListener { view: View ->
            Log.d("Log", "create click")
            val title = titleEt.text.toString() // 제목
            val peoplenum = numberPicker.value // 인원
            val memo = memoEt.text.toString() // 메모

            val meetPost = MeetPost()
            meetPost.meet_title = title   // 제목
            meetPost.meet_date = date.toString()    // 날짜
            meetPost.meet_people_num = peoplenum    // 인원 수
            meetPost.meet_memo = memo   // 메모
            meetPost.meet_place_latitude = latitude    // 위도
            meetPost.meet_place_longitude = longitude  // 경도
            meetPost.leader_id = 1
            meetPost.cate_id = 1
            meetPost.loca_id = 1
            Log.d("title", meetPost.meet_title)
            Log.d("date", meetPost.meet_date)
            Log.d("people_num", meetPost.meet_people_num.toString())
            Log.d("memo", meetPost.meet_memo)
            Log.d("latitude", meetPost.meet_place_latitude.toString())
            Log.d("longitude", meetPost.meet_place_longitude.toString())
        }

        // MapActivity를 시작하고 결과를 받아오기 위한 contract를 정의합니다.
        val startMapActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val latLng = data?.getParcelableExtra<LatLng>("marker_location")
                if (latLng != null) {
                    latitude = latLng.latitude
                    longitude = latLng.longitude
                }
            }
        }

        locationBtn.setOnClickListener { v: View ->
            // contract를 사용하여 MapActivity를 시작합니다.
            val intent = Intent(this@GatheringRegisterActivity, MapActivity::class.java)
            startMapActivity.launch(intent)
        }
    }

    // 카테고리 선택된 버튼 체크
    fun onClick(v: View) {
        val newButton = v as Button
        if (selectedCategory == newButton) {
            // 같은 버튼을 눌렀을 때는 선택을 취소합니다.
            selectedCategory?.setBackgroundResource(R.drawable.btn_corner)
            cList.remove(selectedCategory?.text.toString())
            selectedCategory = null
        } else {
            // 다른 버튼을 눌렀을 때는 기존 선택을 취소하고 새로운 카테고리를 선택합니다.
            selectedCategory?.setBackgroundResource(R.drawable.btn_corner)
            cList.remove(selectedCategory?.text.toString())
            newButton.setBackgroundResource(R.drawable.btn_background)
            cList.add(newButton.text.toString())
            selectedCategory = newButton
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

