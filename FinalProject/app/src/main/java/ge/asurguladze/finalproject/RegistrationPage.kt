package ge.asurguladze.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

    private lateinit var dialog: AlertDialog

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

        dialog = AlertDialog.Builder(this)
            .setView(R.layout.loading_dialog_layout)
            .setCancelable(false)
            .create()

    }

    private fun setListeners() {
        signUp.setOnClickListener {
            registerClient()
        }
    }

    private fun registerClient() {

        if (checkIfAllValuesAreFilled()) {

            if(nickname.text.toString().contains(" ")){
                showToast(getString(R.string.no_spaces_warning))
            }else{

                if(checkForUppercase(nickname.text.toString())) {
                    showToast(getString(R.string.no_uppercase_letters_allowed))
                }else{
                    createUser(
                        nickname.text.toString(),
                        password.text.toString(),
                        profession.text.toString()
                    )
                }
            }

        }else{

            showToast(getString(R.string.not_all_fields_are_filled))

        }

    }

    private fun checkForUppercase(nickname: String): Boolean {

        for(char in nickname){
            if(char.isUpperCase()){
                return true
            }
        }

        return false
    }

    private fun createUser(nickname:String, password:String, profession:String) {

        dialog.show()

        val users =  database.getReference("users")

        auth.createUserWithEmailAndPassword("$nickname@gmail.com", password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    users.push().key?.let{
                        users.child(nickname).setValue(
                            User(nickname, password, profession, null, false)
                        )
                    }

                    dialog.dismiss()
                    goToMainPage()

                } else {
                    showToast("Authentication failed: " + task.exception?.message)
                }
            }
    }

    private fun goToMainPage(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


    private fun checkIfAllValuesAreFilled(): Boolean {

        if(nickname.text.isNotEmpty() && password.text.isNotEmpty() && profession.text.isNotEmpty()){
            return true
        }

        return false
    }

    private fun showToast(text: String){
        dialog.dismiss()
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

}