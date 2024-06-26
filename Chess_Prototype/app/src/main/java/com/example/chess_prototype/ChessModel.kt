package com.example.chess_prototype

import android.util.Log
import kotlin.math.max
import kotlin.math.min
import kotlin.system.exitProcess
import androidx.lifecycle.MutableLiveData

class ChessModel {
        var piecesBox = mutableSetOf<ChessPiece>()
    companion object {
        var currentPlayer: ChessPlayer = ChessPlayer.WHITE
    }

    var whiteMoveCount = MutableLiveData(0)
    var blackMoveCount = MutableLiveData(0)

    var whiteKingMoved = false
    var blackKingMoved = false
    var whiteRookMoved = Array(2) { false }
    var blackRookMoved = Array(2) { false }

    var whiteKingCol: Int = 4
    var whiteKingRow: Int = 0
    var blackKingCol: Int = 4
    var blackKingRow: Int = 7




    init {
        reset()


    }

    fun opponent(player: ChessPlayer): ChessPlayer {
        return if (player == ChessPlayer.WHITE) ChessPlayer.BLACK else ChessPlayer.WHITE
    }


    fun movePiece(fromCol:Int,fromRow:Int,toCol:Int,toRow:Int)
        {
            if(fromCol==toCol && fromRow==toRow) return

            val movingPiece=pieceAt(fromCol,fromRow)?:return

            if (movingPiece.player != currentPlayer) {
                return
            }

            if (movingPiece.rank == ChessRank.KING && movingPiece.player == ChessPlayer.WHITE) {
                whiteKingCol = toCol
                whiteKingRow = toRow
                Log.d(TAG, "Regele s-a mutat la ($whiteKingRow, $whiteKingCol)")
            }

            if (movingPiece.rank == ChessRank.KING && movingPiece.player == ChessPlayer.BLACK) {
                blackKingCol = toCol
                blackKingRow = toRow
                Log.d(TAG, "Regele s-a mutat la ($blackKingRow, $blackKingCol)")
            }

            if (movingPiece.rank == ChessRank.PAWN) {
                if (movingPiece.player == ChessPlayer.WHITE) {
                    if (!whitePawnVer(fromCol, fromRow, toCol, toRow)) {
                        return
                    }
                } else if (movingPiece.player == ChessPlayer.BLACK) {
                    if (!blackPawnVer(fromCol, fromRow, toCol, toRow)) {
                        return
                    }
                }
            }
            if (movingPiece.rank == ChessRank.UPAWN) {
                if (movingPiece.player == ChessPlayer.WHITE) {
                    if (!uwhitePawnVer(fromCol, fromRow, toCol, toRow)) {
                        return
                    }
                } else if (movingPiece.player == ChessPlayer.BLACK) {
                    if (!ublackPawnVer(fromCol, fromRow, toCol, toRow)) {
                        return
                    }
                }
            }

            if (movingPiece.rank == ChessRank.ROOK) {
                if (movingPiece.player == ChessPlayer.WHITE) {
                    if (!whiteRookVer(fromCol, fromRow, toCol, toRow)) {
                        return
                    } else {
                        if (fromRow == 0) {
                            if (fromCol == 0) {
                                whiteRookMoved[0] = true
                            }
                            if (fromCol == 7) {
                                whiteRookMoved[1] = true
                            }
                        }
                    }
                } else if (movingPiece.player == ChessPlayer.BLACK) {
                    if (!blackRookVer(fromCol, fromRow, toCol, toRow)) {
                        return
                    } else {
                        if (fromRow == 7) {
                            if (fromCol == 0) {
                                blackRookMoved[0] = true
                            }
                            if (fromCol == 7) {
                                blackRookMoved[1] = true
                            }
                        }
                    }
                }
            }
            if (movingPiece.rank == ChessRank.UROOK) {
                if (movingPiece.player == ChessPlayer.WHITE) {
                    if (!uWhiteRookVer(fromCol, fromRow, toCol, toRow)) {
                        return
                    } else {
                        if (fromRow == 0) {
                            if (fromCol == 0) {
                                whiteRookMoved[0] = true
                            }
                            if (fromCol == 7) {
                                whiteRookMoved[1] = true
                            }
                        }
                    }
                } else if (movingPiece.player == ChessPlayer.BLACK) {
                    if (!uBlackRookVer(fromCol, fromRow, toCol, toRow)) {
                        return
                    } else {
                        if (fromRow == 7) {
                            if (fromCol == 0) {
                                blackRookMoved[0] = true
                            }
                            if (fromCol == 7) {
                                blackRookMoved[1] = true
                            }
                        }
                    }
                }
            }

            if (movingPiece.rank == ChessRank.KNIGHT) {
                if (movingPiece.player == ChessPlayer.WHITE) {
                    if (!whiteKnightVer(fromCol, fromRow, toCol, toRow)) {
                        return
                    }
                } else if (movingPiece.player == ChessPlayer.BLACK) {
                    if (!blackKnightVer(fromCol, fromRow, toCol, toRow)) {
                        return
                    }
                }
            }

            if (movingPiece.rank == ChessRank.BISHOP) {
                if (movingPiece.player == ChessPlayer.WHITE) {
                    if (!whiteBishopVer(fromCol, fromRow, toCol, toRow)) {
                        return
                    }
                } else if (movingPiece.player == ChessPlayer.BLACK) {
                    if (!blackBishopVer(fromCol, fromRow, toCol, toRow)) {
                        return
                    }
                }
            }

            if (movingPiece.rank == ChessRank.QUEEN) {
                if (movingPiece.player == ChessPlayer.WHITE) {
                    if (!whiteQueenVer(fromCol, fromRow, toCol, toRow)) {
                        return
                    }
                } else if (movingPiece.player == ChessPlayer.BLACK) {
                    if (!blackQueenVer(fromCol, fromRow, toCol, toRow)) {
                        return
                    }
                }
            }

            if (movingPiece.rank == ChessRank.UQUEEN) {
                if (movingPiece.player == ChessPlayer.WHITE) {
                    if (!uwhiteQueenVer(fromCol, fromRow, toCol, toRow)) {
                        return
                    }
                } else if (movingPiece.player == ChessPlayer.BLACK) {
                    if (!ublackQueenVer(fromCol, fromRow, toCol, toRow)) {
                        return
                    }
                }
            }

            if (movingPiece.rank == ChessRank.KING && movingPiece.player == ChessPlayer.WHITE) {
                if (toCol == 2 && toRow == 0 && canWhiteCastleQueenside()) {
                    piecesBox.remove(movingPiece)
                    piecesBox.add(ChessPiece(2, 0, ChessPlayer.WHITE, ChessRank.KING, R.drawable.white_king))
                    whiteKingMoved = true

                    val rook = pieceAt(0, 0)!!
                    piecesBox.remove(rook)
                    piecesBox.add(ChessPiece(3, 0, ChessPlayer.WHITE, ChessRank.ROOK, R.drawable.white_rook))
                    whiteRookMoved[0] = true

                    //currentPlayer = ChessPlayer.BLACK
                    return
                } else if (toCol == 6 && toRow == 0 && canWhiteCastleKingside()) {
                    piecesBox.remove(movingPiece)
                    piecesBox.add(ChessPiece(6, 0, ChessPlayer.WHITE, ChessRank.KING, R.drawable.white_king))
                    whiteKingMoved = true

                    val rook = pieceAt(7, 0)!!
                    piecesBox.remove(rook)
                    piecesBox.add(ChessPiece(5, 0, ChessPlayer.WHITE, ChessRank.ROOK, R.drawable.white_rook))
                    whiteRookMoved[1] = true

                    //currentPlayer = ChessPlayer.BLACK
                    return
                }
            }

            if (movingPiece.rank == ChessRank.KING && movingPiece.player == ChessPlayer.BLACK) {
                if (toCol == 2 && toRow == 7 && canBlackCastleQueenside()) {
                    piecesBox.remove(movingPiece)
                    piecesBox.add(ChessPiece(2, 7, ChessPlayer.BLACK, ChessRank.KING, R.drawable.black_king))
                    blackKingMoved = true

                    val rook = pieceAt(0, 7)!!
                    piecesBox.remove(rook)
                    piecesBox.add(ChessPiece(3, 7, ChessPlayer.BLACK, ChessRank.ROOK, R.drawable.black_rook))
                    blackRookMoved[0] = true

                    currentPlayer = ChessPlayer.WHITE
                    return
                } else if (toCol == 6 && toRow == 7 && canBlackCastleKingside()) {
                    piecesBox.remove(movingPiece)
                    piecesBox.add(ChessPiece(6, 7, ChessPlayer.BLACK, ChessRank.KING, R.drawable.black_king))
                    blackKingMoved = true

                    val rook = pieceAt(7, 7)!!
                    piecesBox.remove(rook)
                    piecesBox.add(ChessPiece(5, 7, ChessPlayer.BLACK, ChessRank.ROOK, R.drawable.black_rook))
                    blackRookMoved[1] = true

                    currentPlayer = ChessPlayer.WHITE
                    return
                }
            }

            var isCheck = if (currentPlayer == ChessPlayer.WHITE) alb_in_sah() else negru_in_sah()

            if (isCheck) {
                Log.d(TAG, "Nu poți lăsa regele în șah!")


                return
            }

        pieceAt(toCol,toRow)?.let{
            if(it.player == movingPiece.player)
            {
                return
            }
            piecesBox.remove(it)}

        piecesBox.remove(movingPiece)
        if (movingPiece.rank == ChessRank.KING) {
            val kingColor = if (movingPiece.player == ChessPlayer.WHITE) "alb" else "negru"
        }
        piecesBox.add(ChessPiece(toCol,toRow,movingPiece.player,movingPiece.rank, movingPiece.resID))
            currentPlayer = if (currentPlayer == ChessPlayer.WHITE) ChessPlayer.BLACK else ChessPlayer.WHITE

            // Verifică dacă regele oponent este în șah sau șah mat
            when (currentPlayer) {
                ChessPlayer.WHITE -> whiteMoveCount.value = whiteMoveCount.value?.plus(1)
                ChessPlayer.BLACK -> blackMoveCount.value = blackMoveCount.value?.plus(1)
            }
            if ((currentPlayer == ChessPlayer.BLACK && negru_in_sah()) || (currentPlayer == ChessPlayer.WHITE && alb_in_sah())) {
                Log.d(TAG, "Șah")
                if (!canKingEscapeCheck(opponent(currentPlayer))) {
                    Log.d(TAG, "Șah Mat pentru ${if (opponent(currentPlayer) == ChessPlayer.WHITE) "Alb" else "Negru"}")
                }
            }


    }

















