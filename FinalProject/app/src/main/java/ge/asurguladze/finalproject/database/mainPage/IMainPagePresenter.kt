package ge.asurguladze.finalproject.database.mainPage

import ge.asurguladze.finalproject.models.FullData

interface IMainPagePresenter {

    fun onAllItemsFetch(items: ArrayList<FullData>)

    fun onErrorDuringGettingData(exception: Exception)

}