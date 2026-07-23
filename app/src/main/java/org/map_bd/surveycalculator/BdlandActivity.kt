package org.map_bd.surveycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Spinner
import org.map_bd.surveycalculator.databinding.ActivityBdlandBinding
import java.math.BigDecimal

class BdlandActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBdlandBinding
    private val conversions = HashMap<String, BigDecimal>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityBdlandBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup action bar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Populate conversion map with correct positive values
        initializeConversions()

        // Set default spinner positions
        binding.unit1Area.setSelection(0)
        binding.unit2Area.setSelection(3)

        // Initialize your custom converter class using binding views
        val converter = Converter(
            binding.input1Area,
            binding.input2Area,
            binding.unit1Area,
            binding.unit2Area,
            conversions
        )
    }

    private fun initializeConversions() {
        conversions["Square Feet"] = BigDecimal("1")
        conversions["Til"] = BigDecimal("3.63")       // Fixed negative value
        conversions["Kranti"] = BigDecimal("72.6")
        conversions["Kora"] = BigDecimal("217.8")
        conversions["Gonda"] = BigDecimal("871.2")
        conversions["Shatak"] = BigDecimal("435.6")
        conversions["Kani"] = BigDecimal("17424")

        conversions["বর্গফুট"] = BigDecimal("1")
        conversions["তিল"] = BigDecimal("3.63")       // Fixed negative value
        conversions["ক্রান্তি"] = BigDecimal("72.6")
        conversions["কড়া"] = BigDecimal("217.8")
        conversions["গন্ডা"] = BigDecimal("871.2")
        conversions["শতক"] = BigDecimal("435.6")
        conversions["কানি"] = BigDecimal("17424")
    }
}