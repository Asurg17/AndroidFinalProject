package ge.asurguladze.finalproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import ge.asurguladze.finalproject.database.IMainView
import ge.asurguladze.finalproject.database.MainPresenter
import ge.asurguladze.finalproject.models.User

class ProfilePage : AppCompatActivity(), IMainView {

    private lateinit var presenter: MainPresenter

    private lateinit var auth: FirebaseAuth

    private lateinit var profileImage: CircleImageView
    private lateinit var plusButton: FloatingActionButton
    private lateinit var homeButton: ImageButton
    private lateinit var settingsButton: ImageButton
    private lateinit var update: Button
    private lateinit var signOut: Button

    private lateinit var profileNickname: EditText
    private lateinit var profileProfession: EditText

    private lateinit var nickname: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        initializeViews()
        setListeners()
        getValues()
    }

    private fun getValues() {

        presenter.getUserInfo(nickname)

        Log.d("user_our_user", nickname)
    }

    private fun initializeViews() {

        auth = Firebase.auth
        val currentUser = auth.currentUser?.email.toString()
        nickname = currentUser.subSequence(0, currentUser.length-10).toString()

        presenter = MainPresenter(this)
        profileImage = findViewById(R.id.profile_picture)
        plusButton = findViewById(R.id.plus)
        homeButton = findViewById(R.id.home)
        settingsButton = findViewById(R.id.settings)
        update = findViewById(R.id.update)
        signOut = findViewById(R.id.sign_out)
        profileNickname = findViewById(R.id.profile_nickname)
        profileProfession = findViewById(R.id.profile_profession)
    }

    private fun setListeners() {
        profileImage.setOnClickListener {
            Log.d("user_our_user", "arika")
        }

        plusButton.setOnClickListener{
            goToSearchPage()
        }

        homeButton.setOnClickListener {
            goToMainPage()
        }

        update.setOnClickListener {
            updateUserInfo()
        }

        signOut.setOnClickListener {
            signOut()
        }

    }

    private fun updateUserInfo(){
        if(profileProfession.length() != 0){
            presenter.changeUserInfo(nickname, nickname, profileProfession.text.toString())
        }
    }

    private fun signOut(){
        Firebase.auth.signOut()
        goToStartPage()
    }

    private fun goToStartPage(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun goToMainPage(){
        val intent = Intent(this, MainPage::class.java)
        startActivity(intent)
    }

    private fun goToSearchPage(){
        val intent = Intent(this, SearchPage::class.java)
        startActivity(intent)
    }


    // IMAinView functions
    override fun showUserInfo(user: User) {
        profileNickname.setText(user.nickname)
        profileProfession.setText(user.profession)
    }

    override fun renderUserInfo(profession: String) {
        profileProfession.setText(profession)
    }
}