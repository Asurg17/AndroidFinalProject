package ge.asurguladze.finalproject.database

import android.net.Uri
import ge.asurguladze.finalproject.models.User

class MainPresenter(private val view: IMainView) : IMainPresenter{

    private val interactor = MainInteractor(this)

    fun getUserInfo(nickname: String){
        interactor.getUserInfo(nickname)
    }

    fun changeUserInfo(nickname: String, newNickname:String, profession: String){
        interactor.changeUserInfo(nickname, newNickname, profession)
    }

    fun uploadUserImage(nickname: String, selectedImage: Uri){
        interactor.uploadUserImage(nickname, selectedImage)
    }

    override fun onUserInfoFetch(user: User) {
        view.showUserInfo(user)
    }

}