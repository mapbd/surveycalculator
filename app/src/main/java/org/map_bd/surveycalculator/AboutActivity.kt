package org.map_bd.surveycalculator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.map_bd.surveycalculator.databinding.ActivityAboutBinding
import org.map_bd.surveycalculator.databinding.ActivitySettingBinding

@Suppress("DEPRECATION")
class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = "About"
        setSupportActionBar(binding.toolbar)
     //   supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.aboutId -> {
                Toast.makeText(this,"About",Toast.LENGTH_LONG).show()
            }
            R.id.settingId -> {
                val settings = Intent(this,SettingActivity::class.java);
                startActivity(settings)
                onBackPressed()
                return true
            }
            R.id.basicCal ->{
                val calculator = Intent(this,BasicalculatorActivity::class.java);
                startActivity(calculator)
                onBackPressed()
                return true
            }
        }
        return true
    }



}