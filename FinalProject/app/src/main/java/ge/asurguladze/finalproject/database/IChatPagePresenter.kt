package ge.asurguladze.finalproject.database

import ge.asurguladze.finalproject.models.Message
import ge.asurguladze.finalproject.models.User

interface IChatPagePresenter {

    fun onAllMessagesFetch(messages: ArrayList<Message>)

    fun onUserInfoFetch(user: User)

}