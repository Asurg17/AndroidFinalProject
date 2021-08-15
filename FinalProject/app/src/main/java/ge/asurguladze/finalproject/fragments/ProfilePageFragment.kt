package ge.asurguladze.finalproject.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import ge.asurguladze.finalproject.AuthorizationPage
import ge.asurguladze.finalproject.MainActivity
import ge.asurguladze.finalproject.R
import ge.asurguladze.finalproject.database.profilePage.IProfilePageView
import ge.asurguladze.finalproject.database.profilePage.ProfilePagePresenter
import ge.asurguladze.finalproject.models.User

class ProfilePageFragment : Fragment(), IProfilePageView {

    private lateinit var presenter: ProfilePagePresenter

    private lateinit var auth: FirebaseAuth

    private lateinit var update: Button
    private lateinit var signOut: Button

    private lateinit var profileImage: CircleImageView
    private lateinit var profileNickname: EditText
    private lateinit var profileProfession: EditText

    private lateinit var nickname: String

    private lateinit var dialog: AlertDialog

    private lateinit var curView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        curView = inflater.inflate(R.layout.fragment_profile_page, container, false)
        return curView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        setListeners()
    }

    override fun onStart() {
        super.onStart()

        dialog.show()
        presenter.getUserInfo(nickname)
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).showBottomAppBar()
    }

    private fun initializeViews() {

        auth = Firebase.auth
        val currentUser = auth.currentUser?.email.toString()
        nickname = currentUser.subSequence(0, currentUser.length-10).toString()

        presenter = ProfilePagePresenter(this)
        profileImage = curView.findViewById(R.id.profile_picture)
        update = curView.findViewById(R.id.update)
        signOut = curView.findViewById(R.id.sign_out)
        profileNickname = curView.findViewById(R.id.profile_nickname)
        profileProfession = curView.findViewById(R.id.profile_profession)

        dialog = AlertDialog.Builder(requireContext())
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
        val intent = Intent(requireContext(), AuthorizationPage::class.java)
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
        Toast.makeText(requireContext(), "Error getting data" + exception.message, Toast.LENGTH_LONG).show()
    }

}