package ge.asurguladze.finalproject.database.profilePage

import ge.asurguladze.finalproject.models.User

interface IProfilePageView {

    fun showUserInfo(user: User)

    fun showError(exception: Exception)

}