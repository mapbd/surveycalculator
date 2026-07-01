package org.map_bd.surveycalculator

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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.map_bd.surveycalculator.databinding.ActivityGenarelBinding
import org.map_bd.surveycalculator.databinding.ActivitySoilBinding

class SoilActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySoilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.toolbar.title = "Home"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var length = findViewById<EditText>(R.id.length4Id)
        var width = findViewById<EditText>(R.id.length5Id)
        var hight = findViewById<EditText>(R.id.length6Id)
        var price = findViewById<EditText>(R.id.length1Id)

        var feet = findViewById<TextView>(R.id.squareId)
        var priz = findViewById<TextView>(R.id.kathaId)


        var reset =findViewById<Button>(R.id.resetId)


        var result = findViewById<LinearLayout>(R.id.resultId)


        binding.calculateId.setOnClickListener {

            if (length.text.isEmpty() || width.text.isEmpty() || hight.text.isEmpty() || price.text.isEmpty()) {
                Toast.makeText(this, "Please Fill the all field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var num1 = length.text.toString().toDoubleOrNull()
            var num2 = width.text.toString().toDoubleOrNull()
            var num3 = hight.text.toString().toDoubleOrNull()
            var num4 = price.text.toString().toDoubleOrNull()

            if (num1 != null && num2 != null && num3 != null) {
                var sum = (num1 * num2 * num3)
                var lnk = num4!! * sum

                feet.text = String.format("%.4f",sum)
                priz.text = String.format("%.2f",lnk)



            } else {
                Toast.makeText(this, "Please add only number", Toast.LENGTH_SHORT).show()
            }
            closeKeyBoard()

            reset.visibility = View.VISIBLE
            result.visibility = View.VISIBLE


        }

        reset.setOnClickListener {
            length.setText("")
            width.setText("")
            hight.setText("")
            price.setText("")
            feet.setText("")
            priz.setText("")
            reset.visibility = View.INVISIBLE
            result.visibility = View.GONE
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