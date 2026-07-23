package org.map_bd.surveycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Spinner
import org.map_bd.surveycalculator.databinding.ActivityTimeBinding
import java.math.BigDecimal

class TimeActivity: AppCompatActivity() {

    private lateinit var binding: ActivityTimeBinding
    private lateinit var input1: EditText
    private lateinit var input2: EditText
    private lateinit var unit1: Spinner
    private lateinit var unit2: Spinner
    private lateinit var conversions : HashMap<String, BigDecimal>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.toolbar.title = "Home"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        input1 = findViewById(R.id.input1_time)
        input2 = findViewById(R.id.input2_time)
        unit1 = findViewById(R.id.unit1_time)
        unit2 = findViewById(R.id.unit2_time)

        unit1.setSelection(2)
        unit2.setSelection(1)

        conversions = HashMap()
        conversions["Millisecond (ms)"] = BigDecimal("0.001")
        conversions["Second (s)"] = BigDecimal("1")
        conversions["Minute"] = BigDecimal("60")
        conversions["Hour (h)"] = BigDecimal("3600")
        conversions["Day"] = BigDecimal("86400")
        conversions["Week"] = BigDecimal("604800")
        conversions["Month"] = BigDecimal("2628000")
        conversions["Year"] = BigDecimal("31536000")
        conversions["Decade"] = BigDecimal("315360000")
        conversions["Century"] = BigDecimal("3153600000")
        conversions["Millennium"] = BigDecimal("31536000000")


        conversions["মিলিসেকেন্ড (ms)"] = BigDecimal("0.001")
        conversions["সেকেন্ড (s)"] = BigDecimal("1")
        conversions["মিনিট"] = BigDecimal("60")
        conversions["ঘণ্টা (h)"] = BigDecimal("3600")
        conversions["দিন"] = BigDecimal("86400")
        conversions["সপ্তাহ"] = BigDecimal("604800")
        conversions["মাস"] = BigDecimal("2628000")
        conversions["বছর"] = BigDecimal("31536000")
        conversions["দশক"] = BigDecimal("315360000")
        conversions["শতাব্দী"] = BigDecimal("3153600000")
        conversions["সহস্রাব্দ"] = BigDecimal("31536000000")

        val converter = Converter(input1, input2, unit1, unit2, conversions)


    }


}