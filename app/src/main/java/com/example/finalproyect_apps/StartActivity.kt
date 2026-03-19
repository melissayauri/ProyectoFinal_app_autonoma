package com.example.finalproyect_apps

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_start)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        showSplash()
    }

    fun showSplash(){
        object : CountDownTimer(5000,1000){
            override fun onFinish() {
                val intent = Intent(this@StartActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            override fun onTick(millisUntilFinished: Long) {

            }

        }.start()
    }
}