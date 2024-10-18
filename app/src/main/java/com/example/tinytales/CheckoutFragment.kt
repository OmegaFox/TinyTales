package com.example.tinytales

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

/**
 * A simple [Fragment] subclass.
 * Use the [CheckoutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CheckoutFragment : Fragment(R.layout.fragment_checkout) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_checkout, container, false)

        val payButton: Button = view.findViewById(R.id.pay)

        payButton.setOnClickListener {
            val intent = Intent(requireContext(), PaymentReceivedActivity::class.java)
            startActivity(intent)
        }
        return view
    }
}