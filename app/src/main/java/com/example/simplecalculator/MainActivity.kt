package com.example.simplecalculator

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.math.BigDecimal

class MainActivity : AppCompatActivity() {

    private var logicService: LogicService? = null

    private val logicConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder: LogicService.LocalBinder = service as LogicService.LocalBinder
            logicService = binder.getService()
            Toast.makeText(this@MainActivity, "Logic Service Connected!", Toast.LENGTH_SHORT).show()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            logicService = null
            Toast.makeText(this@MainActivity, "Logic Service Disconnected!", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        plus_button.setOnClickListener {
            checkEmpty {
                applyToResult(number1.bigDecimal(), number2.bigDecimal()) {
                        n1, n2 -> logicService?.add(n1, n2)
                }
            }
        }

        minus_button.setOnClickListener {
            checkEmpty {
                applyToResult(number1.bigDecimal(), number2.bigDecimal()) {
                        n1, n2 -> logicService?.subtract(n1, n2)
                }
            }

        }

        divide_button.setOnClickListener {
            checkEmpty {
                val num1 = number1.bigDecimal()
                val num2 = number2.bigDecimal()
                if (num1 != BigDecimal.ZERO) {
                    applyToResult(num1, num2) { n1, n2 -> logicService?.divide(n1, n2) }
                } else {
                    Toast.makeText(this@MainActivity, "Division by 0!", Toast.LENGTH_SHORT).show()
                }

            }

        }

        multiply_button.setOnClickListener {
            checkEmpty {
                applyToResult(number1.bigDecimal(), number2.bigDecimal()) {
                        n1, n2 -> logicService?.multiply(n1, n2)
                }
            }
        }

        calc_pi_button.setOnClickListener {
            progressBar.progress = 0
            PiComputeTask().execute(Pair(progressBar, number1))
        }
    }

    private fun checkEmpty(onSuccess: () -> Unit) {
        if (number1.text.isNotBlank() && number2.text.isNotBlank()) {
            onSuccess()
        } else {
            Toast.makeText(this@MainActivity, "Fill input!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun applyToResult(number1: BigDecimal, number2: BigDecimal,
                              reducer: (BigDecimal, BigDecimal) -> BigDecimal?) {
        result.text = reducer.invoke(number1, number2)?.toEditable()
    }

    override fun onStart() {
        super.onStart()
        if (logicService == null) {
            bindService(Intent(this@MainActivity, LogicService::class.java),
                logicConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (logicService != null) {
            unbindService(logicConnection)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
