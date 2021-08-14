package ge.asurguladze.finalproject.database.searchPage

import ge.asurguladze.finalproject.models.User

interface ISearchPagePresenter {

    fun onAllUserInfoFetch(users: MutableMap<String, User>)

    fun onAllSpecificUserInfoFetch(users: MutableMap<String, User>)

    fun onErrorDuringGettingData(exception: Exception)

}