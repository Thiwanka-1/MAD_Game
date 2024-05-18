package com.example.madgame

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

class Game : AppCompatActivity() {
    private lateinit var character: ImageView
    private lateinit var obstacle: ImageView
    private lateinit var scoreText: TextView
    private lateinit var restartButton: Button
    private lateinit var exitButton: Button

    private val handler = Handler(Looper.getMainLooper())
    private var gameRunning = true
    private var obstacleSpeed = 30f
    private var isJumping = false
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        character = findViewById(R.id.character)
        obstacle = findViewById(R.id.obstacle)
        scoreText = findViewById(R.id.score_text)
        restartButton = findViewById(R.id.restart_button)
        exitButton = findViewById(R.id.exit_button)

        resetObstacle() // Set initial position for the obstacle
        startGameLoop()

        findViewById<RelativeLayout>(R.id.game_container).setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN && !isJumping && gameRunning) {
                initiateJump() // Only jump if not already jumping
            }
            true
        }

        restartButton.setOnClickListener {
            restartGame() // Restart the game
        }

        exitButton.setOnClickListener {
            finish() // Exit the game
        }
    }

    private fun startGameLoop() {
        if (gameRunning) {
            handler.postDelayed({
                updateGame()
                if (gameRunning) {
                    startGameLoop()
                }
            }, 50) // Game loop runs every 50ms
        }
    }

    private fun updateGame() {
        if (gameRunning) {
            obstacle.translationX -= obstacleSpeed // Move the obstacle

            // Reset if the obstacle goes off-screen
            if (obstacle.translationX < -5 * obstacle.width) {
                resetObstacle()
                incrementScore() // Increment the score when the obstacle resets
            }

            // Check for collision
            if (checkCollision(character, obstacle)) {
                gameOver() // End the game if there's a collision
            }
        }
    }

    private fun incrementScore() {
        score += 1 // Increase the score
        scoreText.text = "Score: $score" // Update the display
    }

    private fun initiateJump() {
        if (isJumping) {
            return // Prevent double jump
        }

        isJumping = true
        val jumpHeight = 500f
        val jumpDuration = 1200

        character.animate()
            .translationYBy(-jumpHeight) // Jump upward
            .setDuration(jumpDuration.toLong()) // Jump duration
            .withEndAction {
                character.animate()
                    .translationYBy(jumpHeight) // Return to original position
                    .setDuration(jumpDuration.toLong()) // Landing duration
                    .withEndAction {
                        isJumping = false // Reset when back on the ground
                    }
                    .start()
            }
            .start()
    }

    private fun resetObstacle() {
        val parentWidth = findViewById<RelativeLayout>(R.id.game_container).width
        obstacle.translationX = parentWidth.toFloat() + obstacle.width // Start off-screen
        obstacle.translationY = 0f // Align with the ground
    }

    private fun checkCollision(char: ImageView, obs: ImageView): Boolean {
        val charRect = android.graphics.Rect()
        char.getHitRect(charRect)

        val obsRect = android.graphics.Rect()
        obs.getHitRect(obsRect)

        return charRect.intersects(obsRect.left, obsRect.top, obsRect.right, obsRect.bottom)
    }

    private fun saveHighScore() {
        val sharedPrefs = getSharedPreferences("HighScores", MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        val scores = mutableListOf<Int>()
        for (i in 1..5) {
            scores.add(sharedPrefs.getInt("score_$i", 0)) // Get existing high scores
        }
        scores.add(score) // Add the current score
        val sortedScores = scores.sortedDescending().take(5) // Keep only top 5 scores
        for ((index, s) in sortedScores.withIndex()) {
            editor.putInt("score_${index + 1}", s) // Save the top 5 scores
        }
        editor.apply() // Apply the changes to SharedPreferences
    }

    private fun gameOver() {
        gameRunning = false
        handler.removeCallbacksAndMessages(null) // Stop the game loop
        saveHighScore() // Save the high score
        restartButton.visibility = Button.VISIBLE
        exitButton.visibility = Button.VISIBLE
        Toast.makeText(this, "Game Over. Score: $score", Toast.LENGTH_SHORT).show() // Notify the player
    }


    private fun restartGame() {
        score = 0
        scoreText.text = "Score: 0" // Reset the display
        gameRunning = true
        restartButton.visibility = Button.GONE
        exitButton.visibility = Button.GONE
        resetObstacle()
        startGameLoop()
    }
}
