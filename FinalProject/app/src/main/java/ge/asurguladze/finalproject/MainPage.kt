package ge.asurguladze.finalproject

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ge.asurguladze.finalproject.adapters.MainPageItemClickedListener
import ge.asurguladze.finalproject.adapters.MainPageListAdapter
import ge.asurguladze.finalproject.database.mainPage.IMainPageView
import ge.asurguladze.finalproject.database.mainPage.MainPagePresenter
import ge.asurguladze.finalproject.models.FullData
import java.util.*

class MainPage : AppCompatActivity(), MainPageItemClickedListener, IMainPageView {

    private lateinit var search: TextInputEditText
    private lateinit var plusButton: FloatingActionButton
    private lateinit var homeButton: ImageButton
    private lateinit var settingsButton: ImageButton
    private lateinit var chatFriendsListRv: RecyclerView

    private lateinit var listItems: ArrayList<FullData>

    private lateinit var presenter: MainPagePresenter

    private lateinit var auth: FirebaseAuth

    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        initializeViews()
        setListeners()
    }

    override fun onStart() {
        super.onStart()

        search.setText("")
    }

    private fun initializeViews() {
        search = findViewById(R.id.search)
        plusButton = findViewById(R.id.plus)
        homeButton = findViewById(R.id.home)
        settingsButton = findViewById(R.id.settings)
        chatFriendsListRv = findViewById(R.id.chat_friends_list)

        listItems = arrayListOf()
        val adapter = MainPageListAdapter(listItems)
        adapter.mainPageItemClickedListener = this
        chatFriendsListRv.adapter = adapter

        presenter = MainPagePresenter(this)

        dialog = AlertDialog.Builder(this)
            .setView(R.layout.loading_dialog_layout)
            .setCancelable(false)
            .create()
    }

    private fun setListeners() {

        plusButton.setOnClickListener{
            goToSearchPage()
        }

        settingsButton.setOnClickListener {
            goToProfilePage()
        }

        var timer: Timer? = Timer()

        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                timer?.cancel()
            }

            override fun afterTextChanged(s: Editable) {
                timer = Timer()
                timer!!.schedule(object : TimerTask() {
                    override fun run() {
                        runOnUiThread {
                            renderUsers(search.text.toString())
                        }
                    }
                }, 500)
            }
        })

    }

    private fun renderUsers(userName: String){

        dialog.show()

        if(userName.length >= 3){
            presenter.getAllItems(getNickname(), userName)
        }else{
            presenter.getAllItems(getNickname(), null)
        }
    }

    private fun goToProfilePage(){
        val intent = Intent(this, ProfilePage::class.java)
        startActivity(intent)
    }

    private fun goToSearchPage(){
        val intent = Intent(this, SearchPage::class.java)
        startActivity(intent)
    }


    private fun getNickname(): String{
        auth = Firebase.auth
        val currentUser = auth.currentUser?.email.toString()
        return currentUser.subSequence(0, currentUser.length-10).toString()
    }

    private fun showToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

//    Override functions

    override fun renderAllFetchedItems(items: ArrayList<FullData>) {

        listItems.removeAll(listItems)

        for(item in items){
            listItems.add(item)
        }

        chatFriendsListRv.adapter?.notifyDataSetChanged()

        if(listItems.size == 0){
            showToast(getString(R.string.main_page_no_item_fetched_text))
        }

        dialog.dismiss()
    }

    override fun showError(exception: Exception) {
        dialog.dismiss()
        Toast.makeText(this, "Error getting data" + exception.message, Toast.LENGTH_LONG).show()
    }

    override fun onItemClicked(item: FullData) {
        val intent = Intent(this, ChatPage::class.java)
        intent.putExtra(SearchPage.TAG, item.nickname)
        startActivity(intent)
    }
}