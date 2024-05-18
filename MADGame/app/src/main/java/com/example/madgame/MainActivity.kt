package com.example.madgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startbtn = findViewById<Button>(R.id.button)

        startbtn.setOnClickListener {
            val intent = Intent(this@MainActivity,MainMenu::class.java)
            startActivity(intent)
        }

    }
}