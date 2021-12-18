package com.devrakan.rshop.ui.Fragment.Manager

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devrakan.rshop.R
import com.devrakan.rshop.ui.AddProductActivity
import kotlinx.android.synthetic.main.fragment_settings.view.*


class SettingsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view = inflater.inflate(R.layout.fragment_settings, container, false)
        view.btn_add_product_intint.setOnClickListener{
            startActivity(Intent(activity?.applicationContext,AddProductActivity::class.java))

        }
        return view
    }


    }
