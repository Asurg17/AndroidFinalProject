package ge.asurguladze.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChatPage : AppCompatActivity() {

    private lateinit var message: TextInputEditText
    private lateinit var messagesRv: RecyclerView
    private lateinit var fromUserNickname: String
    private lateinit var toUserNickname: String
    private lateinit var textInputLayout: TextInputLayout

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_page)

        initializeViews()
        setListeners()

        Log.d("bladefewf", "$fromUserNickname $toUserNickname")
    }

    private fun initializeViews() {
        message = findViewById(R.id.message)
        messagesRv = findViewById(R.id.messages_rv)
        textInputLayout = findViewById(R.id.message_text_input_layout)

        auth = Firebase.auth
        val currentUser = auth.currentUser?.email.toString()
        fromUserNickname = currentUser.subSequence(0, currentUser.length-10).toString()

        toUserNickname = intent.getStringExtra(SearchPage.TAG).toString()
    }

    private fun setListeners() {
        textInputLayout.setEndIconOnClickListener{

            val curMessage = message.text.toString()
            message.setText("")

            var a = System.currentTimeMillis()

            if(a==a){
                var b= 2
            }

            Log.d("BRREREgf", curMessage)
        }
    }
}