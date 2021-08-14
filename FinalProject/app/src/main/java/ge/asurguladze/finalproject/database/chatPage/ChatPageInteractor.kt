package ge.asurguladze.finalproject.database.chatPage

import android.graphics.BitmapFactory
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

    fun getAllData(fromUserNickname: String, toUserNickname: String){
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

            getUserData(toUserNickname, ArrayList(result.sortedWith(compareBy { subIt -> subIt.time })))

        }.addOnFailureListener{
            presenter.onErrorDuringGettingData(it)
        }

    }

    private fun getUserData(nickname: String, messages: ArrayList<Message>){
        database = Firebase.database
        val bd = database.reference
        val user = User()

        bd.child(USER_TAG).child(nickname).get().addOnSuccessListener {

            val userData = it.value as HashMap<*, *>

            user.nickname = userData[NICKNAME].toString()
            user.password = userData[PASSWORD].toString()
            user.profession = userData[PROFESSION].toString()


            if(userData[IMAGE_FLAG].toString().toBoolean()) {
                getUserImage(nickname, user, messages)
            }else{
                user.image = null
                presenter.onAllDataFetch(messages, user)
            }

        }.addOnFailureListener{
            presenter.onErrorDuringGettingData(it)
        }
    }

    private fun getUserImage(nickname: String, user: User, messages: ArrayList<Message>) {

        val storageReference = Firebase.storage.reference
        val imageRef = storageReference.child(nickname)

        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener {

            user.image = BitmapFactory.decodeByteArray(it, 0, it.size)
            presenter.onAllDataFetch(messages, user)

        }.addOnFailureListener {
            presenter.onErrorDuringGettingData(it)
        }

    }

    companion object{
        const val USER_TAG = "users"
        const val MESSAGE_TAG = "messages"
        const val MESSAGE = "message"
        const val SENDER = "sender"
        const val TIME = "time"
        const val IMAGE_FLAG = "imageFlag"
        const val PROFESSION = "profession"
        const val PASSWORD = "password"
        const val NICKNAME = "nickname"
    }

}