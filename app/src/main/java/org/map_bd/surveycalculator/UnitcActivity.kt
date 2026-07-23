package org.map_bd.surveycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.map_bd.surveycalculator.databinding.ActivityUnitcBinding
import java.math.BigDecimal

class UnitcActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUnitcBinding
    private val conversions = HashMap<String, BigDecimal>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityUnitcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup action bar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Populate conversion map with correct positive values (English & Bangla)
        initializeConversions()

        // Set default spinner positions
        binding.unit1Area.setSelection(0)
        binding.unit2Area.setSelection(3)

        // Initialize your custom converter class using binding views
        val converter = Bdconverter(
            binding.input1Area,
            binding.input2Area,
            binding.unit1Area,
            binding.unit2Area,
            conversions
        )
    }

    private fun initializeConversions() {
        // ==========================================
        // ENGLISH UNITS (Base: 1 Square Foot)
        // ==========================================
        conversions["Square inch"] = BigDecimal("0.00694444444") // 1/144
        conversions["Square foot"] = BigDecimal("1.0")
        conversions["Square hat"] = BigDecimal("2.25")
        conversions["Square yard"] = BigDecimal("9.0")
        conversions["Square meter"] = BigDecimal("10.7639104")
        conversions["Square link"] = BigDecimal("0.4356")
        conversions["Square chain"] = BigDecimal("4356.0")
        conversions["Square centimeter"] = BigDecimal("0.00107639104167")
        conversions["Square kilometer"] = BigDecimal("10763910.41671")
        conversions["Square mile"] = BigDecimal("27878400.0")

        // traditional & regional units
        conversions["Til"] = BigDecimal("24.20")
        conversions["Kranti"] = BigDecimal("72.60")
        conversions["Kora"] = BigDecimal("217.80")
        conversions["Ganda"] = BigDecimal("871.20")
        conversions["Chhatak"] = BigDecimal("45.0")
        conversions["Kachcha Kani"] = BigDecimal("45.0")
        conversions["Katha"] = BigDecimal("720.0")
        conversions["Bigha"] = BigDecimal("14400.0")

        // decimal & shatak variations
        conversions["Shatangsha"] = BigDecimal("435.6")
        conversions["Shatak"] = BigDecimal("435.6")
        conversions["Decimal"] = BigDecimal("435.6")
        conversions["Decim"] = BigDecimal("435.6")
        conversions["Ayutangsha"] = BigDecimal("4.356")

        // global metric standard units
        conversions["Air"] = BigDecimal("1076.39104")
        conversions["Acre"] = BigDecimal("43560.0")
        conversions["Hectare"] = BigDecimal("107639.10417")

        // kani configurations
        conversions["Kani"] = BigDecimal("17280.0")
        conversions["Sai Kani 1"] = BigDecimal("17424.0")
        conversions["Sai Kani 2"] = BigDecimal("52272.0")

        // micro-units
        conversions["Renu"] = BigDecimal("0.00840277777")
        conversions["Kantho"] = BigDecimal("54.45")
        conversions["Kak"] = BigDecimal("54.45")
        conversions["Dul"] = BigDecimal("0.99")
        conversions["Dhanu"] = BigDecimal("1.0")

        // ==========================================
        // BANGLA UNITS (Base: 1 Square Foot)
        // ==========================================
        conversions["বর্গইঞ্চি"] = BigDecimal("0.00694444444")
        conversions["বর্গফুট"] = BigDecimal("1.0")
        conversions["বর্গহাত"] = BigDecimal("2.25")
        conversions["বর্গগজ"] = BigDecimal("9.0")
        conversions["বর্গমিটার"] = BigDecimal("10.7639104")
        conversions["বর্গলিংক"] = BigDecimal("0.4356")
        conversions["বর্গচেইন"] = BigDecimal("4356.0")
        conversions["বর্গসেন্টিমিটার"] = BigDecimal("0.00107639104167")
        conversions["বর্গকিলোমিটার"] = BigDecimal("10763910.41671")
        conversions["বর্গমাইল"] = BigDecimal("27878400.0")

        // ঐতিহ্যবাহী ও দেশীয় এককসমূহ
        conversions["তিল"] = BigDecimal("24.20")
        conversions["ক্রান্তি"] = BigDecimal("72.60")
        conversions["কড়া"] = BigDecimal("217.80")
        conversions["গন্ডা"] = BigDecimal("871.20")
        conversions["ছটাক"] = BigDecimal("45.0")
        conversions["কাচ্চা কানি"] = BigDecimal("45.0")
        conversions["কাঠা"] = BigDecimal("720.0")
        conversions["বিঘা"] = BigDecimal("14400.0")

        // শতাংশ ও শতক সংক্রান্ত এককসমূহ
        conversions["শতাংশ"] = BigDecimal("435.6")
        conversions["শতক"] = BigDecimal("435.6")
        conversions["ডেসিমাল"] = BigDecimal("435.6")
        conversions["ডিসিম"] = BigDecimal("435.6")
        conversions["অযুতাংশ"] = BigDecimal("4.356")

        // আন্তর্জাতিক এককসমূহ
        conversions["এয়র"] = BigDecimal("1076.39104")
        conversions["একর"] = BigDecimal("43560.0")
        conversions["হেক্টর"] = BigDecimal("107639.10417")

        // কানি সংক্রান্ত আঞ্চলিক এককসমূহ
        conversions["কানি"] = BigDecimal("17280.0")
        conversions["সাই কানি ১"] = BigDecimal("17424.0")
        conversions["সাই কানি ২"] = BigDecimal("52272.0")

        // অতি ক্ষুদ্র এককসমূহ
        conversions["রেনু"] = BigDecimal("0.00840277777")
        conversions["কন্ঠ"] = BigDecimal("54.45")
        conversions["কাক"] = BigDecimal("54.45")
        conversions["দুল"] = BigDecimal("0.99")
        conversions["ধনু"] = BigDecimal("1.0")
    }
}