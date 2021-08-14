package ge.asurguladze.finalproject

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import ge.asurguladze.finalproject.database.profilePage.IProfilePageView
import ge.asurguladze.finalproject.database.profilePage.ProfilePagePresenter
import ge.asurguladze.finalproject.models.User

class ProfilePage : AppCompatActivity(), IProfilePageView {

    private lateinit var presenter: ProfilePagePresenter

    private lateinit var auth: FirebaseAuth

    private lateinit var plusButton: FloatingActionButton
    private lateinit var homeButton: ImageButton
    private lateinit var settingsButton: ImageButton
    private lateinit var update: Button
    private lateinit var signOut: Button

    private lateinit var profileImage: CircleImageView
    private lateinit var profileNickname: EditText
    private lateinit var profileProfession: EditText

    private lateinit var nickname: String

    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        initializeViews()
        setListeners()
    }

    override fun onStart() {
        super.onStart()

        dialog.show()
        presenter.getUserInfo(nickname)
    }

    private fun initializeViews() {

        auth = Firebase.auth
        val currentUser = auth.currentUser?.email.toString()
        nickname = currentUser.subSequence(0, currentUser.length-10).toString()

        presenter = ProfilePagePresenter(this)
        profileImage = findViewById(R.id.profile_picture)
        plusButton = findViewById(R.id.plus)
        homeButton = findViewById(R.id.home)
        settingsButton = findViewById(R.id.settings)
        update = findViewById(R.id.update)
        signOut = findViewById(R.id.sign_out)
        profileNickname = findViewById(R.id.profile_nickname)
        profileProfession = findViewById(R.id.profile_profession)

        dialog = AlertDialog.Builder(this)
            .setView(R.layout.loading_dialog_layout)
            .setCancelable(false)
            .create()
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {

            val data: Intent? = result.data
            val selectedImage: Uri = data?.data!!

            profileImage.setImageURI(selectedImage)
            presenter.uploadUserImage(nickname, selectedImage)
        }
    }

    private fun setListeners() {

        profileImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            resultLauncher.launch(intent)
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

        if (user.image != null) {
            profileImage.setImageBitmap(user.image)
        }

        dialog.dismiss()
    }

    override fun showError(exception: Exception) {
        dialog.dismiss()
        Toast.makeText(this, "Error getting data" + exception.message, Toast.LENGTH_LONG).show()
    }

}