    fun isBlackKingInCheckByWhiteBishop(): Boolean {
        val blackKingPosition = Pair(blackKingCol, blackKingRow)

        for (piece in piecesBox) {
            if (piece.rank == ChessRank.BISHOP && piece.player == ChessPlayer.WHITE) {
                val bishopPosition = Pair(piece.col, piece.row)
                if (isSameDiagonal(bishopPosition, blackKingPosition) && isClearDiagonalPath(bishopPosition, blackKingPosition)) {
                    Log.d(TAG, "Regele negru este în șah de nebun")
                    return true
                }
            }
        }
        return false
    }
    fun isWhiteKingInCheckByBlackBishop(): Boolean {
        val whiteKingPosition = Pair(whiteKingCol, whiteKingRow)

        for (piece in piecesBox) {
            if (piece.rank == ChessRank.BISHOP && piece.player == ChessPlayer.BLACK) {
                val bishopPosition = Pair(piece.col, piece.row)
                if (isSameDiagonal(bishopPosition, whiteKingPosition) && isClearDiagonalPath(bishopPosition, whiteKingPosition)) {
                    Log.d(TAG, "Regele alb este în șah de nebun")
                    return true
                }
            }
        }
        return false
    }

    fun isBlackKingInCheckByWhiteRook(): Boolean {
        val blackKingPosition = Pair(blackKingCol, blackKingRow)

        for (piece in piecesBox) {
            if (piece.player == ChessPlayer.WHITE && piece.rank == ChessRank.ROOK) {
                val rookPosition = Pair(piece.col, piece.row)
                if (isSameRow(rookPosition,blackKingPosition) && isClearRowPath(rookPosition,blackKingPosition)) {
                    Log.d(TAG, "Regele negru este în șah de tura")
                    return true
                }
                if (isSameCol(rookPosition,blackKingPosition) && isClearColPath(rookPosition,blackKingPosition)) {
                    Log.d(TAG, "Regele negru este în șah de tura")
                    return true
                }
            }
        }
        return false
    }
    fun isWhiteKingInCheckByBlackRook(): Boolean {
        val whiteKingPosition = Pair(whiteKingCol, whiteKingRow)

        for (piece in piecesBox) {
            if (piece.player == ChessPlayer.BLACK && piece.rank == ChessRank.ROOK) {
                val rookPosition = Pair(piece.col, piece.row)
                if (isSameRow(rookPosition,whiteKingPosition) && isClearRowPath(rookPosition,whiteKingPosition)) {
                    Log.d(TAG, "Regele alb este în șah de tura")
                    return true
                }
                if (isSameCol(rookPosition,whiteKingPosition) && isClearColPath(rookPosition,whiteKingPosition)) {
                    Log.d(TAG, "Regele alb este în șah de tura")
                    return true
                }
            }
        }
        return false
    }

