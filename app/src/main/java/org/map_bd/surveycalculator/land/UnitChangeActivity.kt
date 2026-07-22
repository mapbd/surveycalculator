package org.map_bd.surveycalculator.land

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.map_bd.surveycalculator.BasicalculatorActivity
import org.map_bd.surveycalculator.BdlandActivity
import org.map_bd.surveycalculator.CompassActivity
import org.map_bd.surveycalculator.R
import org.map_bd.surveycalculator.databinding.ActivityUnitchangeBinding

class UnitChangeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUnitchangeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUnitchangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.bdlandId.setOnClickListener {
            val bdland = Intent(this, BdlandActivity::class.java);
            startActivity(bdland)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_new, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                finish()
                return true
            }
            R.id.basicCal ->{
                val calculator = Intent(this, BasicalculatorActivity::class.java);
                startActivity(calculator)
            }
            R.id.compassId ->{
                val compass = Intent(this, CompassActivity::class.java);
                startActivity(compass)
            }
        }
        return true
    }


}