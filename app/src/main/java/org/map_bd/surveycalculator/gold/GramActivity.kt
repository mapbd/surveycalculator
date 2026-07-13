package org.map_bd.surveycalculator.gold


import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.map_bd.surveycalculator.R
import org.map_bd.surveycalculator.databinding.ActivityGramBinding

class GramActivity : AppCompatActivity() {

    val gramsPerVori = 11.664
    val anaPerVori = 16.0
    val ratiPerAna = 6.0
    val pointPerRati = 10.0

    private lateinit var binding: ActivityGramBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGramBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.toolbar.title = "Home"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val goldwt = findViewById<EditText>(R.id.et_shonar_ojon_sub)
        val voridam = findViewById<EditText>(R.id.et_proti_vhorir_dam_sub)
        val gramdam = findViewById<EditText>(R.id.et_proti_granmer_dam_sub)





        val rvori = findViewById<TextView>(R.id.r_vhori_ana_roti_point_sub)

        val motvori = findViewById<TextView>(R.id.r_moat_vhori_sub)
        val mottaka = findViewById<TextView>(R.id.r_moat_taka_sub)


        var reset =findViewById<Button>(R.id.resetId)
        var result = findViewById<LinearLayout>(R.id.resultId)

        binding.calculateId.setOnClickListener {


            val goldwts: Double =  goldwt.text.toString().toDoubleOrNull() ?: 0.0
            val voridams =  voridam.text.toString().trim()
            val gramdams =  gramdam.text.toString().trim()

            val voris = goldwts / gramsPerVori

            motvori.text = String.format("%.2f", voris)




            // Total calculations in the smallest unit (Points)
            val totalVori = goldwts / gramsPerVori
            val totalPoints = totalVori * anaPerVori * ratiPerAna * pointPerRati

            // Round to the nearest whole point to avoid floating-point issues
            var remainingPoints = Math.round(totalPoints).toInt()

            // Extract individual units using breakdown math
            val pointsInRati = pointPerRati.toInt()
            val pointsInAna = (ratiPerAna * pointPerRati).toInt()
            val pointsInVori = (anaPerVori * ratiPerAna * pointPerRati).toInt()

            val vori = remainingPoints / pointsInVori
            remainingPoints %= pointsInVori

            val ana = remainingPoints / pointsInAna
            remainingPoints %= pointsInAna

            val rati = remainingPoints / pointsInRati
            remainingPoints %= pointsInRati

            val point = remainingPoints


            rvori.text = String.format(" $vori Vori  $ana Ana  $rati Roti  $point  Point" )



            when {
                voridams.isNotEmpty() && gramdams.isEmpty() -> {
                    // Add your Vori calculation logic here
                    val pricePerVori = voridams.toDoubleOrNull() ?: 0.0
                    val totalVori = goldwts / gramsPerVori
                    val totalTk = totalVori * pricePerVori
                    mottaka.text = String.format("%.2f", totalTk)
                }
                gramdams.isNotEmpty() && voridams.isEmpty() -> {
                    // Add your Gram calculation logic here
                    val pricePerGram = gramdams.toDoubleOrNull() ?: 0.0
                    val totalTk = goldwts * pricePerGram
                    mottaka.text = String.format("%.2f", totalTk)
                }
                voridams.isNotEmpty() && gramdams.isNotEmpty() -> {
                    Toast.makeText(this, "Error: Please enter either Vori Price or Gram Price, not both.", Toast.LENGTH_SHORT).show()
                    mottaka.text = "0.00"
                }
                else -> {
                    Toast.makeText(this, "Error: Please enter a price.", Toast.LENGTH_SHORT).show()
                    mottaka.text = "0.00"
                }
            }


            closeKeyBoard()
            reset.visibility = View.VISIBLE
            result.visibility = View.VISIBLE

        }

        reset.setOnClickListener {

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