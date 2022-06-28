package com.arthurbn.guessthenumber.api

// Interface com a declaração da função que chamada quando a requisição retorna erro
interface GameLogicInterface {
    fun onRequestError(statusCode: Int)
}