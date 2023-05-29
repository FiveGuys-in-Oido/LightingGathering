package com.tuk.lightninggathering

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import com.tuk.lightninggathering.databinding.FragmentUserBinding

class UserFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnEditNickname.setOnClickListener{
            binding.editUpdateProfileNickname.isEnabled = true
        }

        binding.btnUpdateProfileNickname.setOnClickListener{
            val nickname = binding.editUpdateProfileNickname.text.toString()
            // 유저 데이터 베이스에 저장.
        }

        binding.btnChangeAddress.setOnClickListener {
            val intent = Intent(activity, MapActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_MAP)
        }

        binding.btnGetMyitem.setOnClickListener{
            val intent = Intent(activity, ItemPost::class.java)
            intent.putExtra("userId", 0) // 유저의 아이디를 입력
            startActivity(intent)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUEST_CODE_MAP = 100
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_MAP && resultCode == Activity.RESULT_OK) {
            val markerLocation = data?.getParcelableExtra<LatLng>("marker_location")
            if (markerLocation != null) {
                // 받아온 데이터를 회원 정보에 입력을 구현해야함
                Log.i("position","$markerLocation")
            }
        }
    }


}

