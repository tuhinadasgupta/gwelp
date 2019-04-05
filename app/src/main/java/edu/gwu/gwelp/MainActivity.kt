package edu.gwu.gwelp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var firebaseAuth: FirebaseAuth

    // Anonymous class implementing TextWatcher
    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val inputtedUsername: String = username.text.toString().trim()
            val inputtedPassword: String = password.text.toString().trim()
            val enableButton: Boolean = inputtedUsername.isNotEmpty() && inputtedPassword.isNotEmpty()

            login.isEnabled = enableButton
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        progressBar = findViewById(R.id.progressBar)

        username.addTextChangedListener(textWatcher)
        password.addTextChangedListener(textWatcher)

        login.setOnClickListener {
            val inputtedUsername: String = username.text.toString().trim()
            val inputtedPassword: String = password.text.toString().trim()

            firebaseAuth.signInWithEmailAndPassword(
                inputtedUsername,
                inputtedPassword
            ).addOnSuccessListener { result->
                val currentUser: FirebaseUser? = firebaseAuth.currentUser
                Toast.makeText(
                    this,
                    "Logged in as: ${currentUser!!.email}",
                    Toast.LENGTH_LONG
                ).show()

                // Advance to the next screen
//                val intent: Intent = Intent(this, ::class.java)
//                startActivity(intent)
            }.addOnFailureListener { exception->
                when (exception) {
                    is FirebaseAuthInvalidCredentialsException ->
                        Toast.makeText(
                            this,
                            "Invalid credentials, try again",
                            Toast.LENGTH_LONG
                        ).show()
                    is FirebaseAuthInvalidUserException ->
                        Toast.makeText(
                            this,
                            "We don't recognize that username, want to sign up?",
                            Toast.LENGTH_LONG
                        ).show()
                    else ->
                        Toast.makeText(
                            this,
                            "Failed to login: $exception",
                            Toast.LENGTH_LONG
                        ).show()
                }
            }


        }
    }


}
