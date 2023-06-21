package com.tuk.lightninggathering

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.tuk.lightninggathering.databinding.FragmentUserBinding

class UserFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    // 로그아웃 구현을 위한 변수
    var auth : FirebaseAuth ?= null
    var googleSignInClient : GoogleSignInClient ?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.tvMypageNickname.text = "userName"

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

        // 구글 로그아웃을 위해 로그인 세션 가져오기
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.firebase_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        // firebaseauth를 사용하기 위한 인스턴스 get
        auth = FirebaseAuth.getInstance()

        // 구글 로그아웃 버튼 ID 가져오기
        var google_sign_out_button = view.findViewById<Button>(R.id.google_sign_out_button)

        // 구글 로그아웃 버튼 클릭 시 이벤트
        google_sign_out_button.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            googleSignInClient?.signOut()

            var logoutIntent = Intent (requireActivity(), LoginActivity::class.java)
            logoutIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(logoutIntent)
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
