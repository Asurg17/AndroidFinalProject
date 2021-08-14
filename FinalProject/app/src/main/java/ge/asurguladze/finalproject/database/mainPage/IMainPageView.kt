package ge.asurguladze.finalproject.database.mainPage

import ge.asurguladze.finalproject.models.FullData

interface IMainPageView {

    fun renderAllFetchedItems(items: ArrayList<FullData>)

    fun showError(exception: Exception)

}