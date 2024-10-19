package com.example.tinytales

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

/**
 * A simple [Fragment] subclass.
 * Use the [EditUserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditUserFragment : Fragment(R.layout.fragment_edit_user) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_user, container, false)

        val saveButton: Button = view.findViewById(R.id.saveButton)

        saveButton.setOnClickListener{
            saveUserData()
        }



        return view
    }

    private fun saveUserData(){

    }


}