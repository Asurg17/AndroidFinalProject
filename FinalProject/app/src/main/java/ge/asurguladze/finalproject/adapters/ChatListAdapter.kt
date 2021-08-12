package ge.asurguladze.finalproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ge.asurguladze.finalproject.R
import ge.asurguladze.finalproject.models.Message
import java.text.SimpleDateFormat
import java.util.*

class ChatListAdapter(private var messages: ArrayList<Message>): RecyclerView.Adapter<MessageViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.messages_list_item, parent,false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val nickname = getNickname()

        val message = messages[position]


        if(nickname == message.sender){

            holder.toMessage.visibility = View.GONE
            holder.toMessageTime.visibility = View.GONE
            holder.fromMessage.visibility = View.VISIBLE
            holder.fromMessageTime.visibility = View.VISIBLE

            holder.fromMessage.text = message.message
            holder.fromMessageTime.text = getTime(message.time!!)

        }else{

            holder.fromMessage.visibility = View.GONE
            holder.fromMessageTime.visibility = View.GONE
            holder.toMessage.visibility = View.VISIBLE
            holder.toMessageTime.visibility = View.VISIBLE

            holder.toMessage.text = message.message
            holder.toMessageTime.text = getTime(message.time!!)
        }

    }

    override fun getItemCount(): Int {
        return messages.size
    }

    private fun getNickname(): String {
        val auth = Firebase.auth
        val currentUser = auth.currentUser?.email.toString()

        return currentUser.subSequence(0, currentUser.length - 10).toString()
    }

}

fun getTime(time: Long): String{

    val formatter = SimpleDateFormat("MMM/dd/yyyy HH:mm", Locale.ENGLISH)
    val dateString = formatter.format(Date(time))

    val splitDate = dateString.split(' ')
    val firstPart = splitDate[0].split('/')
    val month = firstPart[0]
    val day = firstPart[1].toInt()
    val year = firstPart[2].toInt()


    val currentDate = formatter.format(Date(System.currentTimeMillis()))
    val splitCurrDate = currentDate.split(' ')
    val currFirstPart = splitCurrDate[0].split('/')
    val currMonth = currFirstPart[0]
    val currDay = currFirstPart[1].toInt()
    val currYear = currFirstPart[2].toInt()

    return if(year == currYear && month == currMonth && day == currDay){
        splitDate[1]
    }else{
        "$day $month"
    }

}

class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var fromMessageTime: TextView = itemView.findViewById(R.id.from_message_time)
    var toMessageTime: TextView = itemView.findViewById(R.id.to_message_time)
    var fromMessage: TextView = itemView.findViewById(R.id.from_message)
    var toMessage: TextView = itemView.findViewById(R.id.to_message)
}