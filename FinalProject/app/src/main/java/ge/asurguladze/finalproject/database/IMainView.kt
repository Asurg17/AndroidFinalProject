package ge.asurguladze.finalproject.database

import ge.asurguladze.finalproject.models.User

interface IMainView {

    fun showUserInfo(user: User)

    fun renderUserInfo(profession: String)

}