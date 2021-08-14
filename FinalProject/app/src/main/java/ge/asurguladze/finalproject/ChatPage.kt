package ge.asurguladze.finalproject

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ge.asurguladze.finalproject.adapters.ChatListAdapter
import ge.asurguladze.finalproject.database.chatPage.ChatPageInteractor
import ge.asurguladze.finalproject.database.chatPage.ChatPagePresenter
import ge.asurguladze.finalproject.database.chatPage.IChatPageView
import ge.asurguladze.finalproject.models.Message
import ge.asurguladze.finalproject.models.User

class ChatPage : AppCompatActivity(), IChatPageView {

    private lateinit var presenter: ChatPagePresenter

    private lateinit var back: ImageButton
    private lateinit var userNickname: TextView
    private lateinit var userProfession: TextView
    private lateinit var userImage: ImageView
    private lateinit var message: TextInputEditText
    private lateinit var messagesRv: RecyclerView
    private lateinit var fromUserNickname: String
    private lateinit var toUserNickname: String
    private lateinit var textInputLayout: TextInputLayout

    private lateinit var auth: FirebaseAuth

    private lateinit var messages: ArrayList<Message>

    private var checker: Boolean = false

    private lateinit var dialog: AlertDialog

    private lateinit var eventListener: ChildEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_page)

        initializeViews()
        setListeners()
    }

    override fun onStart() {
        super.onStart()

        dialog.show()
        presenter.getAllData(fromUserNickname, toUserNickname)
    }

    override fun onDestroy() {

        Firebase.database
            .getReference(ChatPageInteractor.MESSAGE_TAG)
            .child(fromUserNickname)
            .child("$fromUserNickname:$toUserNickname")
            .removeEventListener(eventListener)

        super.onDestroy()
    }

    private fun initializeViews() {

        back = findViewById(R.id.back)
        userNickname = findViewById(R.id.user_nickname)
        userProfession = findViewById(R.id.user_profession)
        userImage = findViewById(R.id.from_user_image)
        message = findViewById(R.id.message)
        textInputLayout = findViewById(R.id.message_text_input_layout)

        auth = Firebase.auth
        val currentUser = auth.currentUser?.email.toString()
        fromUserNickname = currentUser.subSequence(0, currentUser.length-10).toString()
        toUserNickname = intent.getStringExtra(SearchPage.TAG).toString()

        messagesRv = findViewById(R.id.messages_rv)
        messages = arrayListOf()
        val adapter = ChatListAdapter(messages)
        messagesRv.adapter = adapter

        presenter = ChatPagePresenter(this)

        dialog = AlertDialog.Builder(this)
            .setView(R.layout.loading_dialog_layout)
            .setCancelable(false)
            .create()
    }

    private fun setListeners() {

        back.setOnClickListener {
            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)
        }

        textInputLayout.setEndIconOnClickListener{
            val curMessage = Message(fromUserNickname, message.text.toString(), System.currentTimeMillis())
            message.setText("")

            messages.add(curMessage)
            messagesRv.adapter?.notifyItemInserted(messages.size)
            scrollDown()

            presenter.addNewMessage(curMessage, fromUserNickname, toUserNickname)
        }

        val query = Firebase.database
                .getReference(ChatPageInteractor.MESSAGE_TAG)
                .child(fromUserNickname)
                .child("$fromUserNickname:$toUserNickname")

        eventListener  = query.addChildEventListener(object: ChildEventListener{
                            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                                val value = snapshot.value as HashMap<*, *>

                                if(value[ChatPageInteractor.SENDER].toString() != fromUserNickname && checker){

                                    messages.add(Message(value[ChatPageInteractor.SENDER].toString(),
                                                         value[ChatPageInteractor.MESSAGE].toString(),
                                                         value[ChatPageInteractor.TIME].toString().toLong()))

                                    messagesRv.adapter?.notifyItemInserted(messages.size)
                                    scrollDown()
                                }
                            }

                            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                            override fun onChildRemoved(snapshot: DataSnapshot) {}
                            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(this@ChatPage, error.message, Toast.LENGTH_SHORT).show()
                            }
                        })
    }


    // IChatPageView functions
    override fun showAllData(messages: ArrayList<Message>, user: User) {
        showUserInfo(user)
        addMessagesToList(messages)
        dialog.dismiss()
    }

    override fun showError(exception: Exception) {
        dialog.dismiss()
        Toast.makeText(this, "Error getting data" + exception.message, Toast.LENGTH_LONG).show()
    }


    // Help functions
    private fun showUserInfo(user: User) {
        userNickname.text = user.nickname
        userProfession.text = user.profession

        if (user.image != null) {
            userImage.setImageBitmap(user.image)
        }
    }

    private fun addMessagesToList(allMessages: ArrayList<Message>) {
        messages.removeAll(messages)

        for(message in allMessages){
            messages.add(message)
        }

        messagesRv.adapter?.notifyDataSetChanged()
        scrollDown()

        checker = true // all messages were rendered
    }


    private fun scrollDown(){
        messagesRv.scrollToPosition(messages.size - 1)
    }

}
