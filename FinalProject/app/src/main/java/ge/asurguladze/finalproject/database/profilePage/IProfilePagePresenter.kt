package ge.asurguladze.finalproject.database.profilePage

import ge.asurguladze.finalproject.models.User

interface IProfilePagePresenter {

    fun onUserInfoFetch(user: User)

    fun onErrorDuringGettingData(exception: Exception)

}