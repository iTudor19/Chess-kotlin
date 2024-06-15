package com.example.chess_prototype
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.min

class ChessView(context: Context?, attrs:AttributeSet) :View(context,attrs) {
    private final val scaleFactor = 1.0f
    private final var originX: Float = 20f
    private final var originY: Float = 200f
    private final var cellSide: Float = 130f
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
    private val bitmaps = mutableMapOf<Int, Bitmap>()
    private val paint = Paint()

    private var movingPieceBitmap: Bitmap? = null
    private var movingPiece:ChessPiece? = null
    private var fromCol: Int = -1
    private var fromRow: Int = -1
    private var movingPieceX = -1f
    private var movingPieceY = -1f


    var chessDelegate: ChessDelegate? = null

    init {
        loadBitmaps()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val smaller= min(widthMeasureSpec,heightMeasureSpec)
        setMeasuredDimension(smaller,smaller)
    }

    override fun onDraw(canvas: Canvas) {

        canvas ?: return
        val chessBoardSide = min(width, height) * scaleFactor
        cellSide = chessBoardSide / 8f
        originX = (width - chessBoardSide) / 2f
        originY = (height - chessBoardSide) / 2f

        drawChessboard(canvas)
        drawPieces(canvas)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                fromCol = ((event.x - originX) / cellSide).toInt()
                fromRow = 7 - ((event.y - originY) / cellSide).toInt()
                chessDelegate?.pieceAt(fromCol,fromRow)?.let{
                    if (it.player == ChessModel.currentPlayer) {
                        movingPiece = it
                        movingPieceBitmap = bitmaps[it.resID]
                    }
                }


            }

            MotionEvent.ACTION_MOVE -> {
                movingPieceX = event.x
                movingPieceY = event.y
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                val col = ((event.x - originX) / cellSide).toInt()
                val row = 7 - ((event.y - originY) / cellSide).toInt()
                chessDelegate?.movePiece(fromCol, fromRow, col, row)
                movingPiece = null
                movingPieceBitmap= null

            }
        }
        return true
    }



    private fun drawPieces(canvas: Canvas?) {

        for (row in 0..7) {
            for (col in 0..7) {

                chessDelegate?.pieceAt(col, row)?.let {
                    if(it!=movingPiece) {
                        drawPieceAt(canvas, col, row, it.resID)
                    }
                }

            }
        }
        movingPieceBitmap?.let{
            canvas?.drawBitmap(
                it,
                null,
                RectF(movingPieceX-cellSide/2, movingPieceY-cellSide/2, movingPieceX + cellSide/2, movingPieceY + cellSide/2),
                paint
            )
        }

    }

    private fun drawPieceAt(canvas: Canvas?, col: Int, row: Int, resID: Int, ) {
        val bitmap = bitmaps[resID]!!
        canvas?.drawBitmap(
            bitmap,
            null,
            RectF(
                originX + col * cellSide,
                originY + (7 - row) * cellSide,
                originX + (col + 1) * cellSide,
                originY + (7 - row + 1) * cellSide
            ),
            paint
        )
    }

    private fun loadBitmaps() {
        imgResIDs.forEach {
            bitmaps[it] = BitmapFactory.decodeResource(resources, it)
        }
    }

    private fun drawChessboard(canvas: Canvas?) {
        val paint = Paint()
        for (row in 0..7) {
            for (col in 0..7) {
                drawSquareAt(canvas, row, col, (col + row) % 2 == 0)

            }
        }
    }


    private fun drawSquareAt(canvas: Canvas?, col: Int, row: Int, isDark: Boolean) {
        paint.color = if (isDark) Color.LTGRAY else Color.BLUE
        canvas?.drawRect(
            originX + col * cellSide,
            originY + row * cellSide,
            originX + (col + 1) * cellSide,
            originY + (row + 1) * cellSide, paint
        )
    }
}
