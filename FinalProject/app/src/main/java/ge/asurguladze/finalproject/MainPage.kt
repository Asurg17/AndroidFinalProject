package ge.asurguladze.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainPage : AppCompatActivity() {

    lateinit var search: TextInputLayout
    lateinit var plusButton: FloatingActionButton
    lateinit var homeButton: ImageButton
    lateinit var settingsButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

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
            showToast("plus")
        }

        homeButton.setOnClickListener {
            showToast("home")
        }

        settingsButton.setOnClickListener {
            showToast("Settings")
        }

    }

    private fun showToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}