package com.example.simplecalculator

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.math.BigDecimal

class LogicService : Service() {

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService() : LogicService {
            return this@LogicService
        }
    }

    override fun onBind(intent: Intent): IBinder = binder

    fun add(number1: BigDecimal, number2: BigDecimal) = number1 + number2

    fun subtract(number1: BigDecimal, number2: BigDecimal) = number1 - number2

    fun divide(number1: BigDecimal, number2: BigDecimal) = number1
        .divide(number2, 10, BigDecimal.ROUND_HALF_UP)
        .stripTrailingZeros()

    fun multiply(number1: BigDecimal, number2: BigDecimal) = number1 * number2
}
