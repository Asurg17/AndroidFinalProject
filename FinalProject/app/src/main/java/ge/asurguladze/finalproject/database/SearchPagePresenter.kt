package ge.asurguladze.finalproject.database

import ge.asurguladze.finalproject.models.User

class SearchPagePresenter(private val view: ISearchPageView): ISearchPagePresenter {

    private val interactor = SearchPageInteractor(this)

    fun getAllUserInfo(){
        interactor.getAllUserInfo()
    }

    fun getSpecificUsers(userName: String){
        interactor.getSpecificUsers(userName)
    }


    override fun onAllUserInfoFetch(users: ArrayList<User>) {
        view.onAllUsersFetch(users)
    }

    override fun onAllSpecificUserInfoFetch(users: ArrayList<User>) {
        view.onAllSpecificUsersFetch(users)
    }
}