    fun isBlackKingInCheckByWhiteQueen(): Boolean {
        val blackKingPosition = Pair(blackKingCol, blackKingRow)

        for (piece in piecesBox) {
            if (piece.player == ChessPlayer.WHITE && piece.rank == ChessRank.QUEEN) {
                val queenPosition = Pair(piece.col, piece.row)

                if (queenPosition.first == blackKingPosition.first || queenPosition.second == blackKingPosition.second) {
                    var clearPath = true
                    for (col in minOf(queenPosition.first, blackKingPosition.first) + 1 until maxOf(queenPosition.first, blackKingPosition.first)) {
                        if (pieceAt(col, queenPosition.second) != null) {
                            clearPath = false
                            break
                        }
                    }
                    if (clearPath) {
                        Log.d(TAG, "Regele negru este în șah de regină")
                        return true
                    }
                    clearPath = true
                    for (row in minOf(queenPosition.second, blackKingPosition.second) + 1 until maxOf(queenPosition.second, blackKingPosition.second)) {
                        if (pieceAt(queenPosition.first, row) != null) {
                            clearPath = false
                            break
                        }
                    }
                    if (clearPath) {
                        Log.d(TAG, "Regele negru este în șah de regină")
                        return true
                    }
                }

                if (Math.abs(queenPosition.first - blackKingPosition.first) == Math.abs(queenPosition.second - blackKingPosition.second)) {
                    var clearPath = true
                    val startCol = minOf(queenPosition.first, blackKingPosition.first) + 1
                    val endCol = maxOf(queenPosition.first, blackKingPosition.first)
                    val startRow = if (queenPosition.first < blackKingPosition.first) queenPosition.second + 1 else blackKingPosition.second + 1
                    val endRow = if (queenPosition.first < blackKingPosition.first) blackKingPosition.second else queenPosition.second

                    var col = startCol
                    var row = startRow
                    while (col < endCol) {
                        if (pieceAt(col, row) != null) {
                            clearPath = false
                            break
                        }
                        col++
                        if (queenPosition.first < blackKingPosition.first) row++ else row--
                    }

                    if (clearPath) {
                        Log.d(TAG, "Regele negru este în șah de regină")
                        return true
                    }
                }
            }
        }

        return false
    }
    fun isWhiteKingInCheckByBlackQueen(): Boolean {
        val whiteKingPosition = Pair(whiteKingCol, whiteKingRow)

        for (piece in piecesBox) {
            if (piece.player == ChessPlayer.BLACK && piece.rank == ChessRank.QUEEN) {
                val queenPosition = Pair(piece.col, piece.row)

                if (queenPosition.first == whiteKingPosition.first || queenPosition.second == whiteKingPosition.second) {
                    var clearPath = true
                    for (col in minOf(queenPosition.first, whiteKingPosition.first) + 1 until maxOf(queenPosition.first, whiteKingPosition.first)) {
                        if (pieceAt(col, queenPosition.second) != null) {
                            clearPath = false
                            break
                        }
                    }
                    if (clearPath) {
                        Log.d(TAG, "Regele alb este în șah de regină neagră")
                        return true
                    }
                    clearPath = true
                    for (row in minOf(queenPosition.second, whiteKingPosition.second) + 1 until maxOf(queenPosition.second, whiteKingPosition.second)) {
                        if (pieceAt(queenPosition.first, row) != null) {
                            clearPath = false
                            break
                        }
                    }
                    if (clearPath) {
                        Log.d(TAG, "Regele alb este în șah de regină neagră")
                        return true
                    }
                }

                if (Math.abs(queenPosition.first - whiteKingPosition.first) == Math.abs(queenPosition.second - whiteKingPosition.second)) {
                    var clearPath = true
                    val startCol = minOf(queenPosition.first, whiteKingPosition.first) + 1
                    val endCol = maxOf(queenPosition.first, whiteKingPosition.first)
                    val startRow = if (queenPosition.first < whiteKingPosition.first) queenPosition.second + 1 else whiteKingPosition.second + 1
                    val endRow = if (queenPosition.first < whiteKingPosition.first) whiteKingPosition.second else queenPosition.second

                    var col = startCol
                    var row = startRow
                    while (col < endCol) {
                        if (pieceAt(col, row) != null) {
                            clearPath = false
                            break
                        }
                        col++
                        if (queenPosition.first < whiteKingPosition.first) row++ else row--
                    }

                    if (clearPath) {
                        Log.d(TAG, "Regele alb este în șah de regină neagră")
                        return true
                    }
                }
            }
        }

        return false
    }


