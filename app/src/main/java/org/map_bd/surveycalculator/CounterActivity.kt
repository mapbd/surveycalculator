package org.map_bd.surveycalculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.map_bd.surveycalculator.databinding.ActivityCounterBinding

class CounterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCounterBinding


    private var counter = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityCounterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.toolbar.title = "Home"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val counterText = findViewById<TextView>(R.id.counterText)
        val btnIncrement = findViewById<Button>(R.id.btnIncrement)
        val btnDecrement = findViewById<Button>(R.id.btnDecrement)
        val btnReset = findViewById<Button>(R.id.btnReset)

        btnIncrement.setOnClickListener {
            counter++
            counterText.text = counter.toString()
        }

        btnDecrement.setOnClickListener {
            counter--
            counterText.text = counter.toString()
        }

        btnReset.setOnClickListener {
            counter = 0
            counterText.text = counter.toString()
        }
    }


}