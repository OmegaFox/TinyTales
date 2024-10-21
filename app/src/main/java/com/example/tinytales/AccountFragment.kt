package com.example.tinytales

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class AccountFragment : Fragment(R.layout.fragment_account) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        val editProfileButton: Button = view.findViewById(R.id.userProfileButton)

        editProfileButton.setOnClickListener{
           parentFragmentManager.beginTransaction().apply {
               replace(R.id.frame_dashbroad, EditUserFragment())
               commit()
           }
        }





        return view
    }

}