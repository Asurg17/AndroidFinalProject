package ge.asurguladze.finalproject.database.chatPage

import ge.asurguladze.finalproject.models.Message
import ge.asurguladze.finalproject.models.User

class ChatPagePresenter(private val view: IChatPageView): IChatPagePresenter {

    private val interactor = ChatPageInteractor(this)

    fun addNewMessage(newMessage: Message, fromUserNickname: String, toUserNickname: String){
        interactor.addNewMessage(newMessage, fromUserNickname, toUserNickname)
    }

    fun getAllData(fromUserNickname: String, toUserNickname: String){
        interactor.getAllData(fromUserNickname, toUserNickname)
    }

    override fun onAllDataFetch(messages: ArrayList<Message>, user: User) {
        view.showAllData(messages, user)
    }

    override fun onErrorDuringGettingData(exception: Exception) {
        view.showError(exception)
    }

}