    fun isBlackKingInCheckByWhitePawn(): Boolean {
        val blackKingPosition = Pair(blackKingCol, blackKingRow)

        for (piece in piecesBox) {
            if (piece.player == ChessPlayer.WHITE && piece.rank == ChessRank.PAWN) {
                val pawnPosition = Pair(piece.col, piece.row)
                val attackingPositions = listOf(
                    Pair(pawnPosition.first - 1, pawnPosition.second + 1),
                    Pair(pawnPosition.first + 1, pawnPosition.second + 1)
                )
                if (attackingPositions.contains(blackKingPosition)) {
                    Log.d(TAG, "Regele negru este în șah de pion")
                    return true
                }
            }
        }
        return false
    }
    fun isWhiteKingInCheckByBlackPawn(): Boolean {
        val whiteKingPosition = Pair(whiteKingCol, whiteKingRow)

        for (piece in piecesBox) {
            if (piece.player == ChessPlayer.BLACK && piece.rank == ChessRank.PAWN) {
                val pawnPosition = Pair(piece.col, piece.row)
                val attackingPositions = listOf(
                    Pair(pawnPosition.first - 1, pawnPosition.second - 1),
                    Pair(pawnPosition.first + 1, pawnPosition.second - 1)
                )
                if (attackingPositions.contains(whiteKingPosition)) {
                    Log.d(TAG, "Regele alb este în șah de pion")
                    return true
                }
            }
        }
        return false
    }

    fun isBlackKingInCheckByWhiteKnight(): Boolean {
        val blackKingPosition = Pair(blackKingCol, blackKingRow)

        for (piece in piecesBox) {
            if (piece.player == ChessPlayer.WHITE && piece.rank == ChessRank.KNIGHT) {
                val knightPosition = Pair(piece.col, piece.row)
                val knightMoves = listOf(
                    Pair(knightPosition.first - 1, knightPosition.second - 2),
                    Pair(knightPosition.first + 1, knightPosition.second - 2),
                    Pair(knightPosition.first - 2, knightPosition.second - 1),
                    Pair(knightPosition.first + 2, knightPosition.second - 1),
                    Pair(knightPosition.first - 2, knightPosition.second + 1),
                    Pair(knightPosition.first + 2, knightPosition.second + 1),
                    Pair(knightPosition.first - 1, knightPosition.second + 2),
                    Pair(knightPosition.first + 1, knightPosition.second + 2)
                )
                if (knightMoves.contains(blackKingPosition)) {
                    Log.d(TAG, "Regele negru este în șah de cal")
                    return true
                }
            }
        }
        return false
    }
    fun isWhiteKingInCheckByBlackKnight(): Boolean {
        val whiteKingPosition = Pair(whiteKingCol, whiteKingRow)

        for (piece in piecesBox) {
            if (piece.player == ChessPlayer.BLACK && piece.rank == ChessRank.KNIGHT) {
                val knightPosition = Pair(piece.col, piece.row)
                val knightMoves = listOf(
                    Pair(knightPosition.first - 1, knightPosition.second - 2),
                    Pair(knightPosition.first + 1, knightPosition.second - 2),
                    Pair(knightPosition.first - 2, knightPosition.second - 1),
                    Pair(knightPosition.first + 2, knightPosition.second - 1),
                    Pair(knightPosition.first - 2, knightPosition.second + 1),
                    Pair(knightPosition.first + 2, knightPosition.second + 1),
                    Pair(knightPosition.first - 1, knightPosition.second + 2),
                    Pair(knightPosition.first + 1, knightPosition.second + 2)
                )
                if (knightMoves.contains(whiteKingPosition)) {
                    Log.d(TAG, "Regele alb este în șah de cal")
                    return true
                }
            }
        }
        return false
    }













    private fun isSameDiagonal(pos1: Pair<Int, Int>, pos2: Pair<Int, Int>): Boolean {
        return Math.abs(pos1.first - pos2.first) == Math.abs(pos1.second - pos2.second)
    }

    private fun isClearDiagonalPath(start: Pair<Int, Int>, end: Pair<Int, Int>): Boolean {
        val (startCol, startRow) = start
        val (endCol, endRow) = end
        val colStep = if (endCol > startCol) 1 else -1
        val rowStep = if (endRow > startRow) 1 else -1

        var currentCol = startCol + colStep
        var currentRow = startRow + rowStep

        while (currentCol != endCol && currentRow != endRow) {
            if (pieceAt(currentCol, currentRow) != null) {
                return false
            }
            currentCol += colStep
            currentRow += rowStep
        }
        return true
    }

    private fun isSameRow(pos1: Pair<Int, Int>, pos2: Pair<Int, Int>): Boolean {
        return pos1.second == pos2.second
    }

    private fun isClearRowPath(start: Pair<Int, Int>, end: Pair<Int, Int>): Boolean {
        val (startCol, row) = start
        val (endCol, _) = end
        val colStep = if (endCol > startCol) 1 else -1

        var currentCol = startCol + colStep

        while (currentCol != endCol) {
            if (pieceAt(currentCol, row) != null) {
                return false
            }
            currentCol += colStep
        }
        return true
    }

    private fun isSameCol(pos1: Pair<Int, Int>, pos2: Pair<Int, Int>): Boolean {
        return pos1.first == pos2.first
    }

    private fun isClearColPath(start: Pair<Int, Int>, end: Pair<Int, Int>): Boolean {
        val (col, startRow) = start
        val (_, endRow) = end
        val rowStep = if (endRow > startRow) 1 else -1

        var currentRow = startRow + rowStep

        while (currentRow != endRow) {
            if (pieceAt(col, currentRow) != null) {
                return false
            }
            currentRow += rowStep
        }
        return true
    }


    fun negru_in_sah(): Boolean{
        if(isBlackKingInCheckByWhiteKnight()||isBlackKingInCheckByWhitePawn()||isBlackKingInCheckByWhiteRook()||isBlackKingInCheckByWhiteBishop()||isBlackKingInCheckByWhiteQueen())
            return true
        return false
    }
    fun alb_in_sah(): Boolean{
        if(isWhiteKingInCheckByBlackKnight()||isWhiteKingInCheckByBlackBishop()||isWhiteKingInCheckByBlackQueen()||isWhiteKingInCheckByBlackRook()||isWhiteKingInCheckByBlackPawn())
            return true
        return false
    }

