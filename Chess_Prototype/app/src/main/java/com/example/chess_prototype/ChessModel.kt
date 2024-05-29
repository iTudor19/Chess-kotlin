package com.example.chess_prototype

import android.util.Log

class ChessModel {
    var piecesBox = mutableSetOf<ChessPiece>()
    init {
        reset()


    }

    fun movePiece(fromCol:Int,fromRow:Int,toCol:Int,toRow:Int)
    {
        if(fromCol==toCol && fromRow==toRow) return

        val movingPiece=pieceAt(fromCol,fromRow)?:return

        pieceAt(toCol,toRow)?.let{
            if(it.player == movingPiece.player)
            {
                return
            }
            piecesBox.remove(it)}

        piecesBox.remove(movingPiece)
        piecesBox.add(ChessPiece(toCol,toRow,movingPiece.player,movingPiece.rank, movingPiece.resID))

    }


    private fun reset()
    {
        piecesBox.removeAll(piecesBox)

        // Piese albe
        piecesBox.add(ChessPiece(0, 0, ChessPlayer.WHITE, ChessRank.ROOK,R.drawable.white_rook))
        piecesBox.add(ChessPiece(1, 0, ChessPlayer.WHITE, ChessRank.KNIGHT,R.drawable.white_knight))
        piecesBox.add(ChessPiece(2, 0, ChessPlayer.WHITE, ChessRank.BISHOP,R.drawable.white_bishop))
        piecesBox.add(ChessPiece(3, 0, ChessPlayer.WHITE, ChessRank.QUEEN,R.drawable.white_queen))
        piecesBox.add(ChessPiece(4, 0, ChessPlayer.WHITE, ChessRank.KING,R.drawable.white_king))
        piecesBox.add(ChessPiece(5, 0, ChessPlayer.WHITE, ChessRank.BISHOP,R.drawable.white_bishop))
        piecesBox.add(ChessPiece(6, 0, ChessPlayer.WHITE, ChessRank.KNIGHT,R.drawable.white_knight))
        piecesBox.add(ChessPiece(7, 0, ChessPlayer.WHITE, ChessRank.ROOK,R.drawable.white_rook))

        piecesBox.add(ChessPiece(0, 1, ChessPlayer.WHITE, ChessRank.PAWN,R.drawable.white_pawn))
        piecesBox.add(ChessPiece(1, 1, ChessPlayer.WHITE, ChessRank.PAWN,R.drawable.white_pawn))
        piecesBox.add(ChessPiece(2, 1, ChessPlayer.WHITE, ChessRank.PAWN,R.drawable.white_pawn))
        piecesBox.add(ChessPiece(3, 1, ChessPlayer.WHITE, ChessRank.PAWN,R.drawable.white_pawn))
        piecesBox.add(ChessPiece(4, 1, ChessPlayer.WHITE, ChessRank.PAWN,R.drawable.white_pawn))
        piecesBox.add(ChessPiece(5, 1, ChessPlayer.WHITE, ChessRank.PAWN,R.drawable.white_pawn))
        piecesBox.add(ChessPiece(6, 1, ChessPlayer.WHITE, ChessRank.PAWN,R.drawable.white_pawn))
        piecesBox.add(ChessPiece(7, 1, ChessPlayer.WHITE, ChessRank.PAWN,R.drawable.white_pawn))

// Piese negre
        piecesBox.add(ChessPiece(0, 7, ChessPlayer.BLACK, ChessRank.ROOK,R.drawable.black_rook))
        piecesBox.add(ChessPiece(1, 7, ChessPlayer.BLACK, ChessRank.KNIGHT,R.drawable.black_knight))
        piecesBox.add(ChessPiece(2, 7, ChessPlayer.BLACK, ChessRank.BISHOP,R.drawable.black_bishop))
        piecesBox.add(ChessPiece(3, 7, ChessPlayer.BLACK, ChessRank.QUEEN,R.drawable.black_queen))
        piecesBox.add(ChessPiece(4, 7, ChessPlayer.BLACK, ChessRank.KING,R.drawable.black_king))
        piecesBox.add(ChessPiece(5, 7, ChessPlayer.BLACK, ChessRank.BISHOP,R.drawable.black_bishop))
        piecesBox.add(ChessPiece(6, 7, ChessPlayer.BLACK, ChessRank.KNIGHT,R.drawable.black_knight))
        piecesBox.add(ChessPiece(7, 7, ChessPlayer.BLACK, ChessRank.ROOK,R.drawable.black_rook))

        piecesBox.add(ChessPiece(0, 6, ChessPlayer.BLACK, ChessRank.PAWN,R.drawable.black_pawn))
        piecesBox.add(ChessPiece(1, 6, ChessPlayer.BLACK, ChessRank.PAWN,R.drawable.black_pawn))
        piecesBox.add(ChessPiece(2, 6, ChessPlayer.BLACK, ChessRank.PAWN,R.drawable.black_pawn))
        piecesBox.add(ChessPiece(3, 6, ChessPlayer.BLACK, ChessRank.PAWN,R.drawable.black_pawn))
        piecesBox.add(ChessPiece(4, 6, ChessPlayer.BLACK, ChessRank.PAWN,R.drawable.black_pawn))
        piecesBox.add(ChessPiece(5, 6, ChessPlayer.BLACK, ChessRank.PAWN,R.drawable.black_pawn))
        piecesBox.add(ChessPiece(6, 6, ChessPlayer.BLACK, ChessRank.PAWN,R.drawable.black_pawn))
        piecesBox.add(ChessPiece(7, 6, ChessPlayer.BLACK, ChessRank.PAWN,R.drawable.black_pawn))
    }

fun pieceAt(col: Int, row:Int):ChessPiece?
    {
        for(piece in piecesBox)
        {
            if(col == piece.col && row==piece.row)
            {
             return piece
            }
        }
        return null
    }
    override fun toString(): String {
        var desc = ""
        for (row in 7 downTo 0)
        {
            desc += "$row"
            for (col in 0..7)
                 {
                    val piece = pieceAt(col,row)
                     if (piece==null)
                     {
                         desc += " ."
                     }
                     else
                     {
                         val white = piece.player== ChessPlayer.WHITE
                         desc += " "
                         desc += when (piece.rank) {
                             ChessRank.KING -> {
                                 if(white)"k" else "K"
                             }

                             ChessRank.QUEEN -> {
                                 if(white)"q" else "Q"
                             }

                             ChessRank.BISHOP -> {
                                 if(white)"b" else "B"
                             }

                             ChessRank.KNIGHT -> {
                                 if(white)"n" else "N"
                             }

                             ChessRank.PAWN -> {
                                 if(white)"p" else "P"
                             }

                             ChessRank.ROOK -> {
                                 if(white)"r" else "R"
                             }
                         }
                     }
                 }
            desc+="\n"
        }
        desc += "  0 1 2 3 4 5 6 7"
        return desc
    }
}