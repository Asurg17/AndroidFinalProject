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
import ge.asurguladze.finalproject.adapters.UsersListAdapter
import ge.asurguladze.finalproject.database.ISearchPageView
import ge.asurguladze.finalproject.database.SearchPagePresenter
import ge.asurguladze.finalproject.models.User


class SearchPage : AppCompatActivity(), ISearchPageView {

    private lateinit var usersRv : RecyclerView
    private lateinit var adapter : UsersListAdapter

    private lateinit var presenter: SearchPagePresenter

    private lateinit var backButton: ImageButton
    private lateinit var searchText: TextInputEditText

    private lateinit var allUsers: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_page)

        initializeViews()
        setListeners()
        getAllUsers()
    }

    private fun initializeViews() {
        presenter = SearchPagePresenter(this)
        backButton = findViewById(R.id.back)
        searchText = findViewById(R.id.search_users)

        usersRv = findViewById(R.id.users)
    }

    private fun setListeners() {
        backButton.setOnClickListener {
            goToMainPage()
        }

        searchText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                if(start-before+1 >= 3){
                    renderUsers(searchText.text.toString())
                }else{
                    renderUsers("")
                }

            }
        })
    }

    private fun getAllUsers(){
        presenter.getAllUserInfo()
    }

    private fun goToMainPage(){
        val intent = Intent(this, MainPage::class.java)
        startActivity(intent)
    }

    private fun renderUsers(userName: String){

        adapter = if(userName.isNotEmpty()){

            val filteredUsers = arrayListOf<User>()

            for(curUser in allUsers){
                if(curUser.nickname.toString().contains(userName)){
                    filteredUsers.add(curUser)
                }
            }

            if(filteredUsers.size==0){
                showShortToast()
            }

            UsersListAdapter(filteredUsers)

        }else{

            if(allUsers.size==0){
                showShortToast()
            }

            UsersListAdapter(allUsers)

        }

        usersRv.adapter = adapter
    }

    private fun showShortToast(){
        Toast.makeText(this, "Sorry, no users were found!", Toast.LENGTH_SHORT).show()
    }

    // ISearchPageView functions
    override fun onAllUsersFetch(users: ArrayList<User>) {
        allUsers = users
        renderUsers("")
    }
}