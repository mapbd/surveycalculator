package org.map_bd.surveycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Spinner
import org.map_bd.surveycalculator.databinding.ActivityMassBinding
import java.math.BigDecimal

class MassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMassBinding
    private lateinit var input1: EditText
    private lateinit var input2: EditText
    private lateinit var unit1: Spinner
    private lateinit var unit2: Spinner
    private lateinit var conversions : HashMap<String, BigDecimal>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.toolbar.title = "Home"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        input1 = findViewById(R.id.input1_mass)
        input2 = findViewById(R.id.input2_mass)
        unit1 = findViewById(R.id.unit1_mass)
        unit2 = findViewById(R.id.unit2_mass)

        unit1.setSelection(2)
        unit2.setSelection(5)

        conversions = HashMap()
        conversions["Milligram (mg)"] = BigDecimal("0.001")
        conversions["Gram (g)"] = BigDecimal("1")
        conversions["Kilogram (kg)"] = BigDecimal("1000")
        conversions["Tonne / Metric ton (t)"] = BigDecimal("1000000")
        conversions["Ounce (oz)"] = BigDecimal("28.349523125")
        conversions["Pound (lb)"] = BigDecimal("453.59237")
        conversions["US ton"] = BigDecimal("907184.74")
        conversions["Imperial ton"] = BigDecimal("1016046.9088")

        conversions["মিলিগ্রাম (mg)"] = BigDecimal("0.001")
        conversions["গ্রাম (g)"] = BigDecimal("1")
        conversions["কিলোগ্রাম (kg)"] = BigDecimal("1000")
        conversions["টন / মেট্রিক টন (t)"] = BigDecimal("1000000")
        conversions["আউন্স (oz)"] = BigDecimal("28.349523125")
        conversions["পাউন্ড (lb)"] = BigDecimal("453.59237")
        conversions["ইউএস টন"] = BigDecimal("907184.74")
        conversions["ইম্পেরিয়াল টন"] = BigDecimal("1016046.9088")

        val converter = Converter(input1, input2, unit1, unit2, conversions)


    }


}