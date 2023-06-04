package com.tuk.lightninggathering

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.tuk.lightninggathering.R.layout.activity_splash)

        val animationView = findViewById<LottieAnimationView>(R.id.one_btn).apply {
            speed = 1f
            setAnimation("65362-lightning.json") // replace this with your animation file
            enableMergePathsForKitKatAndAbove(true)
            visibility = View.VISIBLE
            repeatCount = LottieDrawable.INFINITE
            addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) { visibility = View.GONE }
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            playAnimation()
        }

        Handler().postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 500)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}
