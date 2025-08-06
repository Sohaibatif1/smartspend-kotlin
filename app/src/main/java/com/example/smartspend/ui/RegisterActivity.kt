package com.example.smartspend.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartspend.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val nameField: EditText = findViewById(R.id.NameEditText)
        val contactField: EditText = findViewById(R.id.contactEditText)
        val addressField: EditText = findViewById(R.id.addressEditText)
        val emailField: EditText = findViewById(R.id.emailEditText)
        val passwordField: EditText = findViewById(R.id.passwordEditText)
        val registerBtn: Button = findViewById(R.id.registerBtn)

        registerBtn.setOnClickListener {
            val name = nameField.text.toString().trim()
            val contact = contactField.text.toString().trim()
            val address = addressField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (name.isEmpty() || contact.isEmpty() || address.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!contact.matches(Regex("^\\d{11}$"))) {
                contactField.error = "Contact must be exactly 11 digits"
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailField.error = "Enter a valid email address"
                return@setOnClickListener
            }

            if (!password.matches(Regex(".*[A-Z].*"))) {
                passwordField.error = "Password must have at least 1 uppercase letter"
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser!!.uid
                    val profileData = hashMapOf(
                        "name" to name,
                        "contact" to contact,
                        "address" to address,
                        "email" to email
                    )
                    db.collection("users")
                        .document(uid)
                        .set(profileData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Profile save failed: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                } else {
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
