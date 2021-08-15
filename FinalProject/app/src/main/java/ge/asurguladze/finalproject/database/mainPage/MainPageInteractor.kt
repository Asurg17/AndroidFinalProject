package ge.asurguladze.finalproject.database.mainPage

import android.graphics.BitmapFactory
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import ge.asurguladze.finalproject.models.FullData

class MainPageInteractor(private val presenter: IMainPagePresenter) {

    private lateinit var database : FirebaseDatabase
    private lateinit var allItems: ArrayList<FullData>
    private var itemsCount = 0
    private var filterText: String? = null

    fun getAllItems(nickname: String, userName: String?){

        filterText = userName

        allItems = arrayListOf()
        itemsCount = 0

        database = Firebase.database
        val messages =  database.getReference(MESSAGE_TAG)

        messages.child(nickname).get().addOnSuccessListener{

            if(it.value == null){
                presenter.onAllItemsFetch(allItems)
            }else{

                val allItems = it.value as HashMap<*, *>

                itemsCount = allItems.size

                allItems.forEach { subIt ->

                    val toUser = subIt.key.toString().split(':')[1]

                    val curData = FullData(toUser, null, null, null)
                    getLastMessage(subIt.value as HashMap<*, *>, curData, toUser)
                }

            }

        }.addOnFailureListener{
            presenter.onErrorDuringGettingData(it)
        }
    }

    private fun getLastMessage(allMessages: HashMap<*, *>, curData: FullData, toUser: String){

        var lastMessage = ""
        var lastMessageSentTime = Long.MIN_VALUE

        for (curMessage in allMessages) {

            val curMessageData = curMessage.value as HashMap<*, *>

            val curTime = curMessageData[TIME].toString().toLong()

            if(curTime > lastMessageSentTime){
                lastMessageSentTime = curTime
                lastMessage = curMessageData[MESSAGE].toString()
            }

        }

        curData.message = lastMessage
        curData.time = lastMessageSentTime

        getUserImage(toUser, curData)
    }

    private fun getUserImage(nickname: String, curData: FullData) {

        database.getReference(USER_TAG).child(nickname).child(IMAGE_FLAG).get()
            .addOnSuccessListener {

                if(it.value.toString().toBoolean()){

                    val storageReference = Firebase.storage.reference
                    val imageRef = storageReference.child(nickname)

                    imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener { subIt ->

                        curData.image = BitmapFactory.decodeByteArray(subIt, 0, subIt.size)

                        allItems.add(curData)
                        itemsCount -= 1

                        if(itemsCount == 0){

                            if(filterText == null) {
                                presenter.onAllItemsFetch(allItems)
                            }else{
                                getSpecificItems(filterText!!, allItems)
                            }

                        }

                    }.addOnFailureListener { subIt ->
                        presenter.onErrorDuringGettingData(subIt)
                    }

                }else{

                    allItems.add(curData)
                    itemsCount -= 1

                    if(itemsCount == 0){

                        if(filterText == null) {
                            presenter.onAllItemsFetch(allItems)
                        }else{
                            getSpecificItems(filterText!!, allItems)
                        }
                    }
                }

            }.addOnFailureListener {
                presenter.onErrorDuringGettingData(it)
            }

    }

    private fun getSpecificItems(userName: String, listItems: ArrayList<FullData>) {

        val arr = arrayListOf<FullData>()

        for(item in listItems){
            if(item.nickname.toString().contains(userName)){
                arr.add(item)
            }
        }

        presenter.onAllItemsFetch(arr)
    }

    companion object{
        const val USER_TAG = "users"
        const val MESSAGE_TAG = "messages"
        const val MESSAGE = "message"
        const val TIME = "time"
        const val IMAGE_FLAG = "imageFlag"
    }

}