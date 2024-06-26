package com.example.chess_prototype

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import androidx.lifecycle.Observer

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), ChessDelegate {

    private val chessModel = ChessModel()
    private lateinit var chessView: ChessView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chessView = findViewById(R.id.chess_view)
        chessView.chessDelegate = this

        findViewById<Button>(R.id.reset_button).setOnClickListener {
            chessModel.reset()
            chessView.invalidate()
        }

        findViewById<Button>(R.id.white_move_count_button).setOnClickListener {
            chessModel.decrementMoveCount(ChessPlayer.WHITE)
            chessView.invalidate()
        }

        findViewById<Button>(R.id.black_move_count_button).setOnClickListener {
            chessModel.decrementMoveCount(ChessPlayer.BLACK)
            chessView.invalidate()
        }

        chessModel.whiteMoveCount.observe(this, Observer { count ->
            findViewById<Button>(R.id.white_move_count_button).text = "White Moves: $count"
        })

        chessModel.blackMoveCount.observe(this, Observer { count ->
            findViewById<Button>(R.id.black_move_count_button).text = "Black Moves: $count"
        })
    }

    override fun pieceAt(col: Int, row: Int): ChessPiece? {
        return chessModel.pieceAt(col, row)
    }

    override fun movePiece(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int) {
        chessModel.movePiece(fromCol, fromRow, toCol, toRow)
        chessView.invalidate()
    }
}
