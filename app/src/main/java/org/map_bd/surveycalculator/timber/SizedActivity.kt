package org.map_bd.surveycalculator.timber



import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import org.map_bd.surveycalculator.R
import org.map_bd.surveycalculator.databinding.ActivitySizedBinding

class SizedActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySizedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySizedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.toolbar.title = "Home"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)




        var length = findViewById<EditText>(R.id.lengthId)
        var width = findViewById<EditText>(R.id.widthId)
        var thickness = findViewById<EditText>(R.id.thicknessId)
        var quntity = findViewById<EditText>(R.id.quantityId)

        var price = findViewById<EditText>(R.id.priceId)

        var cft = findViewById<TextView>(R.id.cftId)
        var priz = findViewById<TextView>(R.id.prizeId)


        var reset =findViewById<Button>(R.id.resetId)


        var result = findViewById<LinearLayout>(R.id.resultId)


        binding.calculateId.setOnClickListener {

            if (length.text.isEmpty() || width.text.isEmpty() || thickness.text.isEmpty() || quntity.toString().isEmpty() || price.text.isEmpty()) {
                Toast.makeText(this, "Please Fill the all field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var num1 = length.text.toString().toDoubleOrNull()
            var num2 = width.text.toString().toDoubleOrNull()
            var num3 = thickness.text.toString().toDoubleOrNull()
            var num4 = quntity.text.toString().toDoubleOrNull()
            var num5 = price.text.toString().toDoubleOrNull()

            if (num1 != null && num2 != null && num3 != null && num4 != null) {
                var sum = (num1 * num2 * num3 * num4)/144
                var lnk = num5!! * sum

                cft.text = String.format("%.2f",sum)
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
            thickness.setText("")
            quntity.setText("")
            price.setText("")
            cft.setText("")
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