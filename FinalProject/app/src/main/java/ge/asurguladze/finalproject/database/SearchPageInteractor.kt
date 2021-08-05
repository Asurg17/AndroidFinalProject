package ge.asurguladze.finalproject.database

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.asurguladze.finalproject.models.User

class SearchPageInteractor(private val presenter: ISearchPagePresenter) {

    private lateinit var database : FirebaseDatabase

    fun getAllUserInfo(){

        val auth = Firebase.auth
        val currentUser = auth.currentUser?.email.toString()
        val nickname = currentUser.subSequence(0, currentUser.length-10).toString()

        database = Firebase.database
        val bd = database.reference

        bd.child(PATH).get().addOnSuccessListener {

            val usersData = it.value as HashMap<*, *>
            val users = arrayListOf<User>()

            for ((key, value) in usersData) {

                if(key != nickname) {

                    val userData = value as HashMap<*, *>
                    val user = User(
                        userData[NICKNAME].toString(),
                        userData[PASSWORD].toString(),
                        userData[PROFESSION].toString()
                    )

                    users.add(user)
                }
            }

            presenter.onAllUserInfoFetch(users)

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