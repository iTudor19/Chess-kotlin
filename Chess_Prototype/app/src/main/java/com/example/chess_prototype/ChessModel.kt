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

    var eliminare: Int = 0



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
                    if(rook.rank == ChessRank.ROOK && rook.player == ChessPlayer.WHITE)
                    {piecesBox.remove(rook)
                    piecesBox.add(ChessPiece(3, 0, ChessPlayer.WHITE, ChessRank.ROOK, R.drawable.white_rook))
                    whiteRookMoved[0] = true
                        currentPlayer = ChessPlayer.BLACK
                    return}
                    else
                    {piecesBox.remove(rook)
                        piecesBox.add(ChessPiece(3, 0, ChessPlayer.WHITE, ChessRank.UROOK, R.drawable.u_white_rook))
                        whiteRookMoved[0] = true
                        currentPlayer = ChessPlayer.BLACK
                        return}
                } else if (toCol == 6 && toRow == 0 && canWhiteCastleKingside()) {
                    piecesBox.remove(movingPiece)
                    piecesBox.add(ChessPiece(6, 0, ChessPlayer.WHITE, ChessRank.KING, R.drawable.white_king))
                    whiteKingMoved = true

                    val rook = pieceAt(7, 0)!!
                    if(rook.rank == ChessRank.ROOK && rook.player == ChessPlayer.WHITE)
                    {piecesBox.remove(rook)
                    piecesBox.add(ChessPiece(5, 0, ChessPlayer.WHITE, ChessRank.ROOK, R.drawable.white_rook))
                    whiteRookMoved[1] = true
                        currentPlayer = ChessPlayer.BLACK
                    return}
                    else {
                        piecesBox.remove(rook)
                            piecesBox.add(ChessPiece(5, 0, ChessPlayer.WHITE, ChessRank.UROOK, R.drawable.u_white_rook))
                            whiteRookMoved[1] = true
                        currentPlayer = ChessPlayer.BLACK
                        return
                    }
                }
                else if (!whiteKingVer(fromCol, fromRow, toCol, toRow))
                    return
            }

            if (movingPiece.rank == ChessRank.KING && movingPiece.player == ChessPlayer.BLACK) {
                if (toCol == 2 && toRow == 7 && canBlackCastleQueenside()) {
                    piecesBox.remove(movingPiece)
                    piecesBox.add(ChessPiece(2, 7, ChessPlayer.BLACK, ChessRank.KING, R.drawable.black_king))
                    blackKingMoved = true

                    val rook = pieceAt(0, 7)!!
                    if(rook.rank == ChessRank.ROOK && rook.player == ChessPlayer.BLACK)
                    { piecesBox.remove(rook)
                    piecesBox.add(ChessPiece(3, 7, ChessPlayer.BLACK, ChessRank.ROOK, R.drawable.black_rook))
                    blackRookMoved[0] = true
                        currentPlayer = ChessPlayer.WHITE

                    return}
                    else{
                        piecesBox.remove(rook)
                        piecesBox.add(ChessPiece(3, 7, ChessPlayer.BLACK, ChessRank.UROOK, R.drawable.u_black_rook))
                        blackRookMoved[0] = true
                        currentPlayer = ChessPlayer.WHITE
                        return
                    }
                } else if (toCol == 6 && toRow == 7 && canBlackCastleKingside()) {
                    piecesBox.remove(movingPiece)
                    piecesBox.add(ChessPiece(6, 7, ChessPlayer.BLACK, ChessRank.KING, R.drawable.black_king))
                    blackKingMoved = true

                    val rook = pieceAt(7, 7)!!
                    if(rook.rank == ChessRank.ROOK && rook.player == ChessPlayer.BLACK)
                    {
                    piecesBox.remove(rook)
                    piecesBox.add(ChessPiece(5, 7, ChessPlayer.BLACK, ChessRank.ROOK, R.drawable.black_rook))
                    blackRookMoved[1] = true
                        currentPlayer = ChessPlayer.WHITE
                    return
                    }
                    else{
                        piecesBox.remove(rook)
                        piecesBox.add(ChessPiece(5, 7, ChessPlayer.BLACK, ChessRank.UROOK, R.drawable.u_black_rook))
                        blackRookMoved[1] = true
                        currentPlayer = ChessPlayer.WHITE


                        return
                    }
                }
                else if (!blackKingVer(fromCol, fromRow, toCol, toRow))
                    return
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

                when (currentPlayer) {
                    ChessPlayer.BLACK ->whiteMoveCount.value = whiteMoveCount.value?.plus(1)
                    ChessPlayer.WHITE ->{
                        blackMoveCount.value = blackMoveCount.value?.plus(1)
                        eliminare = eliminare + 1
                        if(eliminare % 20 == 0)
                        {
                                downgradeKinght(ChessPlayer.WHITE)
                                downgradeKinght(ChessPlayer.BLACK)
                                downgradeBishop(ChessPlayer.WHITE)
                                downgradeBishop(ChessPlayer.BLACK)
                                downgradeQueen(ChessPlayer.WHITE)
                                downgradeQueen(ChessPlayer.BLACK)
                                downgradeRook(ChessPlayer.WHITE)
                                downgradeRook(ChessPlayer.BLACK)
                                downgradePawn(ChessPlayer.WHITE)
                                downgradePawn(ChessPlayer.BLACK)
                        }
                    }

                }
            if ((currentPlayer == ChessPlayer.BLACK && negru_in_sah()) || (currentPlayer == ChessPlayer.WHITE && alb_in_sah())) {
                Log.d(TAG, "Șah")
                if (!canKingEscapeCheckmate(currentPlayer)) {
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

    fun isBlackKingInCheckByUWhiteRook(): Boolean {
        val blackKingPosition = Pair(blackKingCol, blackKingRow)

        for (piece in piecesBox) {
            if (piece.player == ChessPlayer.WHITE && piece.rank == ChessRank.UROOK) {
                val rookPosition = Pair(piece.col, piece.row)
                if (isSameRow(rookPosition,blackKingPosition) && isClearRowPath(rookPosition,blackKingPosition)) {
                    Log.d(TAG, "Regele negru este în șah de utura")
                    return true
                }
                if (isSameCol(rookPosition,blackKingPosition) && isClearColPath(rookPosition,blackKingPosition)) {
                    Log.d(TAG, "Regele negru este în șah de utura")
                    return true
                }
            }
        }
        return false
    }
    fun isWhiteKingInCheckByUBlackRook(): Boolean {
        val whiteKingPosition = Pair(whiteKingCol, whiteKingRow)

        for (piece in piecesBox) {
            if (piece.player == ChessPlayer.BLACK && piece.rank == ChessRank.UROOK) {
                val rookPosition = Pair(piece.col, piece.row)
                if (isSameRow(rookPosition,whiteKingPosition) && isClearRowPath(rookPosition,whiteKingPosition)) {
                    Log.d(TAG, "Regele alb este în șah de utura")
                    return true
                }
                if (isSameCol(rookPosition,whiteKingPosition) && isClearColPath(rookPosition,whiteKingPosition)) {
                    Log.d(TAG, "Regele alb este în șah de utura")
                    return true
                }
            }
        }
        return false
    }

    fun isBlackKingInCheckByWhiteQueen(): Boolean {
        val blackKingPosition = piecesBox.find { it.rank == ChessRank.KING && it.player == ChessPlayer.BLACK }?.let { Pair(it.col, it.row) }
            ?: return false

        for (piece in piecesBox) {
            if (piece.player == ChessPlayer.WHITE && piece.rank == ChessRank.QUEEN) {
                val queenPosition = Pair(piece.col, piece.row)

                // Verificare pentru un pătrat în orice direcție
                val dx = Math.abs(queenPosition.first - blackKingPosition.first)
                val dy = Math.abs(queenPosition.second - blackKingPosition.second)
                if (dx <= 1 && dy <= 1) {
                    Log.d(TAG, "Regele negru este în șah de regină albă")
                    return true
                }

                // Verificare pe coloană
                if (queenPosition.first == blackKingPosition.first && isClearColPath(queenPosition, blackKingPosition)) {
                    Log.d(TAG, "Regele negru este în șah de regină albă")
                    return true
                }

                // Verificare pe rând
                if (queenPosition.second == blackKingPosition.second && isClearRowPath(queenPosition, blackKingPosition)) {
                    Log.d(TAG, "Regele negru este în șah de regină albă")
                    return true
                }

                // Verificare pe diagonală
                if (dx == dy && isClearDiagonalPath(queenPosition, blackKingPosition)) {
                    Log.d(TAG, "Regele negru este în șah de regină albă")
                    return true
                }
            }
        }

        return false
    }

    fun isWhiteKingInCheckByBlackQueen(): Boolean {
        val whiteKingPosition = piecesBox.find { it.rank == ChessRank.KING && it.player == ChessPlayer.WHITE }?.let { Pair(it.col, it.row) }
            ?: return false

        for (piece in piecesBox) {
            if (piece.player == ChessPlayer.BLACK && piece.rank == ChessRank.QUEEN) {
                val queenPosition = Pair(piece.col, piece.row)

                // Verificare pentru un pătrat în orice direcție
                val dx = Math.abs(queenPosition.first - whiteKingPosition.first)
                val dy = Math.abs(queenPosition.second - whiteKingPosition.second)
                if ((dx <= 1 && dy <= 1)&&(true)) {
                    Log.d(TAG, "Regele alb este în șah de regină neagră")
                    return true
                }

                // Verificare pe coloană
                if (queenPosition.first == whiteKingPosition.first && isClearColPath(queenPosition, whiteKingPosition)) {
                    Log.d(TAG, "Regele alb este în șah de regină neagră")
                    return true
                }

                // Verificare pe rând
                if (queenPosition.second == whiteKingPosition.second && isClearRowPath(queenPosition, whiteKingPosition)) {
                    Log.d(TAG, "Regele alb este în șah de regină neagră")
                    return true
                }

                // Verificare pe diagonală
                if (dx == dy && isClearDiagonalPath(queenPosition, whiteKingPosition)) {
                    Log.d(TAG, "Regele alb este în șah de regină neagră")
                    return true
                }
            }
        }

        return false
    }





    fun isBlackKingInCheckByUWhiteQueen(): Boolean {
        val blackKingPosition = piecesBox.find { it.rank == ChessRank.KING  && it.player == ChessPlayer.BLACK }?.let { it.col to it.row } ?: return false
        return isUWhiteQueenChecking(blackKingPosition)
    }

    fun isWhiteKingInCheckByUBlackQueen(): Boolean {
        val whiteKingPosition = piecesBox.find { it.rank == ChessRank.KING && it.player == ChessPlayer.WHITE }?.let { it.col to it.row } ?: return false
        return isUBlackQueenChecking(whiteKingPosition)
    }

    fun isUWhiteQueenChecking(kingPosition: Pair<Int, Int>): Boolean {
        val (kingCol, kingRow) = kingPosition

        // Check one-square moves in all directions
        val directions = listOf(
            Pair(1, 0), Pair(-1, 0), // Horizontal
            Pair(0, 1), Pair(0, -1), // Vertical
            Pair(1, 1), Pair(-1, -1), // Diagonal
            Pair(1, -1), Pair(-1, 1) // Diagonal
        )

        for ((dc, dr) in directions) {
            val newCol = kingCol + dc
            val newRow = kingRow + dr
            var piece = pieceAt(newCol, newRow)
                if (piece != null && piece.rank == ChessRank.UQUEEN&&piece.player == ChessPlayer.WHITE) {
                    return true
                }
        }

        // Check rook-like moves
        return checkRookMovesForUQueen(kingCol, kingRow, ChessPlayer.WHITE)
    }

    fun isUBlackQueenChecking(kingPosition: Pair<Int, Int>): Boolean {
        val (kingCol, kingRow) = kingPosition

        // Check one-square moves in all directions
        val directions = listOf(
            Pair(1, 0), Pair(-1, 0), // Horizontal
            Pair(0, 1), Pair(0, -1), // Vertical
            Pair(1, 1), Pair(-1, -1), // Diagonal
            Pair(1, -1), Pair(-1, 1) // Diagonal
        )

        for ((dc, dr) in directions) {
            val newCol = kingCol + dc
            val newRow = kingRow + dr
            val piece = pieceAt(newCol, newRow)
            if (piece !=null && piece.rank == ChessRank.UQUEEN&&piece.player == ChessPlayer.WHITE) {
                return true
            }
        }

        // Check rook-like moves
        return checkRookMovesForUQueen(kingCol, kingRow, ChessPlayer.BLACK)
    }

    fun checkRookMovesForUQueen(kingCol: Int, kingRow: Int, player: ChessPlayer): Boolean {
        val directions = listOf(
            Pair(1, 0), Pair(-1, 0), // Horizontal
            Pair(0, 1), Pair(0, -1) // Vertical
        )

        for ((dc, dr) in directions) {
            var col = kingCol + dc
            var row = kingRow + dr

            while (col in 0..7 && row in 0..7) {
                val piece = pieceAt(col, row)

                if (piece != null) {
                    if (piece.rank == ChessRank.UQUEEN) {
                        return true
                    }
                    break
                }

                col += dc
                row += dr
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

    fun isBlackKingInCheckByUWhitePawn(): Boolean {
        val blackKingPosition = Pair(blackKingCol, blackKingRow)

        for (piece in piecesBox) {
            if (piece.player == ChessPlayer.WHITE && piece.rank == ChessRank.UPAWN) {
                val pawnPosition = Pair(piece.col, piece.row)
                val attackingPositions = listOf(
                    Pair(pawnPosition.first - 1, pawnPosition.second + 1),
                    Pair(pawnPosition.first + 1, pawnPosition.second + 1),
                    Pair(pawnPosition.first + 2, pawnPosition.second + 2),
                    Pair(pawnPosition.first - 2, pawnPosition.second + 2)
                )
                if (attackingPositions.contains(blackKingPosition)) {
                    Log.d(TAG, "Regele negru este în șah de upion")
                    return true
                }
            }
        }
        return false
    }
    fun isWhiteKingInCheckByUBlackPawn(): Boolean {
        val whiteKingPosition = Pair(whiteKingCol, whiteKingRow)

        for (piece in piecesBox) {
            if (piece.player == ChessPlayer.BLACK && piece.rank == ChessRank.UPAWN) {
                val pawnPosition = Pair(piece.col, piece.row)
                val attackingPositions = listOf(
                    Pair(pawnPosition.first - 1, pawnPosition.second - 1),
                    Pair(pawnPosition.first + 1, pawnPosition.second - 1),
                    Pair(pawnPosition.first + 2, pawnPosition.second + 2),
                    Pair(pawnPosition.first - 2, pawnPosition.second + 2)
                )
                if (attackingPositions.contains(whiteKingPosition)) {
                    Log.d(TAG, "Regele alb este în șah de upion")
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
        if(currentPlayer==ChessPlayer.WHITE &&(isBlackKingInCheckByUWhiteRook()||isBlackKingInCheckByUWhiteQueen()||isBlackKingInCheckByUWhitePawn()||isBlackKingInCheckByWhiteKnight()||isBlackKingInCheckByWhitePawn()||isBlackKingInCheckByWhiteRook()||isBlackKingInCheckByWhiteBishop()||isBlackKingInCheckByWhiteQueen()))
            return true
        return false
    }
    fun alb_in_sah(): Boolean{
        if(currentPlayer==ChessPlayer.WHITE &&( isWhiteKingInCheckByUBlackRook()||isWhiteKingInCheckByUBlackPawn()||isWhiteKingInCheckByUBlackQueen()||isWhiteKingInCheckByBlackKnight()||isWhiteKingInCheckByBlackBishop()||isWhiteKingInCheckByBlackQueen()||isWhiteKingInCheckByBlackRook()||isWhiteKingInCheckByBlackPawn()))
            return true
        return false
    }

    fun canKingEscapeCheckmate(kingColor: ChessPlayer): Boolean {
        // Verificăm dacă regele este în șah
        if (kingColor == ChessPlayer.WHITE && !alb_in_sah()) {
            return true // Regele alb nu este în șah, deci poate să scape
        } else if (kingColor == ChessPlayer.BLACK && !negru_in_sah()) {
            return true // Regele negru nu este în șah, deci poate să scape
        }

        // Determinăm poziția regelui
        val kingPosition = if (kingColor == ChessPlayer.WHITE) Pair(whiteKingCol, whiteKingRow) else Pair(blackKingCol, blackKingRow)

        // Verificăm toate posibilele mutări ale regelui
        val possibleMoves = arrayOf(
            Pair(kingPosition.first + 1, kingPosition.second),
            Pair(kingPosition.first - 1, kingPosition.second),
            Pair(kingPosition.first, kingPosition.second + 1),
            Pair(kingPosition.first, kingPosition.second - 1),
            Pair(kingPosition.first + 1, kingPosition.second + 1),
            Pair(kingPosition.first - 1, kingPosition.second - 1),
            Pair(kingPosition.first + 1, kingPosition.second - 1),
            Pair(kingPosition.first - 1, kingPosition.second + 1)
        )

        for (move in possibleMoves) {
            if (move.first in 0..7 && move.second in 0..7) {
                // Salvăm piesa de pe poziția de destinație (dacă există una acolo)
                val destinationPiece = pieceAt(move.first, move.second)

                // Mutăm temporar regele
                if (kingColor == ChessPlayer.WHITE) {
                    whiteKingCol = move.first
                    whiteKingRow = move.second
                } else {
                    blackKingCol = move.first
                    blackKingRow = move.second
                }

                // Verificăm dacă regele nu mai este în șah după mutarea temporară
                val isCheck = if (kingColor == ChessPlayer.WHITE) alb_in_sah() else negru_in_sah()
                if (!isCheck) {
                    // Dacă nu este în șah, putem reveni la poziția inițială a regelui și returna true
                    if (kingColor == ChessPlayer.WHITE) {
                        whiteKingCol = kingPosition.first
                        whiteKingRow = kingPosition.second
                    } else {
                        blackKingCol = kingPosition.first
                        blackKingRow = kingPosition.second
                    }
                    return true
                }

                // Dacă este în șah, revenim la starea inițială a regelui
                if (kingColor == ChessPlayer.WHITE) {
                    whiteKingCol = kingPosition.first
                    whiteKingRow = kingPosition.second
                } else {
                    blackKingCol = kingPosition.first
                    blackKingRow = kingPosition.second
                }

                // Punem înapoi piesa pe poziția de destinație (dacă exista una)
                if (destinationPiece != null) {
                    piecesBox.add(destinationPiece)
                }
            }
        }

        return false // Nu există mutare prin care regele să scape de șah mat
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
        if (whiteRookVer(fromCol, fromRow, toCol, toRow)) {
            return true
        }

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
        if (blackRookVer(fromCol, fromRow, toCol, toRow)) {
            return true
        }

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

        if (colDiff == 1 && rowDiff == 1) {
            val targetPiece = pieceAt(toCol, toRow)
            return targetPiece == null || targetPiece.player == ChessPlayer.BLACK
        }

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

        if (colDiff == 1 && rowDiff == 1) {
            val targetPiece = pieceAt(toCol, toRow)
            return targetPiece == null || targetPiece.player == ChessPlayer.WHITE
        }

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









    fun upgradeRook(player: ChessPlayer) {
        when (player) {
            ChessPlayer.WHITE -> {
                if (currentPlayer == ChessPlayer.WHITE && whiteMoveCount.value ?: 0 >= 5) {
                    changeToURook(ChessPlayer.WHITE)
                    currentPlayer = ChessPlayer.BLACK
                }
                if(whiteMoveCount.value ?: 0 >= 5)
                    whiteMoveCount.value = whiteMoveCount.value?.minus(5)
                else
                {
                    whiteMoveCount.postValue(0)
                }
            }
            ChessPlayer.BLACK -> {
                if (currentPlayer == ChessPlayer.BLACK&&blackMoveCount.value ?: 0 >= 5) {
                    changeToURook(ChessPlayer.BLACK)
                    currentPlayer = ChessPlayer.WHITE
                }
                if(blackMoveCount.value ?: 0 >= 5)
                    blackMoveCount.value = blackMoveCount.value?.minus(5)
                else
                {
                    blackMoveCount.postValue(0)
                }
            }
        }
    }

    private fun changeToURook(player: ChessPlayer) {
        val rook = piecesBox.find { it.rank == ChessRank.ROOK && it.player == player }

        if (rook != null) {
            piecesBox.remove(rook)
            piecesBox.add(ChessPiece(rook.col, rook.row, player, ChessRank.UROOK, if (player == ChessPlayer.WHITE) R.drawable.u_white_rook else R.drawable.u_black_rook))
        }
    }


    fun upgradePawn(player: ChessPlayer) {
        when (player) {
            ChessPlayer.WHITE -> {
                if (currentPlayer == ChessPlayer.WHITE && whiteMoveCount.value ?: 0 >= 2) {
                    changeToUPawn(ChessPlayer.WHITE)
                    currentPlayer = ChessPlayer.BLACK
                }
                if(whiteMoveCount.value ?: 0 >= 2)
                    whiteMoveCount.value = whiteMoveCount.value?.minus(2)
                else
                {
                    whiteMoveCount.postValue(0)
                }
            }
            ChessPlayer.BLACK -> {
                if (currentPlayer == ChessPlayer.BLACK&&blackMoveCount.value ?: 0 >= 2) {
                    changeToUPawn(ChessPlayer.BLACK)
                    currentPlayer = ChessPlayer.WHITE
                }
                if(blackMoveCount.value ?: 0 >= 2)
                    blackMoveCount.value = blackMoveCount.value?.minus(2)
                else
                {
                    blackMoveCount.postValue(0)
                }
            }
        }
    }

    private fun changeToUPawn(player: ChessPlayer) {
        val pawn = piecesBox.find { it.rank == ChessRank.PAWN && it.player == player }

        if (pawn != null) {
            piecesBox.remove(pawn)
            piecesBox.add(ChessPiece(pawn.col, pawn.row, player, ChessRank.UPAWN, if (player == ChessPlayer.WHITE) R.drawable.u_white_pawn else R.drawable.u_black_pawn))

        }
    }



    fun upgradeQueen(player: ChessPlayer) {
        when (player) {
            ChessPlayer.WHITE -> {
                if (currentPlayer == ChessPlayer.WHITE && whiteMoveCount.value ?: 0 >= 3) {
                    changeToUQueen(ChessPlayer.BLACK)
                    currentPlayer = ChessPlayer.BLACK
                }
                if(whiteMoveCount.value ?: 0 >= 3)
                    whiteMoveCount.value = whiteMoveCount.value?.minus(3)
                else
                {
                    whiteMoveCount.postValue(0)
                }
            }
            ChessPlayer.BLACK -> {
                if (currentPlayer == ChessPlayer.BLACK&&blackMoveCount.value ?: 0 >= 3) {
                    changeToUQueen(ChessPlayer.WHITE)
                    currentPlayer = ChessPlayer.WHITE
                }
                if(blackMoveCount.value ?: 0 >= 3)
                    blackMoveCount.value = blackMoveCount.value?.minus(3)
                else
                {
                    blackMoveCount.postValue(0)
                }
            }
        }
    }

    private fun changeToUQueen(player: ChessPlayer) {
        val queen = piecesBox.find { it.rank == ChessRank.QUEEN && it.player == player }

        if (queen != null) {
            piecesBox.remove(queen)
            piecesBox.add(ChessPiece(queen.col, queen.row, player, ChessRank.UQUEEN, if (player == ChessPlayer.WHITE) R.drawable.u_white_queen else R.drawable.u_black_queen))

        }
    }




    fun upgradeBishop(player: ChessPlayer) {
        when (player) {
            ChessPlayer.WHITE -> {
                if (currentPlayer == ChessPlayer.WHITE && whiteMoveCount.value ?: 0 >= 1) {
                    changeToKnight(ChessPlayer.WHITE)
                    currentPlayer = ChessPlayer.BLACK
                }
                if(whiteMoveCount.value ?: 0 >= 1)
                    whiteMoveCount.value = whiteMoveCount.value?.minus(1)
                else
                {
                    whiteMoveCount.postValue(0)
                }
            }
            ChessPlayer.BLACK -> {
                if (currentPlayer == ChessPlayer.BLACK && blackMoveCount.value ?: 0 >= 1) {
                    changeToKnight(ChessPlayer.BLACK)
                    currentPlayer = ChessPlayer.WHITE
                }
                if(blackMoveCount.value ?: 0 >= 1)
                    blackMoveCount.value = blackMoveCount.value?.minus(1)
                else
                {
                    blackMoveCount.postValue(0)
                }
            }
        }
    }

    private fun changeToKnight(player: ChessPlayer) {
        val bishop = piecesBox.find { it.rank == ChessRank.BISHOP && it.player == player }

        if (bishop != null) {
            piecesBox.remove(bishop)
            piecesBox.add(ChessPiece(bishop.col, bishop.row, player, ChessRank.KNIGHT, if (player == ChessPlayer.WHITE) R.drawable.white_knight else R.drawable.black_knight))

        }
    }





    fun upgradeKnight(player: ChessPlayer) {
        when (player) {
            ChessPlayer.WHITE -> {
                if (currentPlayer == ChessPlayer.WHITE && whiteMoveCount.value ?: 0 >= 1) {
                    changeToBishop(ChessPlayer.WHITE)
                    currentPlayer = ChessPlayer.BLACK
                }
                if(whiteMoveCount.value ?: 0 >= 1)
                    whiteMoveCount.value = whiteMoveCount.value?.minus(1)
                else
                {
                    whiteMoveCount.postValue(0)
                }
            }
            ChessPlayer.BLACK -> {
                if (currentPlayer == ChessPlayer.BLACK&&blackMoveCount.value ?: 0 >= 1) {
                    changeToBishop(ChessPlayer.BLACK)
                    currentPlayer = ChessPlayer.WHITE
                }
                if(blackMoveCount.value ?: 0 >= 1)
                blackMoveCount.value = blackMoveCount.value?.minus(1)
                else
                {
                    blackMoveCount.postValue(0)
                }
            }
        }
    }

    private fun changeToBishop(player: ChessPlayer) {
        val knight = piecesBox.find { it.rank == ChessRank.KNIGHT && it.player == player }

        if (knight != null) {
            piecesBox.remove(knight)
            piecesBox.add(ChessPiece(knight.col, knight.row, player, ChessRank.BISHOP, if (player == ChessPlayer.WHITE) R.drawable.white_bishop else R.drawable.black_bishop))

        }
    }




    fun downgradePawn(player: ChessPlayer) {
        when (player) {
            ChessPlayer.WHITE -> {

                    changeToPawn(ChessPlayer.WHITE)



            }
            ChessPlayer.BLACK -> {

                    changeToPawn(ChessPlayer.BLACK)



            }
        }
    }

    private fun changeToPawn(player: ChessPlayer) {
        val pawn = piecesBox.find { it.rank == ChessRank.UPAWN && it.player == player }

        if (pawn != null) {
            piecesBox.remove(pawn)
            piecesBox.add(ChessPiece(pawn.col, pawn.row, player, ChessRank.PAWN, if (player == ChessPlayer.WHITE) R.drawable.white_pawn else R.drawable.black_pawn))

        }
    }


    fun downgradeRook(player: ChessPlayer) {
        when (player) {
            ChessPlayer.WHITE -> {

                    changeToRook(ChessPlayer.WHITE)


            }
            ChessPlayer.BLACK -> {

                    changeToRook(ChessPlayer.BLACK)



            }
        }
    }

    private fun changeToRook(player: ChessPlayer) {
        val pawn = piecesBox.find { it.rank == ChessRank.UROOK && it.player == player }

        if (pawn != null) {
            piecesBox.remove(pawn)
            piecesBox.add(ChessPiece(pawn.col, pawn.row, player, ChessRank.ROOK, if (player == ChessPlayer.WHITE) R.drawable.white_rook else R.drawable.black_rook))

        }
    }



    fun downgradeQueen(player: ChessPlayer) {
        when (player) {
            ChessPlayer.WHITE -> {

                    changeToQueen(ChessPlayer.WHITE)



            }
            ChessPlayer.BLACK -> {

                    changeToQueen(ChessPlayer.BLACK)



            }
        }
    }

    private fun changeToQueen(player: ChessPlayer) {
        val pawn = piecesBox.find { it.rank == ChessRank.UQUEEN && it.player == player }

        if (pawn != null) {
            piecesBox.remove(pawn)
            piecesBox.add(ChessPiece(pawn.col, pawn.row, player, ChessRank.QUEEN, if (player == ChessPlayer.WHITE) R.drawable.white_queen else R.drawable.black_queen))

        }
    }



    fun downgradeKinght(player: ChessPlayer) {
        when (player) {
            ChessPlayer.WHITE -> {

                    changeToBishop(ChessPlayer.WHITE)



            }
            ChessPlayer.BLACK -> {

                    changeToBishop(ChessPlayer.BLACK)



            }
        }
    }
    fun downgradeBishop(player: ChessPlayer) {
        when (player) {
            ChessPlayer.WHITE -> {

                    changeToKnight(ChessPlayer.WHITE)



            }
            ChessPlayer.BLACK -> {

                    changeToKnight(ChessPlayer.BLACK)



            }
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