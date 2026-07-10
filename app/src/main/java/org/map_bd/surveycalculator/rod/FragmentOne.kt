package org.map_bd.surveycalculator.rod


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import org.map_bd.surveycalculator.R
import org.map_bd.surveycalculator.databinding.FragmentOneBinding


class FragmentOne : Fragment() {

    private var _binding: FragmentOneBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("DefaultLocale")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOneBinding.inflate(inflater, container, false)
        val view = binding.root





            val mm8 = view.findViewById<EditText>(R.id.et_8mm_ojon)
            val mm10 = view.findViewById<EditText>(R.id.et_10mm_ojon)
            val mm12 = view.findViewById<EditText>(R.id.et_12mm_ojon)
            val mm16 = view.findViewById<EditText>(R.id.et_16mm_ojon)
            val mm20 = view.findViewById<EditText>(R.id.et_20mm_ojon)
            val mm25 = view.findViewById<EditText>(R.id.et_25mm_ojon)


            val mmp8 = view.findViewById<TextView>(R.id.et_8mm_piece)
            val mmp10 = view.findViewById<TextView>(R.id.et_10mm_piece)
            val mmp12 = view.findViewById<TextView>(R.id.et_12mm_piece)
            val mmp16 = view.findViewById<TextView>(R.id.et_16mm_piece)
            val mmp20 = view.findViewById<TextView>(R.id.et_20mm_piece)
            val mmp25 = view.findViewById<TextView>(R.id.et_25mm_piece)


            val price = view.findViewById<EditText>(R.id.et_bajar_dor)

            val kg = view.findViewById<TextView>(R.id.r_rod_total_kg)
            val pic = view.findViewById<TextView>(R.id.r_rod_total_piece)
            val priz = view.findViewById<TextView>(R.id.r_rod_total_taka)


            val reset = view.findViewById<Button>(R.id.resetId)
            val result = view.findViewById<LinearLayout>(R.id.resultId)




            binding.calculateId.setOnClickListener {

                if (mm8.text.isEmpty()
                    || mm10.text.isEmpty()
                    || mm12.text.isEmpty()
                    || mm16.text.isEmpty()
                    || mm20.text.isEmpty()
                    || mm25.text.isEmpty()
                    || price.text.isEmpty()
                ) {
                    Toast.makeText(requireContext(), "Please Fill the all field", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val m8: Double = mm8.text.toString().toDouble()
                val m10: Double = mm10.text.toString().toDouble()
                val m12: Double = mm12.text.toString().toDouble()
                val m16: Double = mm16.text.toString().toDouble()
                val m20: Double = mm20.text.toString().toDouble()
                val m25: Double = mm25.text.toString().toDouble()

                val bdor: Double = price.text.toString().toDouble()


                    mmp8.text = String.format("%.2f" , covertToKg8(m8))
                    mmp10.text = String.format("%.2f" , covertToKg10(m10))
                    mmp12.text = String.format("%.2f" , covertToKg12(m12))
                    mmp16.text = String.format("%.2f" , covertToKg16(m16))
                    mmp20.text = String.format("%.2f" , covertToKg20(m20))
                    mmp25.text = String.format("%.2f" , covertToKg25(m25))


                val tkg = (m8 + m10 + m12 + m16 + m20 + m25)
                kg.text = String.format("%.2f Kg",tkg )


                val mp8: Double = mmp8.text.toString().toDouble()
                val mp10: Double = mmp10.text.toString().toDouble()
                val mp12: Double = mmp12.text.toString().toDouble()
                val mp16: Double = mmp16.text.toString().toDouble()
                val mp20: Double = mmp20.text.toString().toDouble()
                val mp25: Double = mmp25.text.toString().toDouble()

                val tpic = (mp8 + mp10 + mp12 + mp16 + mp20 + mp25)

                pic.text = String.format("%.2f Piece", tpic)

                val tk = (tkg * bdor)

                priz.text = String.format("%.2f", tk)

                hideKeyboard()


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



        return view

    }




    fun covertToKg8(m8: Double): Double {
        val kg = m8 / 4.73
        return kg
    }

    fun covertToKg10(m10: Double): Double {
        val kg = m10 / 7.40
        return kg
    }

    fun covertToKg12(m12: Double): Double {
        val kg = m12 / 10.65
        return kg
    }

    fun covertToKg16(m16: Double): Double {
        val kg = m16 / 18.94
        return kg
    }

    fun covertToKg20(m20: Double): Double {
        val kg = m20 / 29.59
        return kg
    }

    fun covertToKg25(m25: Double): Double {
        val kg = m25 / 46.24
        return kg
    }

    fun Fragment.hideKeyboard() {
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        val view = activity?.currentFocus ?: view?.rootView

        if (view != null && inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


}