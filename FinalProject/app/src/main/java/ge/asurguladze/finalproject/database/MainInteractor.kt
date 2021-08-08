package ge.asurguladze.finalproject.database

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import ge.asurguladze.finalproject.models.User


class MainInteractor(private val presenter: IMainPresenter){

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

            getUserImage(nickname, user)

        }.addOnFailureListener{
            Log.e(TAG, "Error getting data", it)
        }

    }

    private fun getUserImage(nickname: String, user: User){

        val storageReference = Firebase.storage.reference
        val imageRef = storageReference.child(nickname)

        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener {

            // Use the bytes to display the image
            user.image = BitmapFactory.decodeByteArray(it, 0, it.size)
            presenter.onUserInfoFetch(user)

        }.addOnFailureListener {

            // Handle any errors
            user.image = null
            presenter.onUserInfoFetch(user)

        }

    }


    fun changeUserInfo(nickname: String, newNickname:String, profession: String){
        database = Firebase.database
        val bd = database.reference

//        bd.child(PATH).child(nickname).child(PROFESSION).setValue(profession)
        bd.child(PATH).child(nickname).child(PROFESSION).setValue(profession)

    }

    fun uploadUserImage(nickname: String, selectedImage: Uri){
        val storage = Firebase.storage
        val storageRef = storage.reference
        val riversRef = storageRef.child(nickname)
        val uploadTask = riversRef.putFile(selectedImage)

        uploadTask.addOnFailureListener {

            Log.d(TAG, "Nah")

        }.addOnSuccessListener {

            Log.d(TAG, "Yeah")

        }
    }



    companion object{
        const val TAG = "error"
        const val PATH = "users"
        const val PROFESSION = "profession"
        const val PASSWORD = "password"
        const val NICKNAME = "nickname"
    }

}