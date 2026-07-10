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


        val mottk = findViewById<TextView>(R.id.r_moat_taka)
        val motgran = findViewById<TextView>(R.id.r_moat_gram)
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