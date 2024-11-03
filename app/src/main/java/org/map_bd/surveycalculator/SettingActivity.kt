@file:Suppress("NAME_SHADOWING")

package org.map_bd.surveycalculator

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import org.map_bd.surveycalculator.databinding.ActivitySettingBinding
import java.util.Locale

@Suppress("DEPRECATION", "UNUSED_ANONYMOUS_PARAMETER")
class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadLocales()

        binding.toolbar.title = "Setting"
        setSupportActionBar(binding.toolbar)
       // supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Restore the switch state from SharedPreferences
        val preferences = getSharedPreferences("Settings", MODE_PRIVATE)
        val isNightMode = preferences.getBoolean("App_night_mode", false)
        binding.themSwitch.isChecked = isNightMode

        binding.changeLanguageCard.setOnClickListener {
            changeLanguage()
        }

        // Inside the switch listener in SettingActivity
        binding.themSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            // Save the switch state in SharedPreferences
            val preferences = getSharedPreferences("Settings", MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putBoolean("App_night_mode", isChecked)
            editor.apply()

            // Set the night mode immediately when the switch is toggled
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.aboutId -> {
                val about = Intent(this,AboutActivity::class.java);
                startActivity(about)
                onBackPressed()
                return true
            }
            R.id.settingId -> {
                Toast.makeText(this,"Settings", Toast.LENGTH_LONG).show()

            }
            R.id.basicCal ->{
                val calculator = Intent(this,BasicalculatorActivity::class.java);
                startActivity(calculator)
                onBackPressed()
                return true
            }
        }
        return true
    }

    private fun changeLanguage() {
        val language = arrayOf("English", "বাংলা")
        val mBuilder = AlertDialog.Builder(this)
        mBuilder.setTitle("Choose Language")
        mBuilder.setSingleChoiceItems(language, -1) { dialog, which ->
            val languageCode = when (which) {
                0 -> "en"
                1 -> "bn"
                else -> "en"
            }
            setLocale(languageCode)
            dialog.dismiss()

            // Recreate MainActivity to apply language change
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        val dialog = mBuilder.create()
        dialog.show()
    }


    private fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val configuration = Configuration()
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)

        val editor = getSharedPreferences("Settings", MODE_PRIVATE).edit()
        editor.putString("App_lang", language)
        editor.apply()
    }

    private fun loadLocales() {
        val preferences = getSharedPreferences("Settings", MODE_PRIVATE)
        val language = preferences.getString("App_lang", "")
        if (language != null) {
            setLocale(language)
        }
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == android.R.id.home) {
//            onBackPressed()
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }
}