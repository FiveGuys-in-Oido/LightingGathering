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
import com.google.firebase.database.*

import com.tuk.lightninggathering.databinding.FragmentUserBinding

class UserFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var userId: String

    private lateinit var nicknameListener: ValueEventListener
    private lateinit var nameListener: ValueEventListener

    private var googleSignInClient: GoogleSignInClient? = null

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val view = binding.root

        // Firebase 실시간 데이터베이스 초기화
        database = FirebaseDatabase.getInstance().reference

        userId = auth.currentUser?.uid ?: ""

        setupNicknameListener()
        setupNameListener()

        binding.btnUpdateProfileNickname.setOnClickListener {
            val nickname = binding.editUpdateProfileNickname.text.toString()
            saveUserNickname(nickname)
        }

        binding.btnChangeAddress.setOnClickListener {
            val intent = Intent(activity, MapActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_MAP)
        }

        // 구글 로그아웃을 위해 로그인 세션 가져오기
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.firebase_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        // 구글 로그아웃 버튼 클릭 시 이벤트
        binding.googleSignOutButton.setOnClickListener {
            signOut()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_MAP && resultCode == Activity.RESULT_OK) {
            val address = data?.getStringExtra("marker_address")
            if (address != null) {
                saveUserAddress(address)
                Log.i("marker_address", address)
            }
        }
    }

    private fun setupNicknameListener() {
        nicknameListener = database.child("users").child(userId).child("nickname")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nickname = snapshot.getValue(String::class.java)
                    binding.editUpdateProfileNickname.hint = nickname
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("UserFragment", "loadNickname:onCancelled", error.toException())
                }
            })
    }

    private fun setupNameListener() {
        nameListener = database.child("users").child(userId).child("name")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.getValue(String::class.java)
                    binding.tvMypageNickname.text = name
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("UserFragment", "loadNickname:onCancelled", error.toException())
                }
            })
    }

    private fun saveUserNickname(nickname: String) {
        database.child("users").child(userId).child("nickname").setValue(nickname)
    }

    private fun saveUserAddress(address: String) {
        database.child("users").child(userId).child("address").setValue(address)
    }

    private fun signOut() {
        auth.signOut()
        googleSignInClient?.signOut()

        val logoutIntent = Intent(requireActivity(), LoginActivity::class.java)
        logoutIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(logoutIntent)
    }

    companion object {
        private const val REQUEST_CODE_MAP = 100
    }
}
