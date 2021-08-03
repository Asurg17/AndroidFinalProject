package ge.asurguladze.finalproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainPage : AppCompatActivity() {

    private lateinit var search: TextInputLayout
    private lateinit var plusButton: FloatingActionButton
    private lateinit var homeButton: ImageButton
    private lateinit var settingsButton: ImageButton

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        auth = Firebase.auth

        val currentUser = auth.currentUser
        Log.d("user_our_user", currentUser?.email.toString())

        initializeViews()
        setListeners()
    }

    private fun initializeViews() {
        search = findViewById(R.id.outlinedTextField)
        plusButton = findViewById(R.id.plus)
        homeButton = findViewById(R.id.home)
        settingsButton = findViewById(R.id.settings)
    }

    private fun setListeners() {

        search.setEndIconOnClickListener{
            showToast("search")
        }

        plusButton.setOnClickListener{
            goToSearchPage()
        }

        settingsButton.setOnClickListener {
            goToProfilePage()
        }

    }

    private fun goToProfilePage(){
        val intent = Intent(this, ProfilePage::class.java)
        startActivity(intent)
    }

    private fun goToSearchPage(){
        val intent = Intent(this, SearchPage::class.java)
        startActivity(intent)
    }

    private fun showToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}