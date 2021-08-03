package ge.asurguladze.finalproject.database

import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.asurguladze.finalproject.models.User


class MainInteractor(private val presenter: IMainPresenter){

    private lateinit var database : FirebaseDatabase

    fun getUserInfo(nickname: String) {

        database = Firebase.database
        val bd = database.reference

        bd.child(PATH).child("al").get().addOnSuccessListener {

            val userData = it.value as HashMap<*, *>
            val user = User(userData[NICKNAME].toString(), userData[PASSWORD].toString(), userData[PROFESSION].toString())

            presenter.onUserInfoFetch(user)

        }.addOnFailureListener{
            Log.e(TAG, "Error getting data", it)
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