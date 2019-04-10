package edu.gwu.gwelp

import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import android.widget.Button
import com.google.firebase.auth.FirebaseUser



class SignUp: AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var cpassword: EditText
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var csignup: Button

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val inputtedUsername: String = username.text.toString().trim()
            val inputtedPassword: String = password.text.toString().trim()
            val inputtedCPassword: String = cpassword.text.toString().trim()
            val enableButton: Boolean = inputtedUsername.isNotEmpty() && inputtedPassword.isNotEmpty() && inputtedCPassword.isNotEmpty()
            csignup.isEnabled = enableButton
        }
    }

    //text watcher so you can't submit unless passwords match
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        cpassword = findViewById(R.id.password)
        firebaseAuth = FirebaseAuth.getInstance()
        cpassword = findViewById(R.id.cpassword)
        csignup = findViewById(R.id.confirm)

        username.addTextChangedListener(textWatcher)
        password.addTextChangedListener(textWatcher)
        cpassword.addTextChangedListener(textWatcher)

        csignup.setOnClickListener {
            val inputtedUsername: String = username.text.toString().trim()
            val inputtedPassword: String = password.text.toString().trim()
            val inputtedCPassword: String = cpassword.text.toString().trim()

            if (inputtedCPassword.equals(inputtedPassword)) {
                firebaseAuth.createUserWithEmailAndPassword(
                    inputtedUsername,
                    inputtedPassword
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
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
            else {
                Toast.makeText(
                    this,
                    "Your passwords don't match!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}