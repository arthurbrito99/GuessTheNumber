package com.arthurbn.guessthenumber.ui

import com.arthurbn.guessthenumber.R

// referenciando todos os svgs a cada número correspondente
enum class DisplayRes(val res: Int) {
    Zero(R.drawable.num0),
    One(R.drawable.num1),
    Two(R.drawable.num2),
    Three(R.drawable.num3),
    Four(R.drawable.num4),
    Five(R.drawable.num5),
    Six(R.drawable.num6),
    Seven(R.drawable.num7),
    Eight(R.drawable.num8),
    Nine(R.drawable.num9);

    companion object {
        // utilizando as referências acima, a função baixo é resposável por retornar o svg
        // correspondente ao número que chega por parâmetro
        // o acesso aos svgs existentes é feito no formato de aceeso de array
        fun getByNumber(pos: Int) = values()[pos].res
        fun getDefaultNumber() = values()[0].res
    }
}