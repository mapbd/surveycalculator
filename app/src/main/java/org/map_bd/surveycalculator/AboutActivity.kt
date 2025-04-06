package org.map_bd.surveycalculator

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.map_bd.surveycalculator.databinding.ActivityAboutBinding
import org.map_bd.surveycalculator.databinding.ActivitySettingBinding

@Suppress("DEPRECATION")
class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = "About"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.versionText.text = getCurrentVersion(packageManager, packageName)

        binding.mapbdId.setOnClickListener{
            openUrl("https://map-bd.org/")
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
                val calculator = Intent(this,BasicalculatorActivity::class.java);
                startActivity(calculator)
            }
            R.id.compassId ->{
                val compass = Intent(this,CompassActivity::class.java);
                startActivity(compass)
            }
        }
        return true
    }

    private fun getCurrentVersion(packageManager: PackageManager, packageName: String, flags: Int = 0): String{
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong())).versionName
            }else{
                packageManager.getPackageInfo(packageName, flags).versionName
            }

        }catch (ex: Exception){
            return ""
        }
    }

    private fun openUrl(link: String) {
        val uri = Uri.parse(link)
        val inte = Intent(Intent.ACTION_VIEW, uri)
        startActivity(inte)
    }


}

