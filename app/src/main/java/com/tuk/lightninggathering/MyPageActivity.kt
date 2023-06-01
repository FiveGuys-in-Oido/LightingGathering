package com.tuk.lightninggathering

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tuk.lightninggathering.databinding.ActivityMyPageBinding

// session 에서 얻은 유저 id를 통해 database 에서 유저 정보를 받아옴
class MyPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEditNickname.setOnClickListener {
            binding.editUpdateProfileNickname.isEnabled = true
        }
    }
}