package ge.asurguladze.finalproject

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ge.asurguladze.finalproject.adapters.ChatListAdapter
import ge.asurguladze.finalproject.database.ChatPagePresenter
import ge.asurguladze.finalproject.database.IChatPageView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_page)

        initializeViews()
        setListeners()

        presenter.getAllMessages(fromUserNickname, toUserNickname)
        presenter.getUserData(fromUserNickname)

    }

    private fun initializeViews() {

        presenter = ChatPagePresenter(this)

        messagesRv = findViewById(R.id.messages_rv)
        messages = arrayListOf()
        val adapter = ChatListAdapter(messages)
        messagesRv.adapter = adapter

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
    }

    private fun setListeners() {

        back.setOnClickListener {
            finish()
        }

        textInputLayout.setEndIconOnClickListener{

            val curMessage = Message(fromUserNickname, message.text.toString(), System.currentTimeMillis())
            message.setText("")

            messages.add(curMessage)
            messagesRv.adapter?.notifyItemInserted(messages.size)
            
            presenter.addNewMessage(curMessage, fromUserNickname, toUserNickname)

        }

    }

    override fun addMessagesToList(allMessages: ArrayList<Message>) {
        messages.removeAll(messages)

        for(message in allMessages){
            messages.add(message)
        }

        messagesRv.adapter?.notifyDataSetChanged()
    }

    override fun showUserInfo(user: User) {
        userNickname.text = user.nickname
        userProfession.text = user.profession

        if (user.image != null) {
            userImage.setImageBitmap(user.image)
        }
    }

}