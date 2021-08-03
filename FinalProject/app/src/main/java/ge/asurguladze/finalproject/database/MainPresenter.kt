package ge.asurguladze.finalproject.database

import ge.asurguladze.finalproject.models.User

class MainPresenter(private val view: IMainView) : IMainPresenter{

    private val interactor = MainInteractor(this)

    fun getUserInfo(nickname: String){
        interactor.getUserInfo(nickname)
    }

    override fun onUserInfoFetch(user: User) {
        view.showUserInfo(user)
    }
}