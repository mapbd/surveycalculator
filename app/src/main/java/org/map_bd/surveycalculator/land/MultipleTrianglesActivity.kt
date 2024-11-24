package org.map_bd.surveycalculator.land

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import org.map_bd.surveycalculator.BasicalculatorActivity
import org.map_bd.surveycalculator.CompassActivity
import org.map_bd.surveycalculator.R
import org.map_bd.surveycalculator.databinding.ActivityMultipleTrianglesBinding

class MultipleTrianglesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMultipleTrianglesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMultipleTrianglesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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