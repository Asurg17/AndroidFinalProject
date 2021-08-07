package ge.asurguladze.finalproject.database

import ge.asurguladze.finalproject.models.User

interface ISearchPageView {

    fun onAllUsersFetch(users: ArrayList<User>)

    fun onAllSpecificUsersFetch(users: ArrayList<User>)

}