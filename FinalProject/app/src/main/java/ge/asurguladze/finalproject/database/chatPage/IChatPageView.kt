package ge.asurguladze.finalproject.database.chatPage

import ge.asurguladze.finalproject.models.Message
import ge.asurguladze.finalproject.models.User

interface IChatPageView{

    fun showAllData(messages: ArrayList<Message>, user: User)

    fun showError(exception: Exception)

}