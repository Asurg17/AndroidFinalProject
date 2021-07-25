package ge.asurguladze.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    lateinit var signIn: Button
    lateinit var signUp: Button

    lateinit var nickname: EditText
    lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("message")

        myRef.setValue("Hello, MAMASITA!")

        initializeViews()
        setListeners()
    }

    private fun initializeViews() {
        signIn = findViewById(R.id.sign_in)
        signUp = findViewById(R.id.sign_up)

        nickname = findViewById(R.id.nickname)
        password = findViewById(R.id.password)
    }

    private fun setListeners() {
        signIn.setOnClickListener {
            var a = 1
        }

        signUp.setOnClickListener {
            goToRegistrationPage()
        }
    }

    private fun goToRegistrationPage() {
        val intent = Intent(this, RegistrationPage::class.java)
        startActivity(intent)
    }

}

