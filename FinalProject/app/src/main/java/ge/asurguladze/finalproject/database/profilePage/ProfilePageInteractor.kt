package ge.asurguladze.finalproject.database.profilePage

import android.graphics.BitmapFactory
import android.net.Uri
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import ge.asurguladze.finalproject.models.User


class ProfilePageInteractor(private val presenter: IProfilePagePresenter){

    private lateinit var database : FirebaseDatabase

    fun getUserInfo(nickname: String) {

        database = Firebase.database
        val bd = database.reference
        val user = User()

        bd.child(PATH).child(nickname).get().addOnSuccessListener {

            val userData = it.value as HashMap<*, *>

            user.nickname = userData[NICKNAME].toString()
            user.password = userData[PASSWORD].toString()
            user.profession = userData[PROFESSION].toString()

            if(userData[IMAGE_FLAG].toString().toBoolean()){
                getUserImage(nickname, user)
            }else{
                user.image = null
                presenter.onUserInfoFetch(user)
            }

        }.addOnFailureListener{
            presenter.onErrorDuringGettingData(it)
        }

    }

    private fun getUserImage(nickname: String, user: User){

        val storageReference = Firebase.storage.reference
        val imageRef = storageReference.child(nickname)

        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener {

            user.image = BitmapFactory.decodeByteArray(it, 0, it.size)
            presenter.onUserInfoFetch(user)

        }.addOnFailureListener {
            presenter.onErrorDuringGettingData(it)
        }

    }


    fun changeUserInfo(nickname: String, newNickname:String, profession: String){
        database = Firebase.database
        val db = database.reference

//        bd.child(PATH).child(nickname).child(PROFESSION).setValue(profession)
        db.child(PATH).child(nickname).child(PROFESSION).setValue(profession)

    }

    fun uploadUserImage(nickname: String, selectedImage: Uri){
        val storage = Firebase.storage
        val storageRef = storage.reference
        val riversRef = storageRef.child(nickname)
        val uploadTask = riversRef.putFile(selectedImage)

        database = Firebase.database
        val db = database.reference

        uploadTask.addOnFailureListener {

            presenter.onErrorDuringGettingData(it)

        }.addOnSuccessListener {

            db.child(PATH).child(nickname).child(IMAGE_FLAG).setValue(true)

        }
    }


    companion object{
        const val PATH = "users"
        const val IMAGE_FLAG = "imageFlag"
        const val PROFESSION = "profession"
        const val PASSWORD = "password"
        const val NICKNAME = "nickname"
    }

}