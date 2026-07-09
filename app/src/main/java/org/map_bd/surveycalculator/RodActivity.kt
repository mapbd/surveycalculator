package org.map_bd.surveycalculator

import android.annotation.SuppressLint
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

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.toolbar.title = "Home"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        var mm8 = findViewById<EditText>(R.id.et_8mm_ojon)
        var mm10 = findViewById<EditText>(R.id.et_10mm_ojon)
        var mm12 = findViewById<EditText>(R.id.et_12mm_ojon)
        var mm16 = findViewById<EditText>(R.id.et_16mm_ojon)
        var mm20 = findViewById<EditText>(R.id.et_20mm_ojon)
        var mm25= findViewById<EditText>(R.id.et_25mm_ojon)


        var mmp8 = findViewById<TextView>(R.id.et_8mm_piece)
        var mmp10 = findViewById<TextView>(R.id.et_10mm_piece)
        var mmp12 = findViewById<TextView>(R.id.et_12mm_piece)
        var mmp16 = findViewById<TextView>(R.id.et_16mm_piece)
        var mmp20 = findViewById<TextView>(R.id.et_20mm_piece)
        var mmp25= findViewById<TextView>(R.id.et_25mm_piece)


        var price = findViewById<EditText>(R.id.et_bajar_dor)

        var kg = findViewById<TextView>(R.id.r_rod_total_kg)
        var pic= findViewById<TextView>(R.id.r_rod_total_piece)
        var priz = findViewById<TextView>(R.id.r_rod_total_taka)


        var reset =findViewById<Button>(R.id.resetId)
        var result = findViewById<LinearLayout>(R.id.resultId)




        binding.calculateId.setOnClickListener {

            if (mm8.text.isEmpty() || mm10.text.isEmpty() || mm12.text.isEmpty() || mm16.text.isEmpty()
                || mm20.text.isEmpty() || mm25.text.isEmpty() || price.text.isEmpty()
            ) {
                Toast.makeText(this, "Please Fill the all field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var m8: Double = mm8.text.toString().toDouble()
            var m10: Double = mm10.text.toString().toDouble()
            var m12: Double = mm12.text.toString().toDouble()
            var m16: Double = mm16.text.toString().toDouble()
            var m20: Double = mm20.text.toString().toDouble()
            var m25: Double = mm25.text.toString().toDouble()

            var bdor: Double = price.text.toString().toDouble()




                mmp8.setText("" + covertToKg8(m8))
                mmp10.setText("" + covertToKg10(m10))
                mmp12.setText("" + covertToKg12(m12))
                mmp16.setText("" + covertToKg16(m16))
                mmp20.setText("" + covertToKg20(m20))
                mmp25.setText("" + covertToKg25(m25))




            var tkg = (m8 + m10 + m12 + m16 + m20 + m25)
            kg.setText(" $tkg Kilogram")

            var mp8: Double = mmp8.text.toString().toDouble()
            var mp10: Double = mmp10.text.toString().toDouble()
            var mp12: Double = mmp12.text.toString().toDouble()
            var mp16: Double = mmp16.text.toString().toDouble()
            var mp20: Double = mmp20.text.toString().toDouble()
            var mp25: Double = mmp25.text.toString().toDouble()

            var tpic = (mp8 + mp10 + mp12 + mp16 + mp20 + mp25)

            pic.text = String.format ("%.2f",tpic)

            var tk = (tkg * bdor)

            priz.setText(" $tk ")

            closeKeyBoard()


            reset.visibility = View.VISIBLE
            result.visibility = View.VISIBLE
        }

        reset.setOnClickListener {
            mm8.setText("")
            mm10.setText("")
            mm12.setText("")
            mm16.setText("")
            mm20.setText("")
            mm25.setText("")

            mmp8.setText("")
            mmp10.setText("")
            mmp12.setText("")
            mmp16.setText("")
            mmp20.setText("")
            mmp25.setText("")


            price.setText("")
            kg.setText("")
            pic.setText("")
            priz.setText("")

            result.visibility = View.GONE

        }




    }



    fun covertToKg8(m8: Double): Double {
        var kg = m8 / 4.73
        return kg
    }

    fun covertToKg10(m10: Double): Double {
        var kg = m10 / 7.40
        return kg
    }

    fun covertToKg12(m12: Double): Double {
        var kg = m12 / 10.65
        return kg
    }

    fun covertToKg16(m16: Double): Double {
        var kg = m16 / 18.94
        return kg
    }

    fun covertToKg20(m20: Double): Double {
        var kg = m20 / 29.59
        return kg
    }

    fun covertToKg25(m25: Double): Double {
        var kg = m25 / 46.24
        return kg
    }


    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


}