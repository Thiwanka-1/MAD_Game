package com.example.madgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val start = findViewById<Button>(R.id.btn_start_game)
        val exit = findViewById<Button>(R.id.btn_exit)
        val score = findViewById<Button>(R.id.btn_high_scores)

        start.setOnClickListener {
            val intent = Intent(this@MainMenu,Game::class.java)
            startActivity(intent)
        }

        exit.setOnClickListener {
            val intent = Intent(this@MainMenu,MainActivity::class.java)
            startActivity(intent)
        }

        score.setOnClickListener {
            val intent = Intent(this@MainMenu,HighScore::class.java)
            startActivity(intent)
        }
    }
}