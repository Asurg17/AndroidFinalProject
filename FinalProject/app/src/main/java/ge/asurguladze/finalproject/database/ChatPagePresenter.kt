package ge.asurguladze.finalproject.database

import ge.asurguladze.finalproject.models.Message
import ge.asurguladze.finalproject.models.User

class ChatPagePresenter(private val view: IChatPageView): IChatPagePresenter {

    private val interactor = ChatPageInteractor(this)

    fun addNewMessage(newMessage: Message, fromUserNickname: String, toUserNickname: String){
        interactor.addNewMessage(newMessage, fromUserNickname, toUserNickname)
    }

    fun getAllMessages(fromUserNickname: String, toUserNickname: String){
        interactor.getAllMessages(fromUserNickname, toUserNickname)
    }

    fun getUserData(nickname: String){
        interactor.getUserData(nickname)
    }

    override fun onAllMessagesFetch(messages: ArrayList<Message>) {
        view.addMessagesToList(messages)
    }

    override fun onUserInfoFetch(user: User) {
        view.showUserInfo(user)
    }

}