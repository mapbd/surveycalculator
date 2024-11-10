package org.map_bd.surveycalculator.land


import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.map_bd.surveycalculator.R
import org.map_bd.surveycalculator.databinding.ActivityOblongBinding



@Suppress("DEPRECATION")
class OblongActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOblongBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOblongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.toolbar.title = "Land"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)




        var leanth1 =findViewById<EditText>(R.id.length1Id)
        var leanth2 =findViewById<EditText>(R.id.length2Id)
        var width1 =findViewById<EditText>(R.id.width1Id)
        var width2 =findViewById<EditText>(R.id.width2Id)

        var decemol = findViewById<TextView>(R.id.decemalId)
        var sqrfeet = findViewById<TextView>(R.id.squareId)
        var katha = findViewById<TextView>(R.id.kathaId)
        var sqrlink = findViewById<TextView>(R.id.squarelinkId)

        var reset =findViewById<Button>(R.id.resetId)


        var result = findViewById<LinearLayout>(R.id.resultId)


        binding.calculateId.setOnClickListener {

            if (leanth1.text.isEmpty() || leanth2.text.isEmpty() || width1.text.isEmpty() || width2.text.isEmpty()) {
                Toast.makeText(this, "Please Fill the all field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var num1 = leanth1.text.toString().toDoubleOrNull()
            var num2 = leanth2.text.toString().toDoubleOrNull()
            var num3 = width1.text.toString().toDoubleOrNull()
            var num4 = width2.text.toString().toDoubleOrNull()

            if (num1 != null && num2 != null && num3 != null && num4 != null) {
                var suml = (num1 + num2) / 2
                var sumw = (num3 + num4) / 2
                var sum = (suml * sumw)
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
            leanth2.setText("")
            width1.setText("")
            width2.setText("")
            reset.visibility = View.INVISIBLE
            result.visibility = View.GONE
        }

//        binding.printId.setOnClickListener {
//            createPdf()
//        }


    }






    // hide keyboard
    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }



    override fun onSupportNavigateUp(): Boolean {
        //finish()
        onBackPressed()
        return true
    }



}




