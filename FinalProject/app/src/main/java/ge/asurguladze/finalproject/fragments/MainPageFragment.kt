package ge.asurguladze.finalproject.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ge.asurguladze.finalproject.ChatPage
import ge.asurguladze.finalproject.MainActivity
import ge.asurguladze.finalproject.R
import ge.asurguladze.finalproject.SearchPage
import ge.asurguladze.finalproject.adapters.MainPageItemClickedListener
import ge.asurguladze.finalproject.adapters.MainPageListAdapter
import ge.asurguladze.finalproject.database.mainPage.IMainPageView
import ge.asurguladze.finalproject.database.mainPage.MainPagePresenter
import ge.asurguladze.finalproject.models.FullData
import java.util.*


class MainPageFragment : Fragment(), MainPageItemClickedListener, IMainPageView {

    private lateinit var curView: View

    private lateinit var search: TextInputEditText
    private lateinit var chatFriendsListRv: RecyclerView

    private lateinit var listItems: ArrayList<FullData>

    private lateinit var presenter: MainPagePresenter

    private lateinit var auth: FirebaseAuth

    private lateinit var dialog: AlertDialog

    private lateinit var  act: MainActivity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        act = activity as MainActivity

        curView = inflater.inflate(R.layout.fragment_main_page, container, false)
        return curView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews()
        setListeners()

    }

    override fun onStart() {
        super.onStart()

        search.setText("")
    }

    private fun initializeViews() {
        search = curView.findViewById(R.id.search)
        chatFriendsListRv = curView.findViewById(R.id.chat_friends_list)

        listItems = arrayListOf()
        val adapter = MainPageListAdapter(listItems)
        adapter.mainPageItemClickedListener = this
        chatFriendsListRv.adapter = adapter

        presenter = MainPagePresenter(this)

        dialog = AlertDialog.Builder(requireContext())
            .setView(R.layout.loading_dialog_layout)
            .setCancelable(false)
            .create()

    }

    private fun setListeners() {

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
                        activity?.runOnUiThread {
                            renderUsers(search.text.toString())
                        }
                    }
                }, 500)
            }
        })

        chatFriendsListRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    (activity as MainActivity).hideBottomAppBar()
                } else if (dy < 0) {
                    (activity as MainActivity).showBottomAppBar()
                }
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


    private fun getNickname(): String{
        auth = Firebase.auth
        val currentUser = auth.currentUser?.email.toString()
        return currentUser.subSequence(0, currentUser.length-10).toString()
    }

    private fun showToast(text: String){
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
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
        Toast.makeText(requireContext(), "Error getting data" + exception.message, Toast.LENGTH_LONG).show()
    }

    override fun onItemClicked(item: FullData) {
        val intent = Intent(requireContext(), ChatPage::class.java)
        intent.putExtra(SearchPage.TAG, item.nickname)
        startActivity(intent)
    }
}