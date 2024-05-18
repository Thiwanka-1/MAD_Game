package com.example.madgame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HighScore : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_score)

        val exitbtn = findViewById<Button>(R.id.button_ex)

        exitbtn.setOnClickListener {
            val intent = Intent(this@HighScore,MainMenu::class.java)
            startActivity(intent)
        }

        val highScoresText = findViewById<TextView>(R.id.high_scores_text)

        // Retrieve and display high scores
        val highScores = getHighScores()
        highScoresText.text = if (highScores.isEmpty()) {
            "No high scores yet."
        } else {
            highScores.joinToString("\n") { score -> "Score: $score" }
        }
    }

    private fun getHighScores(): List<Int> {
        val sharedPrefs = getSharedPreferences("HighScores", MODE_PRIVATE)
        val highScores = mutableListOf<Int>()
        for (i in 1..5) {
            highScores.add(sharedPrefs.getInt("score_$i", 0))
        }
        return highScores.filter { it > 0 }.sortedDescending()
    }
}

