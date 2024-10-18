package com.example.tinytales

import android.os.Bundle
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.tinytales.databinding.FragmentCartBinding

/**
 * A simple [Fragment] subclass.
 * Use the [CartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CartFragment : Fragment(R.layout.fragment_cart) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        // Tìm Button trong layout
        val CheckoutButton: Button = view.findViewById(R.id.checkout)

        CheckoutButton.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.frame_dashbroad, CheckoutFragment())
                commit()
            }
        }
        return view
    }

}