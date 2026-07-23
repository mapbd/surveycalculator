package org.map_bd.surveycalculator


import android.os.Bundle
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import org.map_bd.surveycalculator.databinding.ActivityAreaBinding
import java.math.BigDecimal


class AreaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAreaBinding


    private lateinit var input1: EditText
    private lateinit var input2: EditText
    private lateinit var unit1: Spinner
    private lateinit var unit2: Spinner
    private lateinit var conversions : HashMap<String, BigDecimal>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAreaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.toolbar.title = "Home"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        input1 = findViewById(R.id.input1_area)
        input2 = findViewById(R.id.input2_area)
        unit1 = findViewById(R.id.unit1_area)
        unit2 = findViewById(R.id.unit2_area)

        unit1.setSelection(1)
        unit2.setSelection(5)

        conversions = HashMap()
        conversions["Square centimetre (cm²)"] = BigDecimal("0.0001")
        conversions["Square metre (m²)"] = BigDecimal("1")
        conversions["Square kilometre (km²)"] = BigDecimal("1000000")
        conversions["Hectare (ha)"] = BigDecimal("10000")
        conversions["Square inch (sq in)"] = BigDecimal("0.00064516")
        conversions["Square foot (sq ft)"] = BigDecimal("0.09290304")
        conversions["Square yard (sq yd)"] = BigDecimal("0.83612736")
        conversions["Square mile (sq mi)"] = BigDecimal("2589988.11")
        conversions["Acre (ac)"] = BigDecimal("4046.8564224")

        conversions["বর্গ সেন্টিমিটার (cm²)"] = BigDecimal("0.0001")
        conversions["বর্গ মিটার (m²)"] = BigDecimal("1")
        conversions["বর্গ কিলোমিটার (km²)"] = BigDecimal("1000000")
        conversions["হেক্টর (ha)"] = BigDecimal("10000")
        conversions["বর্গ ইঞ্চি (sq in)"] = BigDecimal("0.00064516")
        conversions["বর্গ ফুট (sq ft)"] = BigDecimal("0.09290304")
        conversions["বর্গ গজ (sq yd)"] = BigDecimal("0.83612736")
        conversions["বর্গ মাইল (sq mi)"] = BigDecimal("2589988.11")
        conversions["একর (ac)"] = BigDecimal("4046.8564224")

        val converter = Converter(input1, input2, unit1, unit2, conversions)





    }
}