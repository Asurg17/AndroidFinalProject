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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.asurguladze.finalproject.models.User

class RegistrationPage : AppCompatActivity() {

    private lateinit var signUp: Button

    private lateinit var nickname: EditText
    private lateinit var password: EditText
    private lateinit var profession: EditText

    private lateinit var database : FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_page)

        initializeViews()
        setListeners()
    }

    private fun initializeViews() {

        // Initialize Firebase Auth
        auth = Firebase.auth

        signUp = findViewById(R.id.sign_up)

        nickname = findViewById(R.id.nickname)
        password = findViewById(R.id.password)
        profession = findViewById(R.id.profession)

        database = Firebase.database
    }

    private fun setListeners() {
        signUp.setOnClickListener {
            registerClient()
        }
    }

    private fun registerClient() {

        if (checkIfAllValuesAreFilled()) {

            if(nickname.text.toString().contains(" ")){
                showToast("You can't use spaces in nickname!")
            }else{
                createUser(nickname.text.toString(), password.text.toString(), profession.text.toString())
            }

        }else{

            showToast("Please fill in all the fields")

        }

    }

    private fun createUser(nickname:String, password:String, profession:String) {

        val users =  database.getReference("users")

        auth.createUserWithEmailAndPassword("$nickname@gmail.com", password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    // Sign in success, update UI with the signed-in user's information
                    Log.d("B:A", "createUserWithEmail:success")

                    users.push().key?.let{
                        users.child(nickname).setValue(
                            User(nickname, password, profession)
                        )
                    }

                    goToMainPage()

                } else {

                    // If sign in fails, display a message to the user.
                    Log.w("B:A", "createUserWithEmail:failure", task.exception)
                    showToast("Authentication failed: " + task.exception?.message)

                }
            }
    }

    private fun goToMainPage(){
        val intent = Intent(this, MainPage::class.java)
        startActivity(intent)
    }


    private fun checkIfAllValuesAreFilled(): Boolean {

        if(nickname.text.isNotEmpty() && password.text.isNotEmpty() && profession.text.isNotEmpty()){
            return true
        }

        return false
    }

    private fun showToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

}