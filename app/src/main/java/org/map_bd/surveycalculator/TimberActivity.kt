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
import org.map_bd.surveycalculator.databinding.ActivityTimberBinding
import org.map_bd.surveycalculator.gold.AnarotiActivity
import org.map_bd.surveycalculator.gold.GramActivity
import org.map_bd.surveycalculator.timber.RoundActivity
import org.map_bd.surveycalculator.timber.SizedActivity

@Suppress("DEPRECATION")
class TimberActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.toolbar.title = "Home"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding.sizedId.setOnClickListener {
            val sized = Intent(this, SizedActivity ::class.java);
            startActivity(sized)
        }

        binding.rundId.setOnClickListener {
            val round = Intent(this, RoundActivity ::class.java);
            startActivity(round)
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