package ge.asurguladze.finalproject

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import ge.asurguladze.finalproject.adapters.UserClickListener
import ge.asurguladze.finalproject.adapters.UsersListAdapter
import ge.asurguladze.finalproject.database.ISearchPageView
import ge.asurguladze.finalproject.database.SearchPagePresenter
import ge.asurguladze.finalproject.models.User
import java.util.*


class SearchPage : AppCompatActivity(), ISearchPageView, UserClickListener {

    private lateinit var usersRv : RecyclerView

    private lateinit var presenter: SearchPagePresenter

    private lateinit var backButton: ImageButton
    private lateinit var searchText: TextInputEditText

    private lateinit var allUsers: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_page)

        initializeViews()
        setListeners()
        renderUsers("")
    }

    private fun initializeViews() {
        presenter = SearchPagePresenter(this)
        backButton = findViewById(R.id.back)
        searchText = findViewById(R.id.search_users)

        usersRv = findViewById(R.id.users)
        allUsers = arrayListOf()

        val adapter = UsersListAdapter(allUsers)
        adapter.userClickListener = this
        usersRv.adapter = adapter
    }

    private fun setListeners() {
        backButton.setOnClickListener {
            goToMainPage()
        }

        var timer: Timer? = Timer()

        searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                timer?.cancel()
            }

            override fun afterTextChanged(s: Editable) {
                timer = Timer()
                timer!!.schedule(object : TimerTask() {
                    override fun run() {
                        runOnUiThread {
                            renderUsers(searchText.text.toString())
                        }
                    }
                }, 500)
            }
        })

    }

    private fun goToMainPage(){
        val intent = Intent(this, MainPage::class.java)
        startActivity(intent)
    }

    private fun renderUsers(userName: String){

        if(userName.length >= 3){
            presenter.getSpecificUsers(userName)
        }else{
            presenter.getAllUserInfo()
        }
    }

    // ISearchPageView functions
    override fun onAllUsersFetch(users: MutableMap<String, User>) {
        changeDataSet(users)
    }

    override fun onAllSpecificUsersFetch(users: MutableMap<String, User>) {
        changeDataSet(users)
    }


    private fun changeDataSet(users: MutableMap<String, User>){

        var checker = false

        allUsers.removeAll(allUsers)

        for(curUser in users){
            checker = true
            allUsers.add(curUser.value)
        }

        if(!checker){
            showNoUsersWereFound()
        }

        usersRv.adapter?.notifyDataSetChanged()
    }

    private fun showNoUsersWereFound(){
        Toast.makeText(this, "Sorry, no users were found!", Toast.LENGTH_SHORT).show()
    }

    override fun onUserClicked(user: User) {
        val intent = Intent(this, ChatPage::class.java)
        intent.putExtra(TAG, user.nickname)
        startActivity(intent)
    }

    companion object{
        const val TAG="to user"
    }

}