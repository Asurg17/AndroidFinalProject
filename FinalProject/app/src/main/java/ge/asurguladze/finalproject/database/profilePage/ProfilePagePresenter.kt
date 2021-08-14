package ge.asurguladze.finalproject.database.profilePage

import android.net.Uri
import ge.asurguladze.finalproject.models.User

class ProfilePagePresenter(private val view: IProfilePageView) : IProfilePagePresenter {

    private val interactor = ProfilePageInteractor(this)

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

    override fun onErrorDuringGettingData(exception: Exception) {
        view.showError(exception)
    }

}