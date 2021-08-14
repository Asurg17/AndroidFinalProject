package ge.asurguladze.finalproject.database.mainPage

import ge.asurguladze.finalproject.models.FullData

class MainPagePresenter(private val view: IMainPageView): IMainPagePresenter {

    private val interactor = MainPageInteractor(this)

    fun getAllItems(nickname: String, userName: String?){
        interactor.getAllItems(nickname, userName)
    }

    fun getSpecificItems(userName: String, listItems: ArrayList<FullData>) {
        interactor.getSpecificItems(userName, listItems)
    }

    override fun onAllItemsFetch(items: ArrayList<FullData>) {
        view.renderAllFetchedItems(items)
    }

    override fun onErrorDuringGettingData(exception: Exception) {
        view.showError(exception)
    }

}