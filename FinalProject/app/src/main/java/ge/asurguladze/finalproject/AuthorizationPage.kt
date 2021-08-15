package ge.asurguladze.finalproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthorizationPage : AppCompatActivity() {

    private lateinit var signIn: Button
    private lateinit var signUp: Button

    lateinit var nickname: EditText
    lateinit var password: EditText

    private lateinit var auth: FirebaseAuth

    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization_page)

        initializeViews()
        setListeners()
    }

    private fun initializeViews() {
        // Initialize Firebase Auth
        auth = Firebase.auth

        signIn = findViewById(R.id.sign_in)
        signUp = findViewById(R.id.sign_up)

        nickname = findViewById(R.id.nickname)
        password = findViewById(R.id.password)

        dialog = AlertDialog.Builder(this)
            .setView(R.layout.loading_dialog_layout)
            .setCancelable(false)
            .create()
    }

    private fun setListeners() {

        signIn.setOnClickListener {

            if (checkIfAllValuesAreFilled()) {

                signIn(nickname.text.toString(), password.text.toString())

            }else{

                showToast(getString(R.string.not_all_fields_are_filled))

            }

        }

        signUp.setOnClickListener {
            goToRegistrationPage()
        }
    }

    private fun signIn(nickname:String, password:String){

        dialog.show()

        auth.signInWithEmailAndPassword("$nickname@gmail.com", password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    dialog.dismiss()
                    goToMainPage()

                } else {
                    showToast("Authentication failed: " + task.exception?.message)
                }
            }
    }

    private fun goToRegistrationPage() {
        val intent = Intent(this, RegistrationPage::class.java)
        startActivity(intent)
    }

    private fun goToMainPage(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


    private fun checkIfAllValuesAreFilled(): Boolean {

        if(nickname.text.isNotEmpty() && password.text.isNotEmpty()){
            return true
        }

        return false
    }

    private fun showToast(text: String){
        dialog.dismiss()
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

}

