package ge.asurguladze.finalproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var signIn: Button
    private lateinit var signUp: Button

    lateinit var nickname: EditText
    lateinit var password: EditText

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setListeners()
    }

    public override fun onStart() {
        super.onStart()

        auth = Firebase.auth

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser

        if(currentUser != null){
            goToMainPage()
        }
    }

    private fun initializeViews() {
        // Initialize Firebase Auth
        auth = Firebase.auth

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

