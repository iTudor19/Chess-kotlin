package com.example.chess_prototype

interface ChessDelegate {
    fun pieceAt(col: Int, row:Int):ChessPiece?
    fun movePiece(fromCol:Int,fromRow:Int,toCol:Int,toRow:Int)
}