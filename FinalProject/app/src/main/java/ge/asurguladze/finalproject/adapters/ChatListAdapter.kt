package ge.asurguladze.finalproject.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ge.asurguladze.finalproject.R
import ge.asurguladze.finalproject.models.Message
import ge.asurguladze.finalproject.models.User

class ChatListAdapter(private var messages: ArrayList<Message>): RecyclerView.Adapter<MessageViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return messages.size
    }


}

class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
//    var userImage: ImageView = itemView.findViewById(R.id.user_image)
//    var userName: TextView = itemView.findViewById(R.id.user_name)
//    var userProfession: TextView = itemView.findViewById(R.id.user_profession)
}