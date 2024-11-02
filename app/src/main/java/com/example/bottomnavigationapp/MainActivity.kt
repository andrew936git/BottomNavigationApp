package com.example.bottomnavigationapp

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bottomnavigationapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startAnimation()
        binding.main.setOnClickListener{
            startMainActivity()
        }

    }
    private fun startAnimation(){
        val animationImage = AnimationUtils.loadAnimation(this, R.anim.fade)
        binding.imageViewStart.startAnimation(animationImage)

        val animationTitle = AnimationUtils.loadAnimation(this, R.anim.slide)
        binding.startTextView.startAnimation(animationTitle)

    }

    private fun startMainActivity(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

}