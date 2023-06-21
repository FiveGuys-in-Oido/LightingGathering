package com.tuk.lightninggathering

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tuk.lightninggathering.databinding.FragmentUserBinding

class UserFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference  // Add this line
    private lateinit var nicknameListener: ValueEventListener
    private lateinit var nameListener: ValueEventListener

    // 로그아웃 구현을 위한 변수
    var auth: FirebaseAuth? = null
    var googleSignInClient: GoogleSignInClient? = null
    val userId = auth?.currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val view = binding.root
        // Firebase 실시간 데이터베이스 초기화
        database = FirebaseDatabase.getInstance().reference

        if (userId != null) {
            nicknameListener = database.child("users").child(userId).child("nickname")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val nickname = snapshot.getValue(String::class.java)
                            binding.editUpdateProfileNickname.hint = nickname
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.w("UserFragment", "loadNickname:onCancelled", error.toException())
                    }
                })
        }

        if (userId != null) {
            nameListener = database.child("users").child(userId).child("name")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val name = snapshot.getValue(String::class.java)
                            binding.tvMypageNickname.text = name
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.w("UserFragment", "loadNickname:onCancelled", error.toException())
                    }
                })
        }


        binding.btnUpdateProfileNickname.setOnClickListener {
            val nickname = binding.editUpdateProfileNickname.text.toString()
            saveUserNickname(nickname)
            // 유저 데이터 베이스에 저장.
        }

        binding.btnChangeAddress.setOnClickListener {
            val intent = Intent(activity, MapActivity::class.java)
            startActivityForResult(intent, 0)
        }

        binding.btnGetMyitem.setOnClickListener {
            val intent = Intent(activity, ItemPost::class.java)
            intent.putExtra("userId", userId) // 유저의 아이디를 입력
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

        // 구글 로그아웃 버튼 클릭 시 이벤트
        binding.googleSignOutButton.setOnClickListener {
            auth!!.signOut()
            googleSignInClient?.signOut()

            var logoutIntent = Intent(requireActivity(), LoginActivity::class.java)
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

        if (resultCode == Activity.RESULT_OK) {
            val address = data?.getStringExtra("marker_address")
            if (address != null) {
                // 받아온 데이터를 회원 정보에 입력을 구현해야함
                saveUserAddress(address)
                Log.i("marker_address", "$address")
            }
        }
    }

    private fun saveUserNickname(nickname: String) {
        if (userId != null) {
            database.child("users").child(userId).child("nickname").setValue(nickname)
        }
    }

    private fun saveUserAddress(address: String) {
        if (userId != null) {
            database.child("users").child(userId).child("address").setValue(address)
        }
    }

}
