package com.example.tinytales

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tinytales.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

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

        // timestamp
        val timestamp = System.currentTimeMillis()

        val uid = firebaseAuth.uid

        if (uid == null) {
            progressDialog.dismiss()
            Toast.makeText(this, "UID is null, cannot save user info", Toast.LENGTH_SHORT).show()
        }

        // tao hashmap de luu du lieu
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["uid"] = uid
        hashMap["email"] = email
        hashMap["phone"] = phone
        hashMap["name"] = ""
        hashMap["profileImage"] = ""
        hashMap["userType"] = "user"
        hashMap["timestamp"] = timestamp

        //luu du lieu
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(uid!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                // thanh cong
                progressDialog.dismiss()
                Toast.makeText(this, "Account created....", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@RegisterActivity, DashbroadUserActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Failed saving user info due to ${e.message}", Toast.LENGTH_SHORT).show()

            }

    }
}