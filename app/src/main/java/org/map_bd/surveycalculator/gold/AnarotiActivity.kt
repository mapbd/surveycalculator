package org.map_bd.surveycalculator.gold

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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.map_bd.surveycalculator.R
import org.map_bd.surveycalculator.databinding.ActivityAnarotiBinding
import org.map_bd.surveycalculator.databinding.ActivityGenarelBinding
import org.map_bd.surveycalculator.databinding.ActivityGoldBinding

class AnarotiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnarotiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnarotiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.toolbar.title = "Home"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val vori = findViewById<EditText>(R.id.et_vhori)
        val ana = findViewById<EditText>(R.id.et_ana)
        val rotti = findViewById<EditText>(R.id.et_roti)
        val point = findViewById<EditText>(R.id.et_point)
        val mojori = findViewById<EditText>(R.id.et_mojori)
        val voridam = findViewById<EditText>(R.id.et_vhorir_dam)


        val rvori = findViewById<TextView>(R.id.r_vhori_ana_roti_point)

        val mottk = findViewById<TextView>(R.id.r_moat_taka)
        val motgran = findViewById<TextView>(R.id.r_moat_gram)

        val motvori = findViewById<TextView>(R.id.r_proti_vhorir_dam)
        val gramtk = findViewById<TextView>(R.id.r_proti_gramer_dam)

        var reset =findViewById<Button>(R.id.resetId)
        var result = findViewById<LinearLayout>(R.id.resultId)


        binding.calculateId.setOnClickListener {

            val vori: Double = vori.text.toString().toDoubleOrNull() ?: 0.0
            val ana: Double = ana.text.toString().toDoubleOrNull() ?: 0.0
            val rotti: Double = rotti.text.toString().toDoubleOrNull() ?: 0.0
            val point: Double = point.text.toString().toDoubleOrNull() ?: 0.0
            val mojoti: Double = mojori.text.toString().toDoubleOrNull() ?: 0.0
            val voridam: Double = voridam.text.toString().toDoubleOrNull() ?: 0.0





//            0 Vori, 0 Ana, 0 Ratti, 0 Point
            rvori.text= String.format("%.2f Vori , %.2f Ana , %.2f Rati , %.2f  Point", vori , ana , rotti , point )


            val anar = (ana / 16)
            val rottir = (rotti / 96)
            val pointr = (point / 960)


//            all are in vori
            val allvori = (vori + anar + rottir + pointr)


//            all are in gram
            val allgram = allvori * 11.664

//            per gram mojori expence
            val expence = mojoti / 11.664

//                per gram price
            val texpen = voridam / 11.664

//                total gram expence
            val goldexpence = allgram * texpen

//            tatal gram mojori
            val mojoriexpence = allgram * expence


//            all expence
            val all = goldexpence + mojoriexpence

//            all gram expence
            val gramt = all / allgram





            mottk.text = String.format("%.2f", all)

            motgran.text = String.format("%.2f", allgram)

            motvori.text = String.format("%.2f", voridam)

            gramtk.text = String.format("%.2f", gramt)

            closeKeyBoard()
            reset.visibility = View.VISIBLE
            result.visibility = View.VISIBLE

        }




        reset.setOnClickListener {

            vori.setText(" ")
            ana.setText(" ")
            rotti.setText(" ")
            point.setText(" ")
            mojori.setText(" ")
            voridam.setText(" ")
            rvori.setText(" ")
            mottk.setText(" ")
            motgran.setText(" ")
            motvori.setText(" ")
            gramtk.setText(" ")
            reset.visibility = View.INVISIBLE
            result.visibility = View.GONE
        }
    }

    // hide keyboard
    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


}