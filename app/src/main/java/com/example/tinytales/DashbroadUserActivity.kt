package com.example.tinytales

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.tinytales.databinding.ActivityDashbroadUserBinding

class DashbroadUserActivity : AppCompatActivity() {

    lateinit var binding: ActivityDashbroadUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashbroadUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_dashbroad, HomeFragment())
            commit()
        }


        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.Home -> supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_dashbroad, HomeFragment())
                    commit()
                }

                R.id.Category -> supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_dashbroad, CategoryFragment())
                    commit()
                }

                R.id.Cart -> supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_dashbroad, CartFragment())
                    commit()
                }

                R.id.Account -> supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_dashbroad, AccountFragment())
                    commit()
                }
            };true
        }
    }
}