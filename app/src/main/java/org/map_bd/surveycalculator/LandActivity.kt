package org.map_bd.surveycalculator

import LanguageManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import org.map_bd.surveycalculator.databinding.ActivityLandBinding
import org.map_bd.surveycalculator.land.AccurateLandCalculationActivity
import org.map_bd.surveycalculator.land.CcircleActivity
import org.map_bd.surveycalculator.land.CircleActivity
import org.map_bd.surveycalculator.land.CosineRuleActivity
import org.map_bd.surveycalculator.land.DistributionoflandActivity
import org.map_bd.surveycalculator.land.EllipseActivity
import org.map_bd.surveycalculator.land.HeronsRuleActivity
import org.map_bd.surveycalculator.land.KhatianCalculatorActivity
import org.map_bd.surveycalculator.land.MultipleTrianglesActivity
import org.map_bd.surveycalculator.land.OblongActivity
import org.map_bd.surveycalculator.land.ParabolaActivity
import org.map_bd.surveycalculator.land.ParellelogramActivity
import org.map_bd.surveycalculator.land.RighttriangleActivity
import org.map_bd.surveycalculator.land.SquareActivity
import org.map_bd.surveycalculator.land.TrapeziumActivity
import org.map_bd.surveycalculator.land.UnitChangeActivity


@Suppress("DEPRECATION")
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
        binding.parellogramId.setOnClickListener{
            val parellogram = Intent(this, ParellelogramActivity ::class.java);
            startActivity(parellogram)
            // onBackPressed()
        }

        binding.circleId.setOnClickListener{
            val circle = Intent(this, CircleActivity ::class.java);
            startActivity(circle)
            // onBackPressed()
        }


        binding.trapeziumId.setOnClickListener{
            val trapezium = Intent(this, TrapeziumActivity ::class.java);
            startActivity(trapezium)
            // onBackPressed()
        }
        binding.cosineId.setOnClickListener{
            val cosine = Intent(this, CosineRuleActivity ::class.java);
            startActivity(cosine)
            // onBackPressed()
        }
        binding.circleId.setOnClickListener{
            val circle = Intent(this, CircleActivity ::class.java);
            startActivity(circle)
            // onBackPressed()
        }
        binding.heronsId.setOnClickListener{
            val herons = Intent(this, HeronsRuleActivity ::class.java);
            startActivity(herons)
            // onBackPressed()
        }
        binding.circleId.setOnClickListener{
            val circle = Intent(this, CircleActivity ::class.java);
            startActivity(circle)
            // onBackPressed()
        }
        binding.righttriangleId.setOnClickListener{
            val right = Intent(this, RighttriangleActivity ::class.java);
            startActivity(right)
            // onBackPressed()
        }
        binding.porabelaId.setOnClickListener{
            val parabola = Intent(this, ParabolaActivity ::class.java);
            startActivity(parabola)
            // onBackPressed()
        }
        binding.ccircleId.setOnClickListener{
            val ccircle = Intent(this, CcircleActivity ::class.java);
            startActivity(ccircle)
            // onBackPressed()
        }
        binding.ellipseId.setOnClickListener{
            val ellipse = Intent(this, EllipseActivity ::class.java);
            startActivity(ellipse)
            // onBackPressed()
        }
        binding.landdisId.setOnClickListener{
            val distribution = Intent(this, DistributionoflandActivity ::class.java);
            startActivity(distribution)
            // onBackPressed()
        }
        binding.khatianId.setOnClickListener{
            val khatian = Intent(this, KhatianCalculatorActivity ::class.java);
            startActivity(khatian)
            // onBackPressed()
        }
        binding.unitId.setOnClickListener{
            val unit = Intent(this, UnitChangeActivity ::class.java);
            startActivity(unit)
            // onBackPressed()
        }
        binding.multipletriId.setOnClickListener{
            val multiple = Intent(this, MultipleTrianglesActivity ::class.java);
            startActivity(multiple)
            // onBackPressed()
        }
        binding.accurateId.setOnClickListener{
            val accurate = Intent(this, AccurateLandCalculationActivity ::class.java);
            startActivity(accurate)
            // onBackPressed()
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

//    override fun onSupportNavigateUp(): Boolean {
//        //finish()
//        onBackPressed()
//        return true
//    }

//    override fun finish() {
//        super.finish()
//        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
//    }

}