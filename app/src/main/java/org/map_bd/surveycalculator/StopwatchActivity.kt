package org.map_bd.surveycalculator

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import org.map_bd.surveycalculator.databinding.ActivityStopwatchBinding

class StopwatchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStopwatchBinding

    private var isRunning = false

    private var timerSecomds = 0

    private val handler = Handler(Looper.getMainLooper())

    private val runnable = object : Runnable {
        override fun run() {
            timerSecomds++
            val hours = timerSecomds / 3600
            val minutes = (timerSecomds % 3600) / 60
            val secounds = timerSecomds % 60

            val time = String.format("%02d:%02d:%02d", hours, minutes, secounds)
            binding.timerText.text = time

            handler.postDelayed(this,1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityStopwatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.toolbar.title = "Home"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        binding.startId.setOnClickListener {
            startTimer()
        }

        binding.stopId.setOnClickListener {
            stopTimer()
        }

        binding.resetId.setOnClickListener {
            resetTimer()
        }



    }

    private fun startTimer(){
        if (!isRunning){
            handler.postDelayed(runnable,1000)
            isRunning = true

            binding.startId.isEnabled = false
            binding.stopId.isEnabled = true
            binding.resetId.isEnabled = true
        }
    }

    private fun stopTimer(){
        if (isRunning){
            handler.removeCallbacks(runnable)
            isRunning = false

            binding.startId.isEnabled = true
            binding.startId.text = "Resume"
            binding.stopId.isEnabled = false
            binding.resetId.isEnabled = true
        }
    }

    private fun resetTimer(){
        stopTimer()

        timerSecomds = 0
        binding.timerText.text = "00:00:00"

        binding.startId.isEnabled = true
        binding.startId.text = "Start"
        binding.resetId.isEnabled = false

    }
}