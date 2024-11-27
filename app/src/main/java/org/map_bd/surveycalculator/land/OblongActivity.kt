package org.map_bd.surveycalculator.land


import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log.d
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.map_bd.surveycalculator.BasicalculatorActivity
import org.map_bd.surveycalculator.CompassActivity
import org.map_bd.surveycalculator.R
import org.map_bd.surveycalculator.databinding.ActivityOblongBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


class OblongActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOblongBinding


    private val REQUESTCODE = 100
    private var pageWidth = 720
    private var pageHeight = 1200
    private var imageBitmap: Bitmap? =  null
    private var scaledImageBitmap: Bitmap? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOblongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.header_image)
        scaledImageBitmap = imageBitmap?.let { Bitmap.createScaledBitmap(it, 720, 257, false) }




        var leanth1 =findViewById<EditText>(R.id.length1Id)
        var leanth2 =findViewById<EditText>(R.id.length2Id)
        var width1 =findViewById<EditText>(R.id.width1Id)
        var width2 =findViewById<EditText>(R.id.width2Id)

        var decemol = findViewById<TextView>(R.id.decemalId)
        var sqrfeet = findViewById<TextView>(R.id.squareId)
        var katha = findViewById<TextView>(R.id.kathaId)
        var sqrlink = findViewById<TextView>(R.id.squarelinkId)

        var reset =findViewById<Button>(R.id.resetId)


        var result = findViewById<LinearLayout>(R.id.resultId)


        binding.calculateId.setOnClickListener {

            if (leanth1.text.isEmpty() || leanth2.text.isEmpty() || width1.text.isEmpty() || width2.text.isEmpty()) {
                Toast.makeText(this, "Please Fill the all field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var num1 = leanth1.text.toString().toDoubleOrNull()
            var num2 = leanth2.text.toString().toDoubleOrNull()
            var num3 = width1.text.toString().toDoubleOrNull()
            var num4 = width2.text.toString().toDoubleOrNull()

            if (num1 != null && num2 != null && num3 != null && num4 != null) {
                var suml = (num1 + num2) / 2
                var sumw = (num3 + num4) / 2
                var sum = (suml * sumw)
                var dec = (sum / 435.6)
                var kat = (sum / 720)
                var lnk = (sum * 2.30)

                sqrfeet.text = String.format("%.4f",sum)
                decemol.text = String.format("%.4f",dec)
                katha.text = String.format("%.4f",kat)
                sqrlink.text = String.format("%.4f",lnk)
            } else {
                Toast.makeText(this, "Please add only number", Toast.LENGTH_SHORT).show()
            }
            closeKeyBoard()

            reset.visibility = View.VISIBLE
            result.visibility = View.VISIBLE


        }

        reset.setOnClickListener {
            sqrfeet.setText("")
            decemol.setText("")
            katha.setText("")
            sqrlink.setText("")
            leanth1.setText("")
            leanth2.setText("")
            width1.setText("")
            width2.setText("")
            reset.visibility = View.INVISIBLE
            result.visibility = View.GONE
        }

        binding.printId.setOnClickListener {

            var num1 = leanth1.text.toString()
            var num2 = leanth2.text.toString()
            var num3 = width1.text.toString()
            var num4 = width2.text.toString()

            var dece = decemol.text.toString()
            var sqrf = sqrfeet.text.toString()
            var kata = katha.text.toString()
            var sqrlin = sqrlink.text.toString()


            val sdf = SimpleDateFormat("dd-MM-yyyy HH-mm-ss")
            val currentDateAndTime = sdf.format(Date()).toString()



            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        applicationContext,
                        permission.READ_MEDIA_IMAGES
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    createPDF(currentDateAndTime,num1,num2,num3,num4,dece,sqrf,kata,sqrlin)
                } else {
                    requestAllPermission()
                }
            } else {
                if (ContextCompat.checkSelfPermission(
                        applicationContext,
                        permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        applicationContext,
                        permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    createPDF(currentDateAndTime,num1,num2,num3,num4,dece,sqrf,kata,sqrlin)
                } else {
                    requestAllPermission()
                }
            }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_new, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                finish()
                return true
            }
            R.id.basicCal ->{
                val calculator = Intent(this, BasicalculatorActivity::class.java);
                startActivity(calculator)
            }
            R.id.compassId ->{
                val compass = Intent(this, CompassActivity::class.java);
                startActivity(compass)
            }
        }
        return true
    }



    private fun createPDF(currentDateAndTime: String, num1:String, num2:String,num3:String, num4:String,dece: String,sqrf:String, kata: String, sqrlin: String) {
        val pdfDocument = PdfDocument()
        val paint = Paint()
        val pageInfo = PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        canvas.drawBitmap(scaledImageBitmap!!, 0f, 0f, paint)
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = 40f
        //paint.color = resources.getColor(android.R.color.holo_blue_bright, null)
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
        canvas.drawText("Oblong Calculation" , (pageWidth / 2).toFloat(),  295f, paint)

        paint.textAlign = Paint.Align.CENTER
        paint.textSize = 20f
        canvas.drawText("Your Input Table",(pageWidth / 2).toFloat(),335f,paint)

        val line = Paint()
        line.strokeWidth = 2f

        canvas.drawLine(150f,310f,550f,310f,line)

        canvas.drawLine(150f,310f,150f,350f,line)
        canvas.drawLine(550f,310f,550f,350f,line)

        // horizontal lines
        canvas.drawLine(150f, 350f, 550f, 350f, line)
        canvas.drawLine(150f, 400f, 550f, 400f, line)
        canvas.drawLine(150f, 450f, 550f, 450f, line)
        canvas.drawLine(150f, 500f, 550f, 500f, line)
        canvas.drawLine(150f, 550f, 550f, 550f, line)


        // vertical lines
        canvas.drawLine(150f, 350f, 150f, 550f, line)
        canvas.drawLine(350f, 350f, 350f, 550f, line)
        canvas.drawLine(550f, 350f, 550f, 550f, line)

        paint.textAlign = Paint.Align.LEFT
        paint.color = resources.getColor(R.color.grapeColor,null)
        paint.textSize = 20f
        canvas.drawText("Length 01",180f,380f,paint)

        paint.textAlign = Paint.Align.LEFT
        paint.color = resources.getColor(R.color.black,null)
        paint.textSize = 20f
        canvas.drawText("$num1 Feet",360f,380f,paint)

        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 20f
        canvas.drawText("Length 02",180f,430f,paint)

        paint.textAlign = Paint.Align.LEFT
        paint.color = resources.getColor(R.color.black,null)
        paint.textSize = 20f
        canvas.drawText("$num2 Feet",360f,430f,paint)

        paint.textAlign = Paint.Align.LEFT
        paint.color = resources.getColor(R.color.grapeColor,null)
        paint.textSize = 20f
        canvas.drawText("Width 01",180f,480f,paint)

        paint.textAlign = Paint.Align.LEFT
        paint.color = resources.getColor(R.color.black,null)
        paint.textSize = 20f
        canvas.drawText("$num3 Feet",360f,480f,paint)

        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 20f
        canvas.drawText("Width 02",180f,530f,paint)

        paint.textAlign = Paint.Align.LEFT
        paint.color = resources.getColor(R.color.black,null)
        paint.textSize = 20f
        canvas.drawText("$num4 Feet",360f,530f,paint)


        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 20f
        paint.color = resources.getColor(R.color.hollyGreenColor, null)
        canvas.drawText("Total : ",150f, 565f, paint)

        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 30f
        paint.color = resources.getColor(R.color.hollyGreenColor, null)
        canvas.drawText("Square Feet = $sqrf ",150f, 590f, paint)

        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 30f
        paint.color = resources.getColor(R.color.hollyGreenColor, null)
        canvas.drawText("Decimal = $dece ",150f, 620f, paint)

        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 30f
        paint.color = resources.getColor(R.color.hollyGreenColor, null)
        canvas.drawText("Katha = $kata ",150f, 650f, paint)

        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 30f
        paint.color = resources.getColor(R.color.hollyGreenColor, null)
        canvas.drawText("Square Link = $sqrlin ",150f, 680f, paint)


        paint.textAlign = Paint.Align.CENTER
        paint.textSize = 15f
        paint.color = resources.getColor(R.color.brickRedColor, null)
        canvas.drawText("Note: This is automatically generated from Survey Calculator.",(pageWidth / 2).toFloat(), 1000f, paint)

        paint.textAlign = Paint.Align.LEFT
        paint.textSize = 10f
        paint.color = resources.getColor(R.color.black, null)
        canvas.drawText("Pdf Generating Time: $currentDateAndTime",40f,1150f,paint)

        pdfDocument.finishPage(page)


        val folder = File(Environment.getExternalStorageDirectory(), "Survey_Calculator")
        if (folder.exists()) {
            d("folder", "exists")
        } else {
            d("folder", "not exists")
            folder.mkdirs()
        }


        val file = File(folder,
            "/Oblong_Calculation_$currentDateAndTime.pdf"
        )
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(this, "PDF saved to " + file.absolutePath, Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        pdfDocument.close()
    }


    private fun requestAllPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this@OblongActivity,
                arrayOf<String>(permission.READ_MEDIA_IMAGES),
                REQUESTCODE
            )
        } else {
            ActivityCompat.requestPermissions(
                this@OblongActivity, arrayOf(
                    permission.READ_EXTERNAL_STORAGE,
                    permission.WRITE_EXTERNAL_STORAGE
                ), REQUESTCODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUESTCODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@OblongActivity, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

}




