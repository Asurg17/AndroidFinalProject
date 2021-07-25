package ge.asurguladze.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class RegistrationPage : AppCompatActivity() {

    lateinit var signUp: Button

    lateinit var nickname: EditText
    lateinit var password: EditText
    lateinit var profession: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_page)

        initializeViews()
        setListeners()
    }

    private fun initializeViews() {
        signUp = findViewById(R.id.sign_up)

        nickname = findViewById(R.id.nickname)
        password = findViewById(R.id.password)
        profession = findViewById(R.id.profession)
    }

    private fun setListeners() {
        signUp.setOnClickListener {
            registerClient()
        }
    }

    private fun registerClient() {
        if (checkIfAllValuesAreFilled()) {
            if(checkIfNicknameAlreadyUsed()){
                showToast("Such nickname already exists")
            }else{
                var a = 1
            }
        }else{
            showToast("Please fill in all the fields")
        }
    }

    private fun checkIfNicknameAlreadyUsed(): Boolean {
        return true
    }

    private fun checkIfAllValuesAreFilled(): Boolean {

        if(nickname.text.isNotEmpty() && password.text.isNotEmpty() && profession.text.isNotEmpty()){
            return true
        }

        return false
    }

    private fun showToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

}