package ge.asurguladze.finalproject.database

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.asurguladze.finalproject.models.User


class MainInteractor(private val presenter: IMainPresenter){

    private lateinit var database : FirebaseDatabase

    fun getUserInfo(nickname: String) {

        database = Firebase.database
        val bd = database.reference

        bd.child(PATH).child(nickname).get().addOnSuccessListener {

            val userData = it.value as HashMap<*, *>
            val user = User(userData[NICKNAME].toString(), userData[PASSWORD].toString(), userData[PROFESSION].toString())

            presenter.onUserInfoFetch(user)

        }.addOnFailureListener{
            Log.e(TAG, "Error getting data", it)
        }

    }

    fun changeUserInfo(nickname: String, newNickname:String, profession: String){
        database = Firebase.database
        val bd = database.reference

//        bd.child(PATH).child(nickname).child(PROFESSION).setValue(profession)
        bd.child(PATH).child(nickname).child(PROFESSION).setValue(profession)

        presenter.onUserInfoChange(profession)
    }

    fun getAllUserInfo(){

    }

    companion object{
        const val TAG = "error"
        const val PATH = "users"
        const val PROFESSION = "profession"
        const val PASSWORD = "password"
        const val NICKNAME = "nickname"
    }

}