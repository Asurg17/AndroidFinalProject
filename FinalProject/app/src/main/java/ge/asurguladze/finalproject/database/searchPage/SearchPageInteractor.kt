package ge.asurguladze.finalproject.database.searchPage

import android.graphics.BitmapFactory
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.storage
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
            val users = mutableMapOf<String, User>()

            for ((key, value) in usersData) {

                if(key != nickname) {

                    val userData = value as HashMap<*, *>
                    val user = User(
                        userData[NICKNAME].toString(),
                        userData[PASSWORD].toString(),
                        userData[PROFESSION].toString()
                    )

                    users[user.nickname.toString()]=user
                }
            }

            getImages(users, true)

        }.addOnFailureListener{
            presenter.onErrorDuringGettingData(it)
        }

    }

    fun getSpecificUsers(userName: String){

        val auth = Firebase.auth
        val currentUser = auth.currentUser?.email.toString()
        val nickname = currentUser.subSequence(0, currentUser.length-10).toString()

        database = Firebase.database
        val bd = database.reference

        bd.child(PATH).get().addOnSuccessListener {

            val usersData = it.value as HashMap<*, *>
            val users = mutableMapOf<String, User>()

            for ((key, value) in usersData) {

                if(key != nickname && key.toString().contains(userName)) {

                    val userData = value as HashMap<*, *>
                    val user = User(
                        userData[NICKNAME].toString(),
                        userData[PASSWORD].toString(),
                        userData[PROFESSION].toString()
                    )

                    users[user.nickname.toString()]=user
                }
            }

            getImages(users, false)

        }.addOnFailureListener{
            presenter.onErrorDuringGettingData(it)
        }

    }


    private fun getImages(users: MutableMap<String, User>, allUsersFlag: Boolean){

        val storageReference = Firebase.storage.reference

        storageReference
            .listAll()
            .addOnSuccessListener { (items) ->

                var size = 0

                items.forEach { item ->
                    item.getBytes(Long.MAX_VALUE).addOnSuccessListener {
                        val bitmap =  BitmapFactory.decodeByteArray(it, 0, it.size)
                        users[item.name]?.image = bitmap

                        size+=1
                        if(size == items.size){

                            if(allUsersFlag){
                                presenter.onAllUserInfoFetch(users)
                            }else{
                                presenter.onAllSpecificUserInfoFetch(users)
                            }
                        }

                    }
                }
            }
            .addOnFailureListener {
                presenter.onErrorDuringGettingData(it)
            }

    }

    companion object{
        const val PATH = "users"
        const val PROFESSION = "profession"
        const val PASSWORD = "password"
        const val NICKNAME = "nickname"
    }

}