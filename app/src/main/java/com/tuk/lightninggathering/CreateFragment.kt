package com.tuk.lightninggathering

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.travijuu.numberpicker.library.NumberPicker
import java.text.SimpleDateFormat
import java.util.*

class CreateFragment : Fragment() {

    // 모든 필요한 뷰들을 선언
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
    var categoryBtn = arrayOfNulls<Button>(9)
    var cList = ArrayList<String>()
    var selectedCategory: Button? = null
    var date: Date? = null
    var category: String? = null
    var latitude: Double? = null
    var longitude: Double? = null
    var location: String? = null

    // Fragment에 대한 View를 만드는 과정
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create, container, false)

        // 뷰 초기화
        init(view)

        // 모임 생성 버튼 클릭 리스너
        createBtn.setOnClickListener { view: View ->
            // 입력 정보 가져오기
            val title = titleEt.text.toString()
            val peoplenum = numberPicker.value
            val memo = memoEt.text.toString()

            // MeetPost 객체 생성 및 정보 설정
            val meetings = Post(title, category, date.toString(),
                location, "key", latitude, longitude,
                memo, peoplenum, listOf("key"))

            saveMeetings(meetings)
        }

        // 지도 액티비티 시작과 결과 수신
        val startMapActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val latLng = data?.getParcelableExtra<LatLng>("marker_location")
                val address = data?.getStringExtra("marker_address")
                if (latLng != null) {
                    // 위도와 경도 설정
                    latitude = latLng.latitude
                    longitude = latLng.longitude
                    location = address
                    selectedLocationTv.text = location
                }
            }
        }

        // 위치 선택 버튼 클릭 리스너
        locationBtn.setOnClickListener { v: View ->
            val intent = Intent(activity, MapActivity::class.java)
            startMapActivity.launch(intent)
        }

        return view
    }
    private fun saveMeetings(meetings: Post) {
        FirebaseApp.initializeApp(requireContext())

        val db = FirebaseDatabase.getInstance()
        val meetingsRef = db.getReference("meetings")
        val newMeetingRef = meetingsRef.push()

        newMeetingRef.setValue(meetings)
    }

    // 카테고리 버튼 클릭 리스너
    private fun onClickCategoryButton(button: Button) {
        if (selectedCategory == button) {
            // 같은 버튼 클릭시 선택 해제
            selectedCategory?.setBackgroundResource(R.drawable.btn_corner)
            selectedCategory = null
            category = null
        } else {
            // 다른 버튼 클릭시 선택 변경
            selectedCategory?.setBackgroundResource(R.drawable.btn_corner)
            button.setBackgroundResource(R.drawable.btn_background)
            selectedCategory = button
            category = button.text.toString()
        }
    }

    // 뷰 초기화 함수
    private fun init(view: View) {
        toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)
        titleEt = view.findViewById(R.id.titleEt)
        calendarBtn = view.findViewById(R.id.calendarBtn)
        selectedDateTv = view.findViewById(R.id.selectedDateTv)
        timeBtn = view.findViewById(R.id.timeBtn)
        selectedTimeTv = view.findViewById(R.id.selectedTimeTv)
        numberPicker = view.findViewById(R.id.numberPicker)
        memoEt = view.findViewById(R.id.memoEt)
        locationBtn = view.findViewById(R.id.locationBtn)
        selectedLocationTv = view.findViewById(R.id.selectedLocationTv)
        createBtn = view.findViewById(R.id.createBtn)

        // onCreateView 메서드에서 버튼에 클릭 리스너 설정
        for (i in 0..8) {
            categoryBtn[i] = view.findViewById(getButtonId(i))
            categoryBtn[i]?.setOnClickListener {
                categoryBtn[i]?.let { it1 -> onClickCategoryButton(it1) }
            }
        }

        val calendar = Calendar.getInstance()
        date = calendar.time
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH시 mm분", Locale.getDefault())
        selectedDateTv.text = dateFormat.format(date)
        selectedTimeTv.text = timeFormat.format(date)

        // 캘린더 버튼 클릭 리스너
        calendarBtn.setOnClickListener { v: View ->
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year: Int, month: Int, dayOfMonth: Int ->
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

        // 시간 버튼 클릭 리스너
        timeBtn.setOnClickListener { v: View ->
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, hourOfDay: Int, minute: Int ->
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

        // 메모에 글자 수 제한하는 리스너
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

    // 카테고리 버튼 ID 가져오기
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
