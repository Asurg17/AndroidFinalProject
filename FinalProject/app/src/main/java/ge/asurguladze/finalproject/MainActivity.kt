package ge.asurguladze.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    lateinit var signIn: Button
    lateinit var signUp: Button

    lateinit var nickname: EditText
    lateinit var password: EditText

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Write a message to the database
        val database = Firebase.database

        // Initialize Firebase Auth
        auth = Firebase.auth

        initializeViews()
        setListeners()

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
//            reload();
            var a = 2
        }
    }

    private fun initializeViews() {
        signIn = findViewById(R.id.sign_in)
        signUp = findViewById(R.id.sign_up)

        nickname = findViewById(R.id.nickname)
        password = findViewById(R.id.password)
    }

    private fun setListeners() {

        signIn.setOnClickListener {

            if (checkIfAllValuesAreFilled()) {

                signIn(nickname.text.toString(), password.text.toString())

            }else{

                showToast("Please fill in all the fields")

            }

        }

        signUp.setOnClickListener {
            goToRegistrationPage()
        }
    }

    private fun signIn(nickname:String, password:String){

        auth.signInWithEmailAndPassword("$nickname@gmail.com", password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser

                    goToMainPage()

//                    updateUI(user)

                } else {

                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    showToast("Authentication failed: " + task.exception?.message)

//                    updateUI(null)

                }
            }
    }

    private fun goToRegistrationPage() {
        val intent = Intent(this, RegistrationPage::class.java)
        startActivity(intent)
    }

    private fun goToMainPage(){
        val intent = Intent(this, MainPage::class.java)
        startActivity(intent)
    }


    private fun checkIfAllValuesAreFilled(): Boolean {

        if(nickname.text.isNotEmpty() && password.text.isNotEmpty()){
            return true
        }

        return false
    }

    private fun showToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    companion object{
        var TAG = "TAG"
    }

}

