package org.map_bd.surveycalculator

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class InvoiceActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.invoice_activity)



        val location = findViewById<TextView>(R.id.txt_invoice)
        val title = findViewById<TextView>(R.id.txt_cust_name)
        val mouza = findViewById<TextView>(R.id.txt_date)
        val plot = findViewById<TextView>(R.id.txt_dates)
        val srname = findViewById<TextView>(R.id.txt_datess)
        val upazila = findViewById<TextView>(R.id.txt_datesss)
        val details = findViewById<TextView>(R.id.txt_datessss)

        val ivResultImage = findViewById<ImageView>(R.id.img)


        // 1. Receive and apply text to TextView
        val locations = intent.getStringExtra("LOCATION")
        location.text = locations ?: " Location"

        val titles = intent.getStringExtra("TITLE")
        title.text = titles ?: " Title"

        val mouzas = intent.getStringExtra("MOUZA")
        mouza.text = mouzas ?: " Mouza"

        val plots = intent.getStringExtra("PLOT")
        plot.text = plots ?: " Plot"

        val srnames = intent.getStringExtra("SURVEY")
        srname.text = srnames ?: " Survey or Record"

        val upazilas = intent.getStringExtra("UPAZILA")
        upazila.text = upazilas ?: " Upazila"

        val detailss = intent.getStringExtra("DETAILS")
        details.text = detailss ?: " Details"


        // 2. Receive and apply URI string to ImageView
        val uriString = intent.getStringExtra("EXTRA_IMAGE_URI")
        if (!uriString.isNullOrEmpty()) {
            val imageUri = Uri.parse(uriString)
            ivResultImage.setImageURI(imageUri)
        }

    }

}