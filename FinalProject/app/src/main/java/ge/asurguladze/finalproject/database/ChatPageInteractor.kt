package ge.asurguladze.finalproject.database

import android.graphics.BitmapFactory
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import ge.asurguladze.finalproject.models.Message
import ge.asurguladze.finalproject.models.User

class ChatPageInteractor(private val presenter: IChatPagePresenter) {

    private lateinit var database : FirebaseDatabase

    fun addNewMessage(newMessage: Message, fromUserNickname: String, toUserNickname: String){

        database = Firebase.database
        val messages =  database.getReference(MESSAGE_TAG)

        messages.push().key?.let{
            messages.child(fromUserNickname).child("$fromUserNickname:$toUserNickname").child(it).setValue(newMessage)
        }

        messages.push().key?.let{
            messages.child(toUserNickname).child("$toUserNickname:$fromUserNickname").child(it).setValue(newMessage)
        }

    }

    fun getAllMessages(fromUserNickname: String, toUserNickname: String){
        database = Firebase.database
        val messages =  database.getReference(MESSAGE_TAG)

        messages.child(fromUserNickname).child("$fromUserNickname:$toUserNickname").get().addOnSuccessListener{

            val result = arrayListOf<Message>()

            if(it.value != null) {

                val allMessages = it.value as HashMap<*, *>

                for (curMessage in allMessages) {

                    val curMessageData = curMessage.value as HashMap<*, *>

                    val message = Message(
                        curMessageData[SENDER].toString(),
                        curMessageData[MESSAGE].toString(),
                        curMessageData[TIME].toString().toLong()
                    )

                    result.add(message)
                }
            }

            sortList(result)

        }.addOnFailureListener{
            Log.e(ERROR_TAG, "Error getting data", it)
        }

    }

    fun getUserData(nickname: String){
        database = Firebase.database
        val bd = database.reference
        val user = User()

        bd.child(USER_TAG).child(nickname).get().addOnSuccessListener {

            val userData = it.value as HashMap<*, *>

            user.nickname = userData[NICKNAME].toString()
            user.password = userData[PASSWORD].toString()
            user.profession = userData[PROFESSION].toString()

            getUserImage(nickname, user)

        }.addOnFailureListener{
            Log.e(ERROR_TAG, "Error getting data", it)
        }
    }

    private fun getUserImage(nickname: String, user: User) {

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

    private fun sortList(list: ArrayList<Message>) {
        presenter.onAllMessagesFetch(ArrayList(list.sortedWith(compareBy { it.time })))
    }

    companion object{
        const val USER_TAG = "users"
        const val MESSAGE_TAG = "messages"
        const val ERROR_TAG = "error"
        const val MESSAGE = "message"
        const val SENDER = "sender"
        const val TIME = "time"
        const val PROFESSION = "profession"
        const val PASSWORD = "password"
        const val NICKNAME = "nickname"
    }

}