package com.example.chess_prototype

interface ChessDelegate {
    fun pieceAt(col: Int, row:Int):ChessPiece?
}