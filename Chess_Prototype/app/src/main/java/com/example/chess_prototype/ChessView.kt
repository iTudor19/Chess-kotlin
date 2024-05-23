package com.example.chess_prototype
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class ChessView(context: Context?, attrs:AttributeSet) :View(context,attrs)
{
    private final val originX: Float = 20f
    private final val originY: Float = 200f
    private final val cellSide: Float = 130f
    private final val imgResIDs = setOf(
        R.drawable.white_rook,
        R.drawable.white_pawn,
        R.drawable.white_bishop,
        R.drawable.white_knight,
        R.drawable.white_queen,
        R.drawable.white_king,
        R.drawable.black_rook,
        R.drawable.black_pawn,
        R.drawable.black_bishop,
        R.drawable.black_knight,
        R.drawable.black_queen,
        R.drawable.black_king,

    )
    private final val bitmaps= mutableMapOf<Int, Bitmap>()
    private final val paint = Paint()

    init{
        loadBitmaps()
    }

    override fun onDraw(canvas: Canvas)
    {
        drawChessboard(canvas)
        drawPieces(canvas)
    }

    private fun loadBitmaps(){
        imgResIDs.forEach{
            bitmaps[it] = BitmapFactory.decodeResource(resources,it)
        }
    }

    private fun drawPieceAt(canvas:Canvas?,col:Int, row:Int, resID: Int,)
    {
        val bitmap = bitmaps[resID]!!
        canvas?.drawBitmap(bitmap,null, RectF(originX + col* cellSide,originY + (7-row)* cellSide,originX + (col+1)* cellSide,originY + (7-row+1)* cellSide),paint)
    }


    private fun drawChessboard(canvas:Canvas?)
    {
        val paint= Paint()
        for(j in 0..7)
        {
            for(i in 0..7)
            {
                paint.color = if((i+j)%2==0)Color.LTGRAY else Color.BLUE
                canvas?.drawRect(
                    originX + i * cellSide,
                    originY + j * cellSide,
                    originX + (i + 1) * cellSide,
                    originY +(j + 1) * cellSide,paint)

            }
        }
    }

    private fun drawPieces(canvas:Canvas?)
    {
        val chessModel = ChessModel()
        chessModel.reset()

        for(row in 0..7){
            for(col in 0..7)
            {
                val piece = chessModel.pieceAt(col,row)
                if(piece!=null) {
                        drawPieceAt(canvas,col,row,piece.resID)
                    }
                }
            }
        }
//        drawPieceAt(canvas,0,1,R.drawable.white_pawn)
//        drawPieceAt(canvas,1,1,R.drawable.white_pawn)
//        drawPieceAt(canvas,2,1,R.drawable.white_pawn)
//        drawPieceAt(canvas,3,1,R.drawable.white_pawn)
//        drawPieceAt(canvas,4,1,R.drawable.white_pawn)
//        drawPieceAt(canvas,5,1,R.drawable.white_pawn)
//        drawPieceAt(canvas,6,1,R.drawable.white_pawn)
//        drawPieceAt(canvas,7,1,R.drawable.white_pawn)
//
//        drawPieceAt(canvas,0,0,R.drawable.white_rook)
//        drawPieceAt(canvas,7,0,R.drawable.white_rook)
//        drawPieceAt(canvas,1,0,R.drawable.white_knight)
//        drawPieceAt(canvas,6,0,R.drawable.white_knight)
//        drawPieceAt(canvas,2,0,R.drawable.white_bishop)
//        drawPieceAt(canvas,5,0,R.drawable.white_bishop)
//        drawPieceAt(canvas,3,0,R.drawable.white_queen)
//        drawPieceAt(canvas,4,0,R.drawable.white_king)
//
//
//
//
//        drawPieceAt(canvas,0,6,R.drawable.black_pawn)
//        drawPieceAt(canvas,1,6,R.drawable.black_pawn)
//        drawPieceAt(canvas,2,6,R.drawable.black_pawn)
//        drawPieceAt(canvas,3,6,R.drawable.black_pawn)
//        drawPieceAt(canvas,4,6,R.drawable.black_pawn)
//        drawPieceAt(canvas,5,6,R.drawable.black_pawn)
//        drawPieceAt(canvas,6,6,R.drawable.black_pawn)
//        drawPieceAt(canvas,7,6,R.drawable.black_pawn)
//
//        drawPieceAt(canvas,0,7,R.drawable.black_rook)
//        drawPieceAt(canvas,7,7,R.drawable.black_rook)
//        drawPieceAt(canvas,1,7,R.drawable.black_knight)
//        drawPieceAt(canvas,6,7,R.drawable.black_knight)
//        drawPieceAt(canvas,2,7,R.drawable.black_bishop)
//        drawPieceAt(canvas,5,7,R.drawable.black_bishop)
//        drawPieceAt(canvas,3,7,R.drawable.black_queen)
//        drawPieceAt(canvas,4,7,R.drawable.black_king)
    }





















//for(j in 0..3) {
//    for (i in 0..3) {
//
//
//        paint.color = Color.LTGRAY
//
//
//        canvas?.drawRect(
//            originX + 2 * i * cellSide,
//            originY + 2 * j * cellSide,
//            originX + (2 * i + 1) * cellSide,
//            originY +(2 * j + 1) * cellSide,
//            paint
//        )
//
//        canvas?.drawRect(
//            originX + (2 * i+1) * cellSide,
//            originY + (2 * j+1) * cellSide,
//            originX + (2 * i + 2) * cellSide,
//            originY +(2 * j + 2) *cellSide,
//            paint
//        )
//
//
//        paint.color = Color.BLUE
//
//
//        canvas?.drawRect(
//            originX + (2 * i+1)  * cellSide,
//            originY + 2*j*cellSide,
//            originX + (2 * i + 2) * cellSide,
//            originY +(2 * j + 1) * cellSide,
//            paint
//        )
//
//        canvas?.drawRect(
//            originX + (2 * i) * cellSide,
//            originY + (2 * j+1) * cellSide,
//            originX + (2 * i + 1) * cellSide,
//            originY + (2 * j + 2) * cellSide,
//            paint
//        )
//    }
//
//}