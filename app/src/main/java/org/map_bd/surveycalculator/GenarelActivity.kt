package org.map_bd.surveycalculator

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.map_bd.surveycalculator.databinding.ActivityGenarelBinding
import org.map_bd.surveycalculator.databinding.ActivityMainBinding

@Suppress("DEPRECATION")
class GenarelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGenarelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGenarelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.toolbar.title = "Home"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        binding.mapId.setOnClickListener{
            val map = Intent(this,MapActivity::class.java);
            startActivity(map)
        }


        binding.cameraId.setOnClickListener{
            val camera = Intent(this,CameraActivity::class.java);
            startActivity(camera)
        }
        binding.audioId.setOnClickListener{
            val audio = Intent(this, AudioActivity::class.java);
//            val audio = Intent(this, VoiceRecoredActivity::class.java);
            startActivity(audio)
        }

        binding.stopwatchId.setOnClickListener {
            val stopwatch = Intent(this, StopwatchActivity::class.java);
            startActivity(stopwatch)
        }

        binding.noteId.setOnClickListener {
            val note = Intent(this, NoteActivity::class.java);
            startActivity(note)
        }

        binding.uconverterId.setOnClickListener {
            val uconverter = Intent(this, UconverterActivity::class.java);
            startActivity(uconverter)
        }

        binding.counterId.setOnClickListener {
            val counter = Intent(this, CounterActivity::class.java);
            startActivity(counter)
        }

        binding.paintId.setOnClickListener {
            val paint = Intent(this, PaintActivity::class.java);
            startActivity(paint)
        }

        binding.formId.setOnClickListener {
            val form = Intent(this, FormActivity::class.java);
            startActivity(form)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                finish()
                return true
            }
            R.id.settingId -> {
                val settings = Intent(this,SettingActivity::class.java);
                startActivity(settings)
            }
            R.id.aboutId -> {
                val about = Intent(this,AboutActivity::class.java);
                startActivity(about)
            }
            R.id.basicCal ->{
                val calculator = Intent(this,BasicalculatorActivity::class.java);
                startActivity(calculator)
            }
            R.id.compassId ->{
                val compass = Intent(this,CompassActivity::class.java);
                startActivity(compass)
            }
        }
        return true
    }


}