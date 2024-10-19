package com.example.tinytales

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tinytales.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    //process dialog
    private lateinit var progressDialog: ProgressDialog

    // firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        // firestore
        val db = FirebaseFirestore.getInstance()
        val usersCollection = db.collection("users")

        // init process dialog, will show while create account | register user
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.registerButton.setOnClickListener{
            /* Step
            * 1. Nhap du lieu
            * 2. xác thực dữ liệu
            * 3. Tao tai khoan - Firebase Auth
            * 4. Luu du lieu - Firebase Realtime Database */

            ValidateData()
        }

    }

    private var email = ""
    private var password = ""
    private var phone = ""


    private fun ValidateData() {
        //1 Nhap du lieu
        email = binding.emailET.text.toString().trim()
        password = binding.passwordET.text.toString().trim()
        phone = binding.phoneET.text.toString().trim()
        val rePassword = binding.rePasswordET.text.toString().trim()

        //2 xác thực dữ liệu
        if(email.isEmpty()) {
            Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show()
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Invalid Email Pattern", Toast.LENGTH_SHORT).show()
        }else if(password.isEmpty()) {
            Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show()
        }else if(rePassword != password){
            Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show()
        }else if(phone.isEmpty()){
            Toast.makeText(this, "Enter your phone", Toast.LENGTH_SHORT).show()
        }else{
            CreateUserAccount()
        }
    }

    private fun CreateUserAccount(){
        //3. Tao tai khoan - Firebase Auth

        // Hien thi trang thai
        progressDialog.setMessage("Creating your account....")
        progressDialog.show()

        // Tao user trong firebase
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // Tao tai khoan thanh cong
                UpdateUserInfo()
            }
            .addOnFailureListener{ e->
                // Tao tai khoan that bai
                progressDialog.dismiss()
                Toast.makeText(this, "Failed creating account due to ${e.message}", Toast.LENGTH_SHORT).show()

            }
    }

    private fun UpdateUserInfo(){
        //4. Luu du lieu - Firebase Realtime Database
        progressDialog.setMessage("Saving user info....")

        val user = User(
            email = binding.emailET.text.toString().trim(),
            phone = binding.phoneET.text.toString().trim(),
            password = binding.passwordET.text.toString().trim()
        )

        val db = Firebase.firestore
        db.collection("User")
            .add(user)
            .addOnSuccessListener { documentReference ->
                startActivity(Intent(this@RegisterActivity, DashbroadUserActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error adding user: ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }
}