    fun canKingEscapeCheck(player: ChessPlayer): Boolean {
        val (kingCol, kingRow) = if (player == ChessPlayer.WHITE) Pair(whiteKingCol, whiteKingRow) else Pair(blackKingCol, blackKingRow)
        val possibleMoves = listOf(
            Pair(kingCol - 1, kingRow - 1), Pair(kingCol, kingRow - 1), Pair(kingCol + 1, kingRow - 1),
            Pair(kingCol - 1, kingRow),                             Pair(kingCol + 1, kingRow),
            Pair(kingCol - 1, kingRow + 1), Pair(kingCol, kingRow + 1), Pair(kingCol + 1, kingRow + 1)
        )

        for (move in possibleMoves) {
            val (newCol, newRow) = move
            if (newCol in 0..7 && newRow in 0..7) {
                val originalPiece = pieceAt(newCol, newRow)
                val king = pieceAt(kingCol, kingRow)
                piecesBox.remove(king)
                originalPiece?.let { piecesBox.remove(it) }
                piecesBox.add(ChessPiece(newCol, newRow, player, ChessRank.KING, king!!.resID))

                val kingInCheck = if (player == ChessPlayer.WHITE) alb_in_sah() else negru_in_sah()

                // Restore the original position
                piecesBox.remove(pieceAt(newCol, newRow))
                piecesBox.add(ChessPiece(kingCol, kingRow, player, ChessRank.KING, king.resID))
                originalPiece?.let { piecesBox.add(it) }

                if (!kingInCheck) {
                    return true
                }
            }
        }
        return false
    }














    private fun canWhiteCastleKingside(): Boolean {
        if (whiteKingMoved || whiteRookMoved[1]) {
            return false
        }
        for (col in 5..6) {
            if (pieceAt(col, 0) != null) {
                return false
            }
        }
        return true
    }

    private fun canWhiteCastleQueenside(): Boolean {
        if (whiteKingMoved || whiteRookMoved[0]) {
            return false
        }
        for (col in 1..3) {
            if (pieceAt(col, 0) != null) {
                return false
            }
        }
        return true
    }

    private fun canBlackCastleKingside(): Boolean {
        if (blackKingMoved || blackRookMoved[1]) {
            return false
        }
        for (col in 5..6) {
            if (pieceAt(col, 7) != null) {
                return false
            }
        }
        return true
    }

    private fun canBlackCastleQueenside(): Boolean {
        if (blackKingMoved || blackRookMoved[0]) {
            return false
        }
        for (col in 1..3) {
            if (pieceAt(col, 7) != null) {
                return false
            }
        }
        return true
    }



    fun whiteRookVer(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int): Boolean {
        if (fromCol == toCol || fromRow == toRow) {
            if (fromCol == toCol) {
                val start = min(fromRow, toRow) + 1
                val end = max(fromRow, toRow)
                for (row in start until end) {
                    if (pieceAt(fromCol, row) != null) {
                        return false
                    }
                }
            } else if (fromRow == toRow) {
                val start = min(fromCol, toCol) + 1
                val end = max(fromCol, toCol)
                for (col in start until end) {
                    if (pieceAt(col, fromRow) != null) {
                        return false
                    }
                }
            }
            val pieceAtDestination = pieceAt(toCol, toRow)
            if (pieceAtDestination == null || pieceAtDestination.player == ChessPlayer.BLACK) {
                return true
            }
        }
        return false
    }

    fun blackRookVer(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int): Boolean {
        if (fromCol == toCol || fromRow == toRow) {
            if (fromCol == toCol) {
                val start = min(fromRow, toRow) + 1
                val end = max(fromRow, toRow)
                for (row in start until end) {
                    if (pieceAt(fromCol, row) != null) {
                        return false
                    }
                }
            } else if (fromRow == toRow) {
                val start = min(fromCol, toCol) + 1
                val end = max(fromCol, toCol)
                for (col in start until end) {
                    if (pieceAt(col, fromRow) != null) {
                        return false
                    }
                }
            }
            val pieceAtDestination = pieceAt(toCol, toRow)
            if (pieceAtDestination == null || pieceAtDestination.player == ChessPlayer.WHITE) {
                return true
            }
        }
        return false
    }


    fun uWhiteRookVer(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int): Boolean {
        // Verifică dacă este o mutare normală de tură
        if (whiteRookVer(fromCol, fromRow, toCol, toRow)) {
            return true
        }

        // Verifică dacă este o mutare specială de UROOK (sărind peste o piesă)
        if (fromCol == toCol) {
            val step = if (toRow > fromRow) 1 else -1
            for (row in fromRow + step until toRow step step) {
                if (pieceAt(fromCol, row) != null) {
                    if (row + step == toRow) {
                        val targetPiece = pieceAt(toCol, toRow)
                        if (targetPiece != null && targetPiece.player != ChessPlayer.WHITE) {
                            return true
                        }
                    }
                    return false
                }
            }
        } else if (fromRow == toRow) {
            val step = if (toCol > fromCol) 1 else -1
            for (col in fromCol + step until toCol step step) {
                if (pieceAt(col, fromRow) != null) {
                    if (col + step == toCol) {
                        val targetPiece = pieceAt(toCol, toRow)
                        if (targetPiece != null && targetPiece.player != ChessPlayer.WHITE) {
                            return true
                        }
                    }
                    return false
                }
            }
        }

        return false
    }

    fun uBlackRookVer(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int): Boolean {
        // Verifică dacă este o mutare normală de tură
        if (blackRookVer(fromCol, fromRow, toCol, toRow)) {
            return true
        }

        // Verifică dacă este o mutare specială de UROOK (sărind peste o piesă)
        if (fromCol == toCol) {
            val step = if (toRow > fromRow) 1 else -1
            for (row in fromRow + step until toRow step step) {
                if (pieceAt(fromCol, row) != null) {
                    if (row + step == toRow) {
                        val targetPiece = pieceAt(toCol, toRow)
                        if (targetPiece != null && targetPiece.player != ChessPlayer.BLACK) {
                            return true
                        }
                    }
                    return false
                }
            }
        } else if (fromRow == toRow) {
            val step = if (toCol > fromCol) 1 else -1
            for (col in fromCol + step until toCol step step) {
                if (pieceAt(col, fromRow) != null) {
                    if (col + step == toCol) {
                        val targetPiece = pieceAt(toCol, toRow)
                        if (targetPiece != null && targetPiece.player != ChessPlayer.BLACK) {
                            return true
                        }
                    }
                    return false
                }
            }
        }

        return false
    }



