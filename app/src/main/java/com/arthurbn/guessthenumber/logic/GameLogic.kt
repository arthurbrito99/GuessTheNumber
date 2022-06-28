package com.arthurbn.guessthenumber.logic

import com.arthurbn.guessthenumber.api.GameLogicInterface
import com.arthurbn.guessthenumber.R
import com.arthurbn.guessthenumber.api.RetrofitConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class GameLogic {

    private var listenerReference: GameLogicInterface? = null

    private var targetNumber: Int? = null

    var statusCode: Int = -1

    val minNumber: Int = 1
    val maxNumber: Int = 300

    private var gameFinished: Boolean = false

    fun validateNumber(number: Int) = number in minNumber..maxNumber

    fun getResultText(attemptNumber: Int): Int {
        // tratamento da tentativa de acerto feita pelo usuário
        return when {
            attemptNumber < targetNumber!! -> {
                gameFinished = false
                R.string.greater_than
            }
            attemptNumber > targetNumber!! -> {
                gameFinished = false
                R.string.less_than
            }
            else -> {
                gameFinished = true
                R.string.correct
            }
        }
    }

    fun getNumber() {

        // Acessando a thread de background para o disparo da requisição
        CoroutineScope(IO).launch {
            val service = RetrofitConfig.getApiService()
            // chamada de disparo da requisição com os parâmetros setados anteriormente
            try {
                val response = service.getNumber(minNumber, maxNumber)
                targetNumber = response.number
            } catch (e: HttpException) {
                statusCode = e.code()
                // retornando para a thread de UI para informar o erro
                withContext(Main) {
                    listenerReference?.onRequestError(e.code())
                }
            }
        }
    }

    fun setListener(listener: GameLogicInterface?) {
        this.listenerReference = listener
    }


}