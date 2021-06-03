package com.dishant26201.tippy

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {

    private lateinit var etBase: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercent: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvTipDescription: TextView
    private lateinit var etPpl: EditText // Inpur for number of people field
    private lateinit var tvPerPersonAmount: TextView // View for the amount per person field

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etBase = findViewById(R.id.etBase)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipPercent = findViewById(R.id.tvTipPercent)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        etPpl = findViewById(R.id.etPpl)
        tvPerPersonAmount = findViewById(R.id.tvPerPersonAmount)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercent.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)
        tvTipAmount.text = "0.00"
        tvTotalAmount.text = "0.00"
        tvPerPersonAmount.text = "0.00"

        seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tvTipPercent.text = "$progress%"
                updateTipDescription(progress)
                computeTotalAndPerPersonAmount()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        etBase.addTextChangedListener(object : TextWatcher { // Listener for base amount input field
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $$s ")
                computeTotalAndPerPersonAmount()
            }

        })

        etPpl.addTextChangedListener(object : TextWatcher { // Listener for the number of people input field
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s people")
                computeTotalAndPerPersonAmount()
            }
        })
    }

    private fun updateTipDescription(tipPercent: Int) {
        tvTipDescription = findViewById<TextView>(R.id.tvTipDescription)
        seekBarTip = findViewById<SeekBar>(R.id.seekBarTip)
        val tipDescription : String
        when (tipPercent){
            in 0..9 -> tipDescription = "Poor   : ("
            in 10..14 -> tipDescription = "Acceptable   : /"
            in 15..19 -> tipDescription = "Good   : |"
            in 20..24 -> tipDescription = "Great   : )"
            else -> tipDescription = "Sir you're a legend   : \" )"
        }
        tvTipDescription.text = tipDescription
        val tipColor = ArgbEvaluator().evaluate(tipPercent.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this, R.color.colorWorstTip),
            ContextCompat.getColor(this, R.color.colorBestTip)
        ) as Int
        tvTipDescription.setTextColor(tipColor)

    }

    private fun computeTotalAndPerPersonAmount() {
        // Get the value of the base amount, number of people, and tip percentage

        etBase = findViewById(R.id.etBase)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipPercent = findViewById(R.id.tvTipPercent)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        etPpl = findViewById(R.id.etPpl)
        tvPerPersonAmount = findViewById(R.id.tvPerPersonAmount)

        if (etBase.text.toString().isEmpty() || etPpl.text.toString().isEmpty() || etPpl.text.toString().isEmpty()){
            tvTipAmount.text = "0.00"
            tvTotalAmount.text = "0.00"
            tvPerPersonAmount.text = "0.00"
            return
        }
        val baseAmount = etBase.text.toString().toDouble()
        val numPpl = etPpl.text.toString().toInt()
        val tipPercent = seekBarTip.progress
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount
        val amountPerPerson = totalAmount / numPpl
        tvTipAmount.text = "%.2f".format(tipAmount)
        tvTotalAmount.text = "%.2f".format(totalAmount)
        tvPerPersonAmount.text = "%.2f".format(amountPerPerson)
    }
}