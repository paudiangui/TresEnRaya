package com.example.tresenraya

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity(), View.OnClickListener{

    // algoritmo maquina MINIMAX
    object GFG {
        var player = 'X'
        var bot = 'O'


        //Nos devulve true si queda alguna casilla libre y flase sino
        fun isMovesLeft(board: Array<CharArray>): Boolean {
            var casillaLibre:Boolean
            for (i in 0..2) {
                for (j in 0..2) {
                    if (board[i][j] == '_')
                        casillaLibre=true
                }
            }
            casillaLibre=false
            return casillaLibre
        }

        // esta funcion devuelve 10 si gana el jugador 1, -10 si pierde el jugador 1 o 0 si ninguno gana
        fun evaluate(board: Array<CharArray>): Int {
            // mira si hay victoria posible en las filas
            for (row in 0..2) {
                if (board[row][0] == board[row][1]
                    && board[row][1] == board[row][2]
                ) {
                    if (board[row][0] == player) {
                        return +10
                    } else if (board[row][0] == bot) {
                        return -10
                    }
                }
            }

            // check de posible victoria en las columnas
            for (col in 0..2) {
                if (board[0][col] == board[1][col]
                    && board[1][col] == board[2][col]
                ) {
                    if (board[0][col] == player) {
                        return +10
                    } else if (board[0][col] == bot) {
                        return -10
                    }
                }
            }

            // check de posible victoria en la diagonal
            if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
                if (board[0][0] == player) {
                    return +10
                } else if (board[0][0] == bot) {
                    return -10
                }
            }
            if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
                if (board[0][2] == player) {
                    return +10
                } else if (board[0][2] == bot) {
                    return -10
                }
            }

            // si no gana nadie devuelve 0
            return 0
        }

        // funcion recursiva
        fun minimax(
            board: Array<CharArray>,
            depth: Int, isMax: Boolean
        ): Int {
            val score = evaluate(board)


            if (score == 10) {
                return score
            }

            if (score == -10) {
                return score
            }

            if (isMovesLeft(board) == false) {
                return 0
            }

            if (isMax) {
                var best = -1000


                for (i in 0..2) {
                    for (j in 0..2) {

                        if (board[i][j] == '_') {

                            board[i][j] = player

                            best = Math.max(
                                best, minimax(
                                    board,
                                    depth + 1, !isMax
                                )
                            )

                            board[i][j] = '_'
                        }
                    }
                }
                return best
            } // Si esta minimizado, muevete
            else {
                var best = 1000

                // Recorre todas las casillas
                for (i in 0..2) {
                    for (j in 0..2) {
                        // Check si la casilla esta vacia
                        if (board[i][j] == '_') {
                            // Hace el movimiento
                            board[i][j] = bot

                            // Llama a la funcion minimax
                            // the minimum value
                            best = Math.min(
                                best, minimax(
                                    board,
                                    depth + 1, !isMax
                                )
                            )

                            // Deshace el movimiento
                            board[i][j] = '_'
                        }
                    }
                }
                return best
            }
        }

        // devuelve la mejor posicion posible
        fun findBestMove(board: Array<CharArray>): Move {
            var bestVal = -1000
            val bestMove = Move()
            bestMove.row = -1
            bestMove.col = -1

            // Recore todas las celdas
            // evalua la funcion minimax para todasa las celdas vacias.
            // y devuelve la celda con un valor optimo.
            for (i in 0..2) {
                for (j in 0..2) {
                    // Mira si la celda esta vacia
                    if (board[i][j] == '_') {
                        // Hace le movimiento
                        board[i][j] = player

                        val moveVal = minimax(board, 0, false)

                        // Deshace el movimiento
                        board[i][j] = '_'

                        // Si el valor del movimiento actual es mayor
                        // que el valor the best, actualiza best
                        if (moveVal > bestVal) {
                            bestMove.row = i
                            bestMove.col = j
                            bestVal = moveVal
                        }
                    }
                }
            }
            System.out.printf(
                "The value of the best Move "
                        + "is : %d\n\n", bestVal
            )
            return bestMove
        }

        class Move() {
            var row = 0
            var col = 0
        }
    }
    // FIN MINIMAX

    fun fieldToBoard(): Array<CharArray> {
        val board = arrayOf(
            charArrayOf('_','_', '_'
            ),
            charArrayOf('_','_','_'
            ),
            charArrayOf('_','_', '_'
            )
        )

        val field = retField()
        for (i in 0 until field.size) {
            for (j in 0 until field.get(0).size) {
                if (field[i][j] == "X"){
                    board[i][j] = 'X'
                }else if (field[i][j] == "O"){
                    board[i][j] = 'O'
                }
            }
        }
        return board
    }

    fun retField(): Array<Array<String?>> {
        val field = Array(3) {
            arrayOfNulls<String>(
                3
            )
        }
        for (i in 0..2) {
            for (j in 0..2) {
                field[i][j] = buttons[i][j]!!.text.toString()
            }
        }
        return field
    }

    //JUEGO


    private val buttons = Array(3) {
        arrayOfNulls<Button>(
            3
        )
    }

    private var player1Turn = true

    private var roundCount = 1

    private var player1Points = 0
    private var player2Points = 0

    private var textViewPlayer1: TextView? = null
    private var textViewPlayer2: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewPlayer1 = findViewById(R.id.text_view_p1)
        textViewPlayer2 = findViewById(R.id.text_view_p2)

        for (i in 0..2) {
            for (j in 0..2) {
                val buttonID = "btn$i$j"
                val resID = resources.getIdentifier(buttonID, "id", packageName)
                buttons[i][j] = findViewById(resID)
                buttons[i][j]?.setOnClickListener(this)
            }
        }

        val buttonReset = findViewById<Button>(R.id.btnReset)
        buttonReset.setOnClickListener { resetGame() }

        var j = GFG.findBestMove(fieldToBoard()).col
        var i = GFG.findBestMove(fieldToBoard()).row
        buttons[i][j]?.setText("X")

    }

    override fun onClick(v: View) {
        if ((v as Button).text.toString() != "") {
            return
        }
        if (player1Turn) {
            v.setText("O")
            roundCount++
        }

        if (GFG.evaluate(fieldToBoard()) == 10) {
            botWins()
        } else if (roundCount == 9) {
            draw()
        } else {
            player1Turn = !player1Turn
        }
        if (!player1Turn){
            var j = GFG.findBestMove(fieldToBoard()).col
            var i = GFG.findBestMove(fieldToBoard()).row
            buttons[i][j]?.setText("X")
            roundCount++

        }
        if (GFG.evaluate(fieldToBoard()) == 10) {
            botWins()
        }else if (roundCount == 9) {
            draw()
        } else {
            player1Turn = !player1Turn
        }

        println("asdf"+roundCount)
    }

    private fun playerWins() {
        player1Points++
        Toast.makeText(this, "PLAYER 1 WINS!", Toast.LENGTH_SHORT).show()
        updatePointsText()
        resetBoard()
    }

    private fun botWins() {
        player2Points++
        Toast.makeText(this, "BOT WINS!", Toast.LENGTH_SHORT).show()
        updatePointsText()
        resetBoard()
    }

    private fun draw() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show()
        resetBoard()
    }

    private fun updatePointsText() {
        textViewPlayer1!!.text = "Player 1: $player1Points"
        textViewPlayer2!!.text = "BOT: $player2Points"
    }

    private fun resetBoard() {
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j]!!.text = ""
            }
        }
        roundCount = 0
        player1Turn = true
    }

    private fun resetGame() {
        player1Points = 0
        player2Points = 0
        updatePointsText()
        resetBoard()
    }


}