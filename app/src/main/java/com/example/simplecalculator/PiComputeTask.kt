package com.example.simplecalculator

import android.os.AsyncTask
import android.text.Editable
import android.widget.EditText
import android.widget.ProgressBar


class PiComputeTask : AsyncTask<Pair<ProgressBar, EditText>, Int, Double>() {

    companion object {
        val ITERATIONS: Int = 10_000_000
    }

    private lateinit var progressBar: ProgressBar

    private lateinit var resultText: EditText

    override fun doInBackground(vararg params: Pair<ProgressBar, EditText>?): Double {
        progressBar = params[0]?.first as ProgressBar
        resultText = params[0]?.second as EditText
        val progressDiv = ITERATIONS / 100
        var inCircle = 0
        for (i in 0 until ITERATIONS) {
            val x = Math.random()
            val y = Math.random()
            if (x * x + y * y <= 1) {
                inCircle++
            }
            if (i % progressDiv == 0) {
                publishProgress(i / progressDiv)
            }
        }
        return 4 * inCircle.toDouble() / ITERATIONS
    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        progressBar.progress = values[0]!!
    }

    override fun onPostExecute(result: Double?) {
        super.onPostExecute(result)
        resultText.text = Editable.Factory.getInstance().newEditable(result?.toString())
    }
}