    fun whitePawnVer(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int): Boolean {
        if (fromRow == 1) {
            if ((toRow == fromRow + 1 || toRow == fromRow + 2) && toCol == fromCol) {
                if (toRow == fromRow + 1 && pieceAt(toCol, toRow) == null) {
                    return true
                } else if (toRow == fromRow + 2 && pieceAt(toCol, toRow) == null && pieceAt(toCol, fromRow + 1) == null) {
                    return true
                }
            }
        } else {
            if (toRow == fromRow + 1 && toCol == fromCol && pieceAt(toCol, toRow) == null) {
                return true
            }
        }

        if (toRow == fromRow + 1) {
            val rightDiagonalPiece = pieceAt(fromCol + 1, fromRow + 1)
            if (toCol == fromCol + 1 && rightDiagonalPiece != null && rightDiagonalPiece.player == ChessPlayer.BLACK) {
                return true
            }
            val leftDiagonalPiece = pieceAt(fromCol - 1, fromRow + 1)
            if (toCol == fromCol - 1 && leftDiagonalPiece != null && leftDiagonalPiece.player == ChessPlayer.BLACK) {
                return true
            }
        }

        return false
    }

    fun blackPawnVer(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int): Boolean {
        if (fromRow == 6) {
            if ((toRow == fromRow - 1 || toRow == fromRow - 2) && toCol == fromCol) {
                if (toRow == fromRow - 1 && pieceAt(toCol, toRow) == null) {
                    return true
                } else if (toRow == fromRow - 2 && pieceAt(toCol, toRow) == null && pieceAt(toCol, fromRow - 1) == null) {
                    return true
                }
            }
        } else {
            if (toRow == fromRow - 1 && toCol == fromCol && pieceAt(toCol, toRow) == null) {
                return true
            }
        }

        if (toRow == fromRow - 1) {
            val rightDiagonalPiece = pieceAt(fromCol + 1, fromRow - 1)
            if (toCol == fromCol + 1 && rightDiagonalPiece != null && rightDiagonalPiece.player == ChessPlayer.WHITE) {
                return true
            }
            val leftDiagonalPiece = pieceAt(fromCol - 1, fromRow - 1)
            if (toCol == fromCol - 1 && leftDiagonalPiece != null && leftDiagonalPiece.player == ChessPlayer.WHITE) {
                return true
            }
        }

        return false
    }


    fun uwhitePawnVer(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int): Boolean {
        // Mișcare inițială de două pătrate sau un pătrat
        if (fromRow == 1) {
            if ((toRow == fromRow + 1 || toRow == fromRow + 2) && toCol == fromCol) {
                if (toRow == fromRow + 1 && pieceAt(toCol, toRow) == null) {
                    return true
                } else if (toRow == fromRow + 2 && pieceAt(toCol, toRow) == null && pieceAt(toCol, fromRow + 1) == null) {
                    return true
                }
            }
        } else {
            if (toRow == fromRow + 1 && toCol == fromCol && pieceAt(toCol, toRow) == null) {
                return true
            }
        }

        // Capturare diagonală normală
        if (toRow == fromRow + 1) {
            val rightDiagonalPiece = pieceAt(fromCol + 1, fromRow + 1)
            if (toCol == fromCol + 1 && rightDiagonalPiece != null && rightDiagonalPiece.player == ChessPlayer.BLACK) {
                return true
            }
            val leftDiagonalPiece = pieceAt(fromCol - 1, fromRow + 1)
            if (toCol == fromCol - 1 && leftDiagonalPiece != null && leftDiagonalPiece.player == ChessPlayer.BLACK) {
                return true
            }
        }

        // Capturare diagonală la două pătrate distanță
        if (toRow == fromRow + 2) {
            val rightDiagonalPiece = pieceAt(fromCol + 2, fromRow + 2)
            if (toCol == fromCol + 2 && rightDiagonalPiece != null && rightDiagonalPiece.player == ChessPlayer.BLACK) {
                return true
            }
            val leftDiagonalPiece = pieceAt(fromCol - 2, fromRow + 2)
            if (toCol == fromCol - 2 && leftDiagonalPiece != null && leftDiagonalPiece.player == ChessPlayer.BLACK) {
                return true
            }
        }

        return false
    }

    fun ublackPawnVer(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int): Boolean {
        // Mișcare inițială de două pătrate sau un pătrat
        if (fromRow == 6) {
            if ((toRow == fromRow - 1 || toRow == fromRow - 2) && toCol == fromCol) {
                if (toRow == fromRow - 1 && pieceAt(toCol, toRow) == null) {
                    return true
                } else if (toRow == fromRow - 2 && pieceAt(toCol, toRow) == null && pieceAt(toCol, fromRow - 1) == null) {
                    return true
                }
            }
        } else {
            if (toRow == fromRow - 1 && toCol == fromCol && pieceAt(toCol, toRow) == null) {
                return true
            }
        }

        // Capturare diagonală normală
        if (toRow == fromRow - 1) {
            val rightDiagonalPiece = pieceAt(fromCol + 1, fromRow - 1)
            if (toCol == fromCol + 1 && rightDiagonalPiece != null && rightDiagonalPiece.player == ChessPlayer.WHITE) {
                return true
            }
            val leftDiagonalPiece = pieceAt(fromCol - 1, fromRow - 1)
            if (toCol == fromCol - 1 && leftDiagonalPiece != null && leftDiagonalPiece.player == ChessPlayer.WHITE) {
                return true
            }
        }

        // Capturare diagonală la două pătrate distanță
        if (toRow == fromRow - 2) {
            val rightDiagonalPiece = pieceAt(fromCol + 2, fromRow - 2)
            if (toCol == fromCol + 2 && rightDiagonalPiece != null && rightDiagonalPiece.player == ChessPlayer.WHITE) {
                return true
            }
            val leftDiagonalPiece = pieceAt(fromCol - 2, fromRow - 2)
            if (toCol == fromCol - 2 && leftDiagonalPiece != null && leftDiagonalPiece.player == ChessPlayer.WHITE) {
                return true
            }
        }

        return false
    }


