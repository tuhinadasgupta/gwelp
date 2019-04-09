package edu.gwu.gwelp

import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser



class SignUp: AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    //text watcher so you can't submit unless passwords match
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val username:String =intent.getStringExtra("Username")
        val password1:String = intent.getStringExtra("Password")

        firebaseAuth.createUserWithEmailAndPassword(
            username,
            password1
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // If Sign Up is successful, Firebase automatically logs
                // in as that user too (e.g. currentUser is set)
                val currentUser: FirebaseUser? = firebaseAuth.currentUser
                Toast.makeText(
                    this,
                    "Registered as: ${currentUser!!.email}",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                val exception = task.exception
                Toast.makeText(
                    this,
                    "Failed to register: $exception",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}