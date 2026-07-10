package org.map_bd.surveycalculator.gold


import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.map_bd.surveycalculator.R
import org.map_bd.surveycalculator.databinding.ActivityGramBinding

class GramActivity : AppCompatActivity() {

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

        val motvori = findViewById<TextView>(R.id.r_moat_vhori_sub)
        val mottaka = findViewById<TextView>(R.id.r_moat_taka_sub)


        var reset =findViewById<Button>(R.id.resetId)
        var result = findViewById<LinearLayout>(R.id.resultId)

        binding.calculateId.setOnClickListener {



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