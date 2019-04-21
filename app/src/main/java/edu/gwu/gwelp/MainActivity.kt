package edu.gwu.gwelp

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var signUp: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var rememberUsername: Switch
    private lateinit var rememberPassword: Switch
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

        // Pass the name and the file-create mode (private to the app)
        val sharedPrefs = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)

        firebaseAuth = FirebaseAuth.getInstance()

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        signUp = findViewById(R.id.signUp)
        progressBar = findViewById(R.id.progressBar)

        username.addTextChangedListener(textWatcher)
        password.addTextChangedListener(textWatcher)


        signUp.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
        rememberUsername = findViewById(R.id.rememberSwitchUsername)
        rememberPassword = findViewById(R.id.rememberSwitchPassword)

        username.addTextChangedListener(textWatcher)
        password.addTextChangedListener(textWatcher)

        // Reading from preferences, indicate default if not present
        val savedUsername = sharedPrefs.getString("SAVED_USERNAME", "")
        val savedPassword = sharedPrefs.getString("SAVED_PASSWORD", "")

        // Based on prefs, set switches and text inputs
        rememberUsername.isChecked = !savedUsername.isNullOrEmpty()
        rememberPassword.isChecked = !savedPassword.isNullOrEmpty()
        
        // Set the username and password to be the saved text
        username.setText(savedUsername)
        password.setText(savedPassword)
        
        // Listen for whether Remember Username switch is on or off
        rememberUsername.setOnCheckedChangeListener { view, isChecked ->
            if (isChecked) {
                // Write to preferences (make sure to call apply)
                sharedPrefs.edit().putString("SAVED_USERNAME", username.text.toString()).apply()
            } else {
                // Remove saved destination when user unchecks switch
                sharedPrefs.edit().remove("SAVED_USERNAME").apply()
            }
        }
        // Listen for whether Remember Password switch is on or off
        rememberPassword.setOnCheckedChangeListener { view, isChecked ->
            if (isChecked) {
                sharedPrefs.edit().putString("SAVED_PASSWORD", password.text.toString()).apply()
            } else {
                sharedPrefs.edit().remove("SAVED_PASSWORD").apply()
            }
        }

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
                val intent: Intent = Intent(this, LandmarksActivity::class.java)
                startActivity(intent)
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
