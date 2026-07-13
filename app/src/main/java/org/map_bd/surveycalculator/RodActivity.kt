package org.map_bd.surveycalculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.map_bd.surveycalculator.databinding.ActivityRodBinding


@Suppress("DEPRECATION")
class RodActivity : AppCompatActivity() {


    lateinit var tabLayout: TabLayout
    lateinit var viewpager : ViewPager2

    private lateinit var binding: ActivityRodBinding

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.toolbar.title = "Home"
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        tabLayout = findViewById(R.id.tab_layout)
        viewpager = findViewById(R.id.view_pager)
        viewpager.adapter = ViewPagerFragmentAdapter(this)


        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.kg2pic)
                1 -> tab.text = getString(R.string.pic2kg)
            }
        }.attach()


    }


}