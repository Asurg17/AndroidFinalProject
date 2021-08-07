package ge.asurguladze.finalproject.database

import ge.asurguladze.finalproject.models.User

interface ISearchPagePresenter {

    fun onAllUserInfoFetch(users: ArrayList<User>)

    fun onAllSpecificUserInfoFetch(users: ArrayList<User>)

}