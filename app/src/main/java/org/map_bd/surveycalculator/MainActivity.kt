package org.map_bd.surveycalculator


import LanguageManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import org.map_bd.surveycalculator.R.*
import org.map_bd.surveycalculator.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fragmentManager: FragmentManager
    private lateinit var languageManager: LanguageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = "Home"
        setSupportActionBar(binding.toolbar)

        fragmentManager = supportFragmentManager
        languageManager = LanguageManager(this)

        binding.areaCardView.setOnClickListener{
            openFragment(LandsFragment())

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
                }
                R.id.settingId -> {
                    val settings = Intent(this,SettingActivity::class.java);
                    startActivity(settings)
                }
                R.id.basicCal ->{
                    val calculator = Intent(this,BasicalculatorActivity::class.java);
                    startActivity(calculator)
                }
        }
        return true
    }




    private fun openFragment(fragment: Fragment){
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(id.fragment_container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }





}