    fun whiteKnightVer(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int): Boolean {
        val dx = Math.abs(fromCol - toCol)
        val dy = Math.abs(fromRow - toRow)
        if ((dx == 2 && dy == 1) || (dx == 1 && dy == 2)) {
            val pieceAtDestination = pieceAt(toCol, toRow)
            if (pieceAtDestination == null || pieceAtDestination.player == ChessPlayer.BLACK) {
                return true
            }
        }
        return false
    }

    fun blackKnightVer(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int): Boolean {
        val dx = Math.abs(fromCol - toCol)
        val dy = Math.abs(fromRow - toRow)
        if ((dx == 2 && dy == 1) || (dx == 1 && dy == 2)) {
            val pieceAtDestination = pieceAt(toCol, toRow)
            if (pieceAtDestination == null || pieceAtDestination.player == ChessPlayer.WHITE) {
                return true
            }
        }
        return false
    }


    fun whiteBishopVer(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int): Boolean {
        val dx = Math.abs(fromCol - toCol)
        val dy = Math.abs(fromRow - toRow)
        if (dx != dy) {
            return false
        }

        val xStep = if (toCol > fromCol) 1 else -1
        val yStep = if (toRow > fromRow) 1 else -1
        var col = fromCol + xStep
        var row = fromRow + yStep

        while (col != toCol && row != toRow) {
            if (pieceAt(col, row) != null) {
                return false
            }
            col += xStep
            row += yStep
        }

        val pieceAtDestination = pieceAt(toCol, toRow)
        return pieceAtDestination == null || pieceAtDestination.player == ChessPlayer.BLACK
    }

    fun blackBishopVer(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int): Boolean {
        val dx = Math.abs(fromCol - toCol)
        val dy = Math.abs(fromRow - toRow)
        if (dx != dy) {
            return false
        }

        val xStep = if (toCol > fromCol) 1 else -1
        val yStep = if (toRow > fromRow) 1 else -1
        var col = fromCol + xStep
        var row = fromRow + yStep

        while (col != toCol && row != toRow) {
            if (pieceAt(col, row) != null) {
                return false
            }
            col += xStep
            row += yStep
        }

        val pieceAtDestination = pieceAt(toCol, toRow)
        return pieceAtDestination == null || pieceAtDestination.player == ChessPlayer.WHITE
    }

    fun whiteQueenVer(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int): Boolean {
        if (fromCol == toCol || fromRow == toRow) {
            return whiteRookVer(fromCol, fromRow, toCol, toRow)
        }

        return whiteBishopVer(fromCol, fromRow, toCol, toRow)
    }

    fun blackQueenVer(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int): Boolean {
        if (fromCol == toCol || fromRow == toRow) {
            return blackRookVer(fromCol, fromRow, toCol, toRow)
        }

        return blackBishopVer(fromCol, fromRow, toCol, toRow)
    }


    fun uwhiteQueenVer(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int): Boolean {
        val colDiff = Math.abs(toCol - fromCol)
        val rowDiff = Math.abs(toRow - fromRow)

        // Verificare mutare pe diagonala un patrat
        if (colDiff == 1 && rowDiff == 1) {
            val targetPiece = pieceAt(toCol, toRow)
            return targetPiece == null || targetPiece.player == ChessPlayer.BLACK
        }

        // Verificare mutare ca o tura
        if (colDiff == 0 || rowDiff == 0) {
            val stepCol = if (toCol - fromCol > 0) 1 else if (toCol - fromCol < 0) -1 else 0
            val stepRow = if (toRow - fromRow > 0) 1 else if (toRow - fromRow < 0) -1 else 0

            var currentCol = fromCol + stepCol
            var currentRow = fromRow + stepRow

            while (currentCol != toCol || currentRow != toRow) {
                if (pieceAt(currentCol, currentRow) != null) {
                    return false
                }
                currentCol += stepCol
                currentRow += stepRow
            }

            val targetPiece = pieceAt(toCol, toRow)
            return targetPiece == null || targetPiece.player == ChessPlayer.BLACK
        }

        return false
    }

    fun ublackQueenVer(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int): Boolean {
        val colDiff = Math.abs(toCol - fromCol)
        val rowDiff = Math.abs(toRow - fromRow)

        // Verificare mutare pe diagonala un patrat
        if (colDiff == 1 && rowDiff == 1) {
            val targetPiece = pieceAt(toCol, toRow)
            return targetPiece == null || targetPiece.player == ChessPlayer.WHITE
        }

        // Verificare mutare ca o tura
        if (colDiff == 0 || rowDiff == 0) {
            val stepCol = if (toCol - fromCol > 0) 1 else if (toCol - fromCol < 0) -1 else 0
            val stepRow = if (toRow - fromRow > 0) 1 else if (toRow - fromRow < 0) -1 else 0

            var currentCol = fromCol + stepCol
            var currentRow = fromRow + stepRow

            while (currentCol != toCol || currentRow != toRow) {
                if (pieceAt(currentCol, currentRow) != null) {
                    return false
                }
                currentCol += stepCol
                currentRow += stepRow
            }

            val targetPiece = pieceAt(toCol, toRow)
            return targetPiece == null || targetPiece.player == ChessPlayer.WHITE
        }

        return false
    }


    private fun whiteKingVer(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int): Boolean {
        if (Math.abs(toCol - fromCol) <= 1 && Math.abs(toRow - fromRow) <= 1) {
            val destinationPiece = pieceAt(toCol, toRow)
            if (destinationPiece == null || destinationPiece.player == ChessPlayer.BLACK) {
                return true
            }
        }
        if (toCol == 6 && fromRow == toRow && fromRow == 0 && canWhiteCastleKingside()) {
            return true
        }
        if (toCol == 2 && fromRow == toRow && fromRow == 0 && canWhiteCastleQueenside()) {
            return true
        }

        return false
    }

