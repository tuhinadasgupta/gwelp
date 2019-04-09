package edu.gwu.gwelp



import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
class MainActivity : AppCompatActivity() {
    /*
    * These variables are "lateinit" because can't actually assign a value to them until
    * onCreate() is called (e.g. we are promising to the Kotlin compiler that these will be
    * "initialized later").
    *
    * Alternative is to make them nullable and set them equal to null, but that's not as nice to
    * work with.
    *   private var username: EditText? = null
    */
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var signUp: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var firebaseAuth: FirebaseAuth
    /**
     * We're creating an "anonymous class" here (e.g. we're creating a class which implements
     * TextWatcher, but not creating an explicit class).
     *
     * object : TextWatcher == "creating a new object which implements TextWatcher"
     */
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
    private val textWatcher2: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val inputtedUsername2: String = username.text.toString().trim()
            val inputtedPassword2: String = password.text.toString().trim()
            val enableButton2: Boolean = inputtedUsername2.isNotEmpty() && inputtedPassword2.isNotEmpty()
            signUp.isEnabled = enableButton2
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAuth = FirebaseAuth.getInstance()
        signUp = findViewById(R.id.signUp)
        Log.d("MainActivity", "onCreate called")
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        progressBar = findViewById(R.id.progressBar)
        username.addTextChangedListener(textWatcher)
        password.addTextChangedListener(textWatcher)
        username.addTextChangedListener(textWatcher2)
        password.addTextChangedListener(textWatcher2)

        signUp.setOnClickListener {
            val inputtedUsername: String = username.text.toString().trim()
            val inputtedPassword: String = password.text.toString().trim()
            val intent = Intent(this, SignUp::class.java)
            intent.putExtra("Username", inputtedUsername)
            intent.putExtra("Password", inputtedPassword)
            startActivity(intent)
//            firebaseAuth.createUserWithEmailAndPassword(
//                inputtedUsername,
//                inputtedPassword
//            ).addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    // If Sign Up is successful, Firebase automatically logs
//                    // in as that user too (e.g. currentUser is set)
//                    val currentUser: FirebaseUser? = firebaseAuth.currentUser
//                    Toast.makeText(
//                        this,
//                        "Registered as: ${currentUser!!.email}",
//                        Toast.LENGTH_LONG
//                    ).show()
//                } else {
//                    val exception = task.exception
//                    Toast.makeText(
//                        this,
//                        "Failed to register: $exception",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            }
        }
        // This is similar to the TextWatcher -- setOnClickListener takes a View.OnClickListener
        // as a parameter, which is an **interface with only one method**, so in this special case
        // you can just use a lambda (e.g. just open brances) instead of doing
        //      object : View.OnClickListener { ... }
        login.setOnClickListener {
            val inputtedUsername: String = username.text.toString().trim()
            val inputtedPassword: String = password.text.toString().trim()
            firebaseAuth.signInWithEmailAndPassword(
                inputtedUsername,
                inputtedPassword
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser: FirebaseUser? = firebaseAuth.currentUser
                    Toast.makeText(
                        this,
                        "Logged in as: ${currentUser!!.email}",
                        Toast.LENGTH_LONG
                    ).show()
                    //login successful take user to new page

                } else {
                    val exception = task.exception
                    Toast.makeText(
                        this,
                        "Failed to login: $exception",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        Log.d("MainActivity", "onStart called")
    }
    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume called")
    }
    override fun onPause() {
        super.onPause()
        Log.d("MainActivity", "onPause called")
    }
    override fun onStop() {
        super.onStop()
        Log.d("MainActivity", "onStop called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "onDestroy called")
    }
}