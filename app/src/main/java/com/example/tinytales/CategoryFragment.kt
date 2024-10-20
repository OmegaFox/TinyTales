package com.example.tinytales

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button


class CategoryFragment : Fragment(R.layout.fragment_category) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fictionButton = view.findViewById<Button>(R.id.FictionButton)
        val classicButton = view.findViewById<Button>(R.id.ClassicButton)
        val horrorButton = view.findViewById<Button>(R.id.HorrorButton)
        val fantasyButton = view.findViewById<Button>(R.id.FantasyButton)
        val dramaButton = view.findViewById<Button>(R.id.DramaButton)
        val scienceButton = view.findViewById<Button>(R.id.ScienceButton)
        val mangaButton = view.findViewById<Button>(R.id.MangaButton)
        val sciFiButton = view.findViewById<Button>(R.id.SciFiButton)

        fictionButton.setOnClickListener {
            openCategoryBookActivity("Fiction")
        }

        classicButton.setOnClickListener {
            openCategoryBookActivity("Classic")
        }

        horrorButton.setOnClickListener {
            openCategoryBookActivity("Horror")
        }

        fantasyButton.setOnClickListener {
            openCategoryBookActivity("Fantasy")
        }

        dramaButton.setOnClickListener {
            openCategoryBookActivity("Drama")
        }

        scienceButton.setOnClickListener {
            openCategoryBookActivity("Science")
        }

        mangaButton.setOnClickListener {
            openCategoryBookActivity("Manga")
        }

        sciFiButton.setOnClickListener {
            openCategoryBookActivity("Sci-fi")
        }
    }

    private fun openCategoryBookActivity(category: String) {
        val intent = Intent(requireContext(), CategoryBookActivity::class.java)
        intent.putExtra("category", category)
        startActivity(intent)
    }

}