package org.map_bd.surveycalculator.land

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.map_bd.surveycalculator.BasicalculatorActivity
import org.map_bd.surveycalculator.CompassActivity
import org.map_bd.surveycalculator.R
import org.map_bd.surveycalculator.databinding.ActivityCircleBinding

class CircleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCircleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCircleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.toolbar.title = "Land"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var leanth1 =findViewById<EditText>(R.id.length1Id)

        var decemol = findViewById<TextView>(R.id.decemalId)
        var sqrfeet = findViewById<TextView>(R.id.squareId)
        var katha = findViewById<TextView>(R.id.kathaId)
        var sqrlink = findViewById<TextView>(R.id.squarelinkId)

        var reset =findViewById<Button>(R.id.resetId)


        var result = findViewById<LinearLayout>(R.id.resultId)

        binding.calculateId.setOnClickListener {

            if (leanth1.text.isEmpty()) {
                Toast.makeText(this, "Please Fill the field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var num1 = leanth1.text.toString().toDoubleOrNull()

            if (num1 != null ) {

                var sum = (num1 * num1) * 3.14159
                var dec = (sum / 435.6)
                var kat = (sum / 720)
                var lnk = (sum * 2.30)

                sqrfeet.text = ("$sum")
                decemol.text = ("$dec")
                katha.text = ("$kat")
                sqrlink.text = ("$lnk")
            } else {
                Toast.makeText(this, "Please add only number", Toast.LENGTH_SHORT).show()
            }
            closeKeyBoard()

            reset.visibility = View.VISIBLE
            result.visibility = View.VISIBLE


        }


        reset.setOnClickListener {
            sqrfeet.setText("")
            decemol.setText("")
            katha.setText("")
            sqrlink.setText("")
            leanth1.setText("")
            reset.visibility = View.INVISIBLE
            result.visibility = View.GONE
        }

    }

    // hide keyboard
    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
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