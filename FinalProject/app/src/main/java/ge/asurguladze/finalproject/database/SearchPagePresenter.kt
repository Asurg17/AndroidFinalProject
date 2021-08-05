package ge.asurguladze.finalproject.database

import ge.asurguladze.finalproject.models.User

class SearchPagePresenter(private val view: ISearchPageView): ISearchPagePresenter {

    private val interactor = SearchPageInteractor(this)

    fun getAllUserInfo(){
        interactor.getAllUserInfo()
    }


    override fun onAllUserInfoFetch(users: ArrayList<User>) {
        view.onAllUsersFetch(users)
    }
}