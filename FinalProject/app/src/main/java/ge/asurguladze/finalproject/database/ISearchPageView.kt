package ge.asurguladze.finalproject.database

import ge.asurguladze.finalproject.models.User

interface ISearchPageView {

    fun onAllUsersFetch(users: MutableMap<String, User>)

    fun onAllSpecificUsersFetch(users: MutableMap<String, User>)

}