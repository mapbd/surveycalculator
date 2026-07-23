package org.map_bd.surveycalculator

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import java.math.BigDecimal
import java.math.RoundingMode

class Bdconverter(
    private val input1: EditText,
    private val input2: EditText,
    private val unit1: Spinner,
    private val unit2: Spinner,
    private val conversions: HashMap<String, BigDecimal>
) {

    // একে অপরের টেক্সট ওয়াচার ট্রিগার করা থেকে বিরত রাখার ফ্ল্যাগ
    private var isUpdating = false

    init {
        setupListeners()
    }

    private fun setupListeners() {
        // Input 1 এ টাইপ করলে Input 2 আপডেট হবে
        input1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return
                isUpdating = true
                performConversion(input1, input2, unit1, unit2)
                isUpdating = false
            }
        })

        // Input 2 এ টাইপ করলে Input 1 আপডেট হবে
        input2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return
                isUpdating = true
                performConversion(input2, input1, unit2, unit1)
                isUpdating = false
            }
        })

        // Spinner 1 এর ইউনিট পরিবর্তন হলে ড্রপডাউন সিলেকশন আপডেট
        unit1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isUpdating) return
                isUpdating = true
                performConversion(input1, input2, unit1, unit2)
                isUpdating = false
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Spinner 2 এর ইউনিট পরিবর্তন হলে ড্রপডাউন সিলেকশন আপডেট
        unit2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isUpdating) return
                isUpdating = true
                performConversion(input1, input2, unit1, unit2)
                isUpdating = false
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    /**
     * আসল রূপান্তর ও হিসাব নিকাশের মূল মেথড
     */
    private fun performConversion(
        sourceInput: EditText,
        targetInput: EditText,
        sourceSpinner: Spinner,
        targetSpinner: Spinner
    ) {
        val sourceValueStr = sourceInput.text.toString().trim()

        // ইনপুট খালি বা ইনভ্যালিড ক্যারেক্টার হলে আউটপুট ক্লিয়ার করে দিন
        if (sourceValueStr.isEmpty() || sourceValueStr == "." || sourceValueStr == "-") {
            targetInput.setText("")
            return
        }

        try {
            val sourceValue = BigDecimal(sourceValueStr)
            val fromUnit = sourceSpinner.selectedItem.toString()
            val toUnit = targetSpinner.selectedItem.toString()

            // ম্যাপ থেকে কনভার্সন রেট নেওয়া, না পাওয়া গেলে সেটিকে ১ (Base) ধরা
            val fromFactor = conversions[fromUnit] ?: BigDecimal.ONE
            val toFactor = conversions[toUnit] ?: BigDecimal.ONE

            // ১. ইনপুট দেওয়া মানকে বেস ইউনিটে (Square Foot) নিয়ে যাওয়া
            val baseValue = sourceValue.multiply(fromFactor)

            // ২. বেস ইউনিট থেকে কাঙ্ক্ষিত ইউনিটে রূপান্তর (দশমিকের পর ৮ ঘর পর্যন্ত নিখুঁত মান রাখবে)
            val targetValue = baseValue.divide(toFactor, 8, RoundingMode.HALF_UP)

            // অপ্রয়োজনীয় ট্রেইলিং জিরো (.000) বাদ দিয়ে প্লেইন টেক্সট হিসেবে বসানো
            val finalizedResult = targetValue.stripTrailingZeros().toPlainString()

            targetInput.setText(finalizedResult)
        } catch (e: Exception) {
            targetInput.setText("")
        }
    }
}