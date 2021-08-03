package ge.asurguladze.finalproject.database

import ge.asurguladze.finalproject.models.User

interface IMainPresenter {

    fun onUserInfoFetch(user: User)

}