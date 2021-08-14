package ge.asurguladze.finalproject.database.chatPage

import ge.asurguladze.finalproject.models.Message
import ge.asurguladze.finalproject.models.User

interface IChatPagePresenter {

    fun onAllDataFetch(messages: ArrayList<Message>, user: User)

    fun onErrorDuringGettingData(exception: Exception)

}