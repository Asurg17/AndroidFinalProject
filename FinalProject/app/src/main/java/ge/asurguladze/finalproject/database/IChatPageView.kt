package ge.asurguladze.finalproject.database

import ge.asurguladze.finalproject.models.Message
import ge.asurguladze.finalproject.models.User

interface IChatPageView{

    fun addMessagesToList(allMessages: ArrayList<Message>)

    fun showUserInfo(user: User)

}