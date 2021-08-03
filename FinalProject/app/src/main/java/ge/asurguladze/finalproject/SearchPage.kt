package ge.asurguladze.finalproject

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText


class SearchPage : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var textInput: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_page)

        initializeViews()
        setListeners()
    }

    private fun initializeViews() {
        backButton = findViewById(R.id.back)
        textInput = findViewById(R.id.search_users)
    }

    private fun setListeners() {
        backButton.setOnClickListener {
            goToMainPage()
        }

        textInput.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                if(start-before+1 >= 3){
                    Log.d("text_change", (start-before+1).toString())
                }

            }
        })
    }

    private fun goToMainPage(){
        val intent = Intent(this, MainPage::class.java)
        startActivity(intent)
    }
}