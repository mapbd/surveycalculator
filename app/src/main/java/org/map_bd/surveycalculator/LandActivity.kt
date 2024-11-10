package org.map_bd.surveycalculator

import LanguageManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import org.map_bd.surveycalculator.databinding.ActivityLandBinding
import org.map_bd.surveycalculator.land.CircleActivity
import org.map_bd.surveycalculator.land.OblongActivity
import org.map_bd.surveycalculator.land.SquareActivity


class LandActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandBinding
    private lateinit var fragmentManager: FragmentManager
    private lateinit var languageManager: LanguageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.toolbar.title = "Land"
        setSupportActionBar(binding.toolbar)

        fragmentManager = supportFragmentManager
        languageManager = LanguageManager(this)

        binding.oblongId.setOnClickListener{
            val oblong = Intent(this, OblongActivity ::class.java);
            startActivity(oblong)
            // onBackPressed()
        }

        binding.squareId.setOnClickListener{
            val square = Intent(this, SquareActivity ::class.java);
            startActivity(square)
            // onBackPressed()
        }

        binding.circleId.setOnClickListener{
            val circle = Intent(this, CircleActivity ::class.java);
            startActivity(circle)
            // onBackPressed()
        }

    }
}