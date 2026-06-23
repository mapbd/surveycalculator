package org.map_bd.surveycalculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.map_bd.surveycalculator.databinding.ActivityUconverterBinding
import android.view.View


class UconverterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUconverterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    binding = ActivityUconverterBinding.inflate(layoutInflater)
    setContentView(binding.root)

    //binding.toolbar.title = "Home"
    setSupportActionBar(binding.toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)



        binding.buttonareaId.setOnClickListener{
            val area = Intent(this, AreaActivity::class.java);
            startActivity(area)
        }

        binding.buttonstorageId.setOnClickListener{
            val store = Intent(this, DataStorageActivity::class.java);
            startActivity(store)
        }

        binding.buttonlengthId.setOnClickListener{
            val length = Intent(this, LengthActivity::class.java);
            startActivity(length)
        }

        binding.buttonmassId.setOnClickListener{
            val mass = Intent(this, MassActivity::class.java);
            startActivity(mass)
        }

        binding.buttonspeedId.setOnClickListener{
            val speed = Intent(this, SpeedActivity::class.java);
            startActivity(speed)
        }

        binding.buttontemperatureId.setOnClickListener{
            val temperature = Intent(this, TemperatureActivity::class.java);
            startActivity(temperature)
        }

        binding.buttontimeId.setOnClickListener{
            val time = Intent(this, TimeActivity::class.java);
            startActivity(time)
        }

        binding.buttonvolumeId.setOnClickListener{
            val volume = Intent(this, VolumeActivity::class.java);
            startActivity(volume)
        }




    }




}