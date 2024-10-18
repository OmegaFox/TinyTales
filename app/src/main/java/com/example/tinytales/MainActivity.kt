package com.example.tinytales

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tinytales.databinding.ActivityMainBinding
import com.example.tinytales.ui.theme.TinyTalesTheme

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // handle click, login
        binding.loginButton.setOnClickListener {
            val gotoLogin = Intent(this, LoginActivity::class.java)
            startActivity(gotoLogin)
        }

        // handle click, skip login
        binding.skiploginButton.setOnClickListener {
            val gotoskipLogin = Intent(this, DashbroadUserActivity::class.java)
            startActivity(gotoskipLogin)
        }
    }
}