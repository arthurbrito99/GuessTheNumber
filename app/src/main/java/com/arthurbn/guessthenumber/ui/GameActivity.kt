package com.arthurbn.guessthenumber.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arthurbn.guessthenumber.logic.GameLogic
import com.arthurbn.guessthenumber.api.GameLogicInterface
import com.arthurbn.guessthenumber.R
import kotlinx.android.synthetic.main.game_activity.*

class GameActivity : AppCompatActivity(), GameLogicInterface {

    private lateinit var gameLogic: GameLogic
    private val maxDigits: Int = 3
    private var displayWidth: Float = 1F
    private var displayHeight: Float = 1F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_activity)

        gameLogic = GameLogic()
        gameLogic.setListener(this)

        // get no tamanho padrão do display
        displayWidth = resources.getDimension(R.dimen.display_width)
        displayHeight = resources.getDimension(R.dimen.display_height)

        // configurando o tamanho do display com o valor da seekBar
        setDisplaysSize(displaySizeBar.progress.toFloat() / displaySizeBar.max)

        submitButton.isEnabled = false
        inputNumberCount.text =
            resources.getString(R.string.digits_counter, 0, maxDigits)

        submitButton.setOnClickListener {
            // controle da exibição do teclado
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(
                currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )

            guessAttempt()
        }
        displaySizeBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                setDisplaysSize(displaySizeBar.progress.toFloat() / displaySizeBar.max)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        redBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                setDisplayColor()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        greenBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                setDisplayColor()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        blueBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                setDisplayColor()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        // alternando entre qual configuração mostrar
        fontSizeButton.setOnClickListener {
            if (colorOption.visibility == View.VISIBLE) {
                colorOption.visibility = View.GONE
            }
            sizeOption.visibility =
                if (sizeOption.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
        palleteButton.setOnClickListener {
            if (sizeOption.visibility == View.VISIBLE) {
                sizeOption.visibility = View.GONE
            }
            colorOption.visibility =
                if (colorOption.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        newGameButton.setOnClickListener { restartGame() }
        inputNumber.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                inputNumberCount.text =
                    resources.getString(R.string.digits_counter, s.length, maxDigits)
                submitButton.isEnabled = s.isNotEmpty()
            }
        })
    }

    // ao reiniciar o jogo, apenas o primeiro display é mostrado com o número 0
    private fun restartGame() {

        inputNumber.text.clear()

        thirdDisplay.visibility = View.GONE
        secondDisplay.visibility = View.GONE
        firstDisplay.visibility = View.VISIBLE
        firstDigit.setImageResource(DisplayRes.getDefaultNumber())

        // chamando a função responsável por fornecer o número que deve ser acertado
        gameLogic.getNumber()
        gameMessage.text = ""
        newGameButton.visibility = View.INVISIBLE

    }

    private fun guessAttempt() {

        val attemptNumber = inputNumber.text.toString().toInt()

        // validando o número da tentativa
        if (!gameLogic.validateNumber(attemptNumber)) {
            val message = resources.getString(
                R.string.invalid_number,
                gameLogic.minNumber,
                gameLogic.maxNumber
            )
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            return
        }

        inputNumber.text.clear()

        configDigits(attemptNumber)

        val message = getString(gameLogic.getResultText(attemptNumber))
        gameMessage.text = message

        if (message == resources.getString(R.string.correct)) {
            newGameButton.visibility = View.VISIBLE
        }
    }

    private fun configDigits(number: Int) {

        var auxNumber = number
        var numberIndex = 0

        // verificando quantos algarismos o número tem e setando no display
        while (auxNumber > 0) {
            // pegando o svg referente ao algarismo
            val res = DisplayRes.getByNumber(auxNumber % 10)

            // trocando o svg do display para o correspondente ao algarismo extraído do número
            // o numberIndex é utilizado para definir em qual posição o display deve ser alterado
            when (numberIndex) {
                0 -> firstDigit.setImageResource(res)
                1 -> secondDigit.setImageResource(res)
                2 -> thirdDigit.setImageResource(res)
            }

            // percorrendo cada algarismo("casa do número") do numero e atualizando o index
            auxNumber /= 10
            numberIndex++
        }

        // verificando quantos algarismos tem o número para ativar a quantidade correta de displays
        when {
            number < 10 -> {
                firstDisplay.visibility = View.VISIBLE
                secondDisplay.visibility = View.GONE
                thirdDisplay.visibility = View.GONE
            }
            number < 100 -> {
                firstDisplay.visibility = View.VISIBLE
                secondDisplay.visibility = View.VISIBLE
                thirdDisplay.visibility = View.GONE
            }
            else -> {
                firstDisplay.visibility = View.VISIBLE
                secondDisplay.visibility = View.VISIBLE
                thirdDisplay.visibility = View.VISIBLE
            }
        }
    }

    fun setDisplaysSize(percentage: Float) {
        // calculando o tamanho do display proporcionalmente entre 50% e 150% do tamanho padrão
        val width = (displayWidth * (percentage + 0.5)).toInt()
        val height = (displayHeight * (percentage + 0.5)).toInt()

        val displayList = listOf<FrameLayout>(firstDisplay, secondDisplay, thirdDisplay)

        // Com os valores definidos, basta aplicá-los em cada display
        for (display in displayList) {
            val params = display.layoutParams
            params.width = width
            params.height = height
            display.layoutParams = params
        }
    }

    fun setDisplayColor() {
        // cada SeekBar vai de 0 a 255 e representa um valor no RGB. Com a junção dos valores
        // é gerado o código da cor que é aplicada nos displays através da função abaixo
        val color = Color.argb(255, redBar.progress, greenBar.progress, blueBar.progress)
        // aplicando a cor gerada pelo valores das barras
        firstDigit.setColorFilter(color)
        secondDigit.setColorFilter(color)
        thirdDigit.setColorFilter(color)
    }

    // reescrevendo o comportamento da função de erro da GameInterface para que ela exiba no app
    // a situação do erro
    override fun onRequestError(statusCode: Int) {
        // setando o error code no display
        configDigits(gameLogic.statusCode)
        gameMessage.text = resources.getString(R.string.error)
        newGameButton.visibility = View.VISIBLE
    }
}