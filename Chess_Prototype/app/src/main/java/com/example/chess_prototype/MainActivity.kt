package com.example.chess_prototype

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer

const val TAG = "MainActivity"


    class MainActivity : AppCompatActivity(), ChessDelegate {

        private val chessModel = ChessModel()
        private lateinit var chessView: ChessView
        private lateinit var whiteTimerTextView: TextView
        private lateinit var blackTimerTextView: TextView

        private lateinit var whiteTimer: CountDownTimer
        private lateinit var blackTimer: CountDownTimer

        private var whiteTimeRemaining = 10 * 60 * 1000L // 10 minutes in milliseconds
        private var blackTimeRemaining = 10 * 60 * 1000L // 10 minutes in milliseconds

        private var isWhiteTimerRunning = false
        private var isBlackTimerRunning = false

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            chessView = findViewById(R.id.chess_view)
            chessView.chessDelegate = this

            whiteTimerTextView = findViewById(R.id.white_timer)
            blackTimerTextView = findViewById(R.id.black_timer)

            setupTimers()

            findViewById<Button>(R.id.reset_button).setOnClickListener {
                chessModel.reset()
                chessView.invalidate()
                resetTimers()
            }

            findViewById<Button>(R.id.white_move_count_button).setOnClickListener {
                chessModel.upgradeRook(ChessPlayer.WHITE)
                chessView.invalidate()
            }

            findViewById<Button>(R.id.black_move_count_button).setOnClickListener {
                chessModel.upgradeRook(ChessPlayer.BLACK)
                chessView.invalidate()
            }

            findViewById<Button>(R.id.white_upgrade_queen_button).setOnClickListener {
                chessModel.upgradeQueen(ChessPlayer.WHITE)
                chessView.invalidate()
            }

            findViewById<Button>(R.id.black_upgrade_queen_button).setOnClickListener {
                chessModel.upgradeQueen(ChessPlayer.BLACK)
                chessView.invalidate()
            }

            findViewById<Button>(R.id.white_upgrade_knight_button).setOnClickListener {
                chessModel.upgradeKnight(ChessPlayer.WHITE)
                chessView.invalidate()
            }

            findViewById<Button>(R.id.black_upgrade_knight_button).setOnClickListener {
                chessModel.upgradeKnight(ChessPlayer.BLACK)
                chessView.invalidate()
            }

            findViewById<Button>(R.id.white_upgrade_bishop_button).setOnClickListener {
                chessModel.upgradeBishop(ChessPlayer.WHITE)
                chessView.invalidate()
            }

            findViewById<Button>(R.id.black_upgrade_bishop_button).setOnClickListener {
                chessModel.upgradeBishop(ChessPlayer.BLACK)
                chessView.invalidate()
            }

            findViewById<Button>(R.id.white_upgrade_pawn_button).setOnClickListener {
                chessModel.upgradePawn(ChessPlayer.WHITE)
                chessView.invalidate()
            }

            findViewById<Button>(R.id.black_upgrade_pawn_button).setOnClickListener {
                chessModel.upgradePawn(ChessPlayer.BLACK)
                chessView.invalidate()
            }

            chessModel.whiteMoveCount.observe(this, Observer { count ->
                findViewById<Button>(R.id.white_move_count_button).text = "R"
                startBlackTimer()
            })

            chessModel.blackMoveCount.observe(this, Observer { count ->
                findViewById<Button>(R.id.black_move_count_button).text = "r"
                startWhiteTimer()
            })

            val whiteMoveCountDisplay = findViewById<Button>(R.id.white_move_count_display)
            val blackMoveCountDisplay = findViewById<Button>(R.id.black_move_count_display)


            chessModel.whiteMoveCount.observe(this, Observer { count ->
                whiteMoveCountDisplay.text = "Alb: $count"
            })

            chessModel.blackMoveCount.observe(this, Observer { count ->
                blackMoveCountDisplay.text = "Negru: $count"
            })
        }

        private fun setupTimers() {
            whiteTimer = object : CountDownTimer(whiteTimeRemaining, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    whiteTimeRemaining = millisUntilFinished
                    updateTimerTextView(whiteTimerTextView, whiteTimeRemaining)
                }

                override fun onFinish() {
                }
            }

            blackTimer = object : CountDownTimer(blackTimeRemaining, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    blackTimeRemaining = millisUntilFinished
                    updateTimerTextView(blackTimerTextView, blackTimeRemaining)
                }

                override fun onFinish() {
                }
            }
        }

        private fun startWhiteTimer() {
            if (isBlackTimerRunning) {
                blackTimer.cancel()
                isBlackTimerRunning = false
            }
            whiteTimer.start()
            isWhiteTimerRunning = true
        }

        private fun startBlackTimer() {
            if (isWhiteTimerRunning) {
                whiteTimer.cancel()
                isWhiteTimerRunning = false
            }
            blackTimer.start()
            isBlackTimerRunning = true
        }

        private fun resetTimers() {
            whiteTimer.cancel()
            blackTimer.cancel()
            whiteTimeRemaining = 10 * 60 * 1000L
            blackTimeRemaining = 10 * 60 * 1000L
            updateTimerTextView(whiteTimerTextView, whiteTimeRemaining)
            updateTimerTextView(blackTimerTextView, blackTimeRemaining)
            isWhiteTimerRunning = false
            isBlackTimerRunning = false
        }

        private fun updateTimerTextView(timerTextView: TextView, millisRemaining: Long) {
            val minutes = millisRemaining / 1000 / 60
            val seconds = millisRemaining / 1000 % 60
            timerTextView.text = String.format("%02d:%02d", minutes, seconds)
        }

        override fun pieceAt(col: Int, row: Int): ChessPiece? {
            return chessModel.pieceAt(col, row)
        }

        override fun movePiece(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int) {
            chessModel.movePiece(fromCol, fromRow, toCol, toRow)
            chessView.invalidate()
        }
    }