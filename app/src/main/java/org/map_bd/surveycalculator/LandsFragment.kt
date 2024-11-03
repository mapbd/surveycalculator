package org.map_bd.surveycalculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class LandsFragment : Fragment(){

    private lateinit var fragmentManager: FragmentManager


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_lands, container, false)

        fragmentManager = parentFragmentManager

        val area: CardView = view.findViewById(R.id.area_cardViews)

        area.setOnClickListener{
            Toast.makeText(view.context,"Social Media",Toast.LENGTH_LONG).show()
        }

        return view

    }

    private fun openFragment(fragment: Fragment){
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


}