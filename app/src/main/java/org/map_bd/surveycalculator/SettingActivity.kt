@file:Suppress("NAME_SHADOWING")

package org.map_bd.surveycalculator

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Button
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

        // 1. MUST load locale BEFORE calling super.onCreate or binding to avoid UI layout bugs
        loadLocales()

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Dynamically get the title from strings.xml to respect the current language
        binding.toolbar.title = getString(R.string.setting_title_res) // Replace with your string ID
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Recommended for UX

        val preferences = getSharedPreferences("Settings", MODE_PRIVATE)
        val isNightMode = preferences.getBoolean("App_night_mode", false)
        binding.themSwitch.isChecked = isNightMode

        binding.changeLanguageCard.setOnClickListener {
            changeLanguage()
        }

        binding.themSwitch.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit().putBoolean("App_night_mode", isChecked).apply()

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

//        val btnLight = findViewById<Button>(R.id.btnLight)
//        val btnDark = findViewById<Button>(R.id.btnDark)
//        val btnSystem = findViewById<Button>(R.id.btnSystem)
//
//        // Force Light Mode
//        btnLight.setOnClickListener {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        }
//
//        // Force Dark Mode
//        btnDark.setOnClickListener {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//        }
//
//        // Follow System Settings
//        btnSystem.setOnClickListener {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle Toolbar Back Navigation Arrow
//        if (item.itemId == android.R.id.home) {
//            finish()
//            return true
//        }
//
//        when(item.itemId){
//            R.id.aboutId -> {
//                startActivity(Intent(this, AboutActivity::class.java))
//                finish() // Use finish() instead of calling onBackPressed() manually
//                return true
//            }
//            R.id.settingId -> {
//                Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show()
//                return true
//            }
//            R.id.basicCal -> {
//                startActivity(Intent(this, BasicalculatorActivity::class.java))
//                finish()
//                return true
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    private fun changeLanguage() {
        val languages = arrayOf("English", "বাংলা")
        val preferences = getSharedPreferences("Settings", MODE_PRIVATE)
        val currentLang = preferences.getString("App_lang", "en")

        // Find which language is currently checked
        val checkedItem = if (currentLang == "bn") 1 else 0

        AlertDialog.Builder(this)
            .setTitle("Choose Language") // Ideally use getString(R.string.choose_language)
            .setSingleChoiceItems(languages, checkedItem) { dialog, which ->
                val languageCode = when (which) {
                    0 -> "en"
                    1 -> "bn"
                    else -> "en"
                }

                if (languageCode != currentLang) {
                    setLocale(languageCode)
                    dialog.dismiss()

                    // Recreate THIS activity so the user immediately sees the localization change
                    val refresh = Intent(this, SettingActivity::class.java)
                    startActivity(refresh)
                    finish()
                    // Override transition to prevent screen blinking during recreation
                    overridePendingTransition(0, 0)
                } else {
                    dialog.dismiss()
                }
            }
            .create()
            .show()
    }

//    private fun setLocale(language: String) {
//        val locale = Locale(language)
//        Locale.setDefault(locale)
//
//        val configuration = Configuration()
//        configuration.setLocale(locale)
//        resources.updateConfiguration(configuration, resources.displayMetrics)
//
//        getSharedPreferences("Settings", MODE_PRIVATE).edit()
//            .putString("App_lang", language)
//            .apply()
//    }
//
//    private fun loadLocales() {
//        val preferences = getSharedPreferences("Settings", MODE_PRIVATE)
//        val language = preferences.getString("App_lang", "en") ?: "en"
//        setLocale(language)
//    }

    private fun setLocale(language: String) {
        // Force "en" if string is blank or invalid
        val targetLanguage = if (language.isBlank()) "en" else language
        val locale = Locale(targetLanguage)
        Locale.setDefault(locale)

        val configuration = Configuration()
        configuration.setLocale(locale)

        // Fallback context implementation
        baseContext.resources.updateConfiguration(configuration, baseContext.resources.displayMetrics)

        // Save selected language to SharedPreferences
        getSharedPreferences("Settings", MODE_PRIVATE).edit()
            .putString("App_lang", targetLanguage)
            .apply()
    }

    private fun loadLocales() {
        val preferences = getSharedPreferences("Settings", MODE_PRIVATE)
        // "en" acts as the fallback default if "App_lang" doesn't exist yet
        val language = preferences.getString("App_lang", "en") ?: "en"
        setLocale(language)
    }
}