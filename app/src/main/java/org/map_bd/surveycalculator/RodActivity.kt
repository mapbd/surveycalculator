package org.map_bd.surveycalculator

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.map_bd.surveycalculator.databinding.ActivityRodBinding


class RodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.toolbar.title = "Home"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        var mm8 = findViewById<EditText>(R.id.mm8Id)
        var mm10 = findViewById<EditText>(R.id.mm10Id)
        var mm12 = findViewById<EditText>(R.id.mm12Id)
        var mm16 = findViewById<EditText>(R.id.mm16Id)
        var mm20 = findViewById<EditText>(R.id.mm20Id)
        var mm25= findViewById<EditText>(R.id.mm25Id)


        var mmp8 = findViewById<EditText>(R.id.mmp8Id)
        var mmp10 = findViewById<EditText>(R.id.mmp10Id)
        var mmp12 = findViewById<EditText>(R.id.mmp12Id)
        var mmp16 = findViewById<EditText>(R.id.mmp16Id)
        var mmp20 = findViewById<EditText>(R.id.mmp20Id)
        var mmp25= findViewById<EditText>(R.id.mmp25Id)


        var price = findViewById<EditText>(R.id.priceId)

        var kg = findViewById<TextView>(R.id.kgId)
        var priz = findViewById<TextView>(R.id.prizeId)


        var reset =findViewById<Button>(R.id.resetId)


        var result = findViewById<LinearLayout>(R.id.resultId)


        binding.calculateId.setOnClickListener {


            var mm8 = mm8.text.toString().toDoubleOrNull()
            var mm10 = mm10.text.toString().toDoubleOrNull()
            var mm12 = mm12.text.toString().toDoubleOrNull()
            var mm16 = mm16.text.toString().toDoubleOrNull()
            var mm20 = mm20.text.toString().toDoubleOrNull()
            var mm25 = mm25.text.toString().toDoubleOrNull()

            var mmp8 = mmp8.text.toString().toDoubleOrNull()
            var mmp10 = mmp10.text.toString().toDoubleOrNull()
            var mmp12 = mmp12.text.toString().toDoubleOrNull()
            var mmp16 = mmp16.text.toString().toDoubleOrNull()
            var mmp20 = mmp20.text.toString().toDoubleOrNull()
            var mmp25 = mmp25.text.toString().toDoubleOrNull()






            if (mm8 != null || mm10 != null || mm12 != null || mm16 != null || mm20 != null || mm25 != null ||
                mmp8 != null || mmp10 != null || mmp12 != null || mmp16 != null || mmp20 != null || mmp25 != null) {


                while (mmp8 != null) {
                    mmp8 = mm8!! * 0.120
                }




            } else {
                Toast.makeText(this, "Please add any number", Toast.LENGTH_SHORT).show()
            }
            closeKeyBoard()

            reset.visibility = View.VISIBLE
            result.visibility = View.VISIBLE


        }




    }

    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }




}