    fun blackKingVer(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int): Boolean {
        if (Math.abs(toCol - fromCol) <= 1 && Math.abs(toRow - fromRow) <= 1) {
            val destinationPiece = pieceAt(toCol, toRow)
            if (destinationPiece == null || destinationPiece.player == ChessPlayer.WHITE) {
                return true
            }
        }

        if (toCol == 6 && fromRow == toRow && fromRow == 7 && canBlackCastleKingside()) {
            return true
        }

        if (toCol == 2 && fromRow == toRow && fromRow == 7 && canBlackCastleQueenside()) {
            return true
        }

        return false
    }


    fun decrementMoveCount(player: ChessPlayer) {
        when (player) {
            ChessPlayer.WHITE -> {
                whiteMoveCount.value = whiteMoveCount.value?.minus(2)
                if (whiteMoveCount.value ?: 0 >= 4) {
                    changeToURook(ChessPlayer.WHITE)
                }
            }
            ChessPlayer.BLACK -> {
                blackMoveCount.value = blackMoveCount.value?.minus(2)
                if (blackMoveCount.value ?: 0 >= 4) {
                    changeToURook(ChessPlayer.BLACK)
                }
            }
        }
    }

    private fun changeToURook(player: ChessPlayer) {
        val rookCol = if (player == ChessPlayer.WHITE) 7 else 7
        val rookRow = if (player == ChessPlayer.WHITE) 0 else 7
        val rook = pieceAt(rookCol, rookRow)
        if (rook != null && rook.rank == ChessRank.ROOK && rook.player == player) {
            piecesBox.remove(rook)
            piecesBox.add(ChessPiece(rookCol, rookRow, player, ChessRank.UROOK, if (player == ChessPlayer.WHITE) R.drawable.u_white_rook else R.drawable.u_black_rook))
        }
    }




    fun reset()
    {
        piecesBox.removeAll(piecesBox)
        currentPlayer = ChessPlayer.WHITE

        whiteKingMoved = false
        blackKingMoved = false
        whiteRookMoved = Array(2) { false }
        blackRookMoved = Array(2) { false }

        whiteKingCol = 4
        whiteKingRow = 0
        blackKingCol = 4
        blackKingRow = 7

        whiteMoveCount.value = 0
        blackMoveCount.value = 0

        // Piese albe
        piecesBox.add(ChessPiece(0, 0, ChessPlayer.WHITE, ChessRank.UROOK,R.drawable.u_white_rook))
        piecesBox.add(ChessPiece(1, 0, ChessPlayer.WHITE, ChessRank.KNIGHT,R.drawable.white_knight))
        piecesBox.add(ChessPiece(2, 0, ChessPlayer.WHITE, ChessRank.BISHOP,R.drawable.white_bishop))
        piecesBox.add(ChessPiece(3, 0, ChessPlayer.WHITE, ChessRank.UQUEEN,R.drawable.u_white_queen))
        piecesBox.add(ChessPiece(4, 0, ChessPlayer.WHITE, ChessRank.KING,R.drawable.white_king))
        piecesBox.add(ChessPiece(5, 0, ChessPlayer.WHITE, ChessRank.BISHOP,R.drawable.white_bishop))
        piecesBox.add(ChessPiece(6, 0, ChessPlayer.WHITE, ChessRank.KNIGHT,R.drawable.white_knight))
        piecesBox.add(ChessPiece(7, 0, ChessPlayer.WHITE, ChessRank.ROOK,R.drawable.white_rook))

        piecesBox.add(ChessPiece(0, 1, ChessPlayer.WHITE, ChessRank.UPAWN,R.drawable.u_white_pawn))
        piecesBox.add(ChessPiece(1, 1, ChessPlayer.WHITE, ChessRank.PAWN,R.drawable.white_pawn))
        piecesBox.add(ChessPiece(2, 1, ChessPlayer.WHITE, ChessRank.PAWN,R.drawable.white_pawn))
        piecesBox.add(ChessPiece(3, 1, ChessPlayer.WHITE, ChessRank.PAWN,R.drawable.white_pawn))
        piecesBox.add(ChessPiece(4, 1, ChessPlayer.WHITE, ChessRank.PAWN,R.drawable.white_pawn))
        piecesBox.add(ChessPiece(5, 1, ChessPlayer.WHITE, ChessRank.PAWN,R.drawable.white_pawn))
        piecesBox.add(ChessPiece(6, 1, ChessPlayer.WHITE, ChessRank.PAWN,R.drawable.white_pawn))
        piecesBox.add(ChessPiece(7, 1, ChessPlayer.WHITE, ChessRank.PAWN,R.drawable.white_pawn))

// Piese negre
        piecesBox.add(ChessPiece(0, 7, ChessPlayer.BLACK, ChessRank.UROOK,R.drawable.u_black_rook))
        piecesBox.add(ChessPiece(1, 7, ChessPlayer.BLACK, ChessRank.KNIGHT,R.drawable.black_knight))
        piecesBox.add(ChessPiece(2, 7, ChessPlayer.BLACK, ChessRank.BISHOP,R.drawable.black_bishop))
        piecesBox.add(ChessPiece(3, 7, ChessPlayer.BLACK, ChessRank.UQUEEN,R.drawable.u_black_queen))
        piecesBox.add(ChessPiece(4, 7, ChessPlayer.BLACK, ChessRank.KING,R.drawable.black_king))
        piecesBox.add(ChessPiece(5, 7, ChessPlayer.BLACK, ChessRank.BISHOP,R.drawable.black_bishop))
        piecesBox.add(ChessPiece(6, 7, ChessPlayer.BLACK, ChessRank.KNIGHT,R.drawable.black_knight))
        piecesBox.add(ChessPiece(7, 7, ChessPlayer.BLACK, ChessRank.ROOK,R.drawable.black_rook))

        piecesBox.add(ChessPiece(0, 6, ChessPlayer.BLACK, ChessRank.UPAWN,R.drawable.u_black_pawn))
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
                             ChessRank.UQUEEN -> {
                                 if(white)"uq" else "UQ"
                             }

                             ChessRank.UROOK -> {
                                 if(white)"ur" else "UR"
                             }

                             ChessRank.UPAWN -> {
                                 if(white)"up" else "UP"
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