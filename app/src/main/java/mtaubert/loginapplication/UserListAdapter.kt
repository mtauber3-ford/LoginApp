package mtaubert.loginapplication

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import mtaubert.loginapplication.Data.User

class UserListAdapter internal constructor(context: Context) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    /**
     * UserViewHolder
     * Class for the user info view for the recylcer view
     */
    inner class UserViewHolder(userView: View): RecyclerView.ViewHolder(userView) {
        val tvEmail: TextView = userView.findViewById(R.id.email_text)
        val tvPassword: TextView = userView.findViewById(R.id.password_text)
        val tvName: TextView = userView.findViewById(R.id.name_text)
    }

    private var usersList = emptyList<User>() //List of current users
    private val inflater = LayoutInflater.from(context)
    val selectedUsers = ArrayList<User>()
    //val selectedUsers: LiveData<ArrayList<User()>>

    /**
     * Creates and returns a view holder for the list
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val userView = inflater.inflate(R.layout.user_entry_item, parent, false)
        return UserViewHolder(userView)
    }

    /**
     * Returns the number of items in the list
     */
    override fun getItemCount(): Int {
        return usersList.size
    }

    /**
     * Adds data from the database to the viewholder
     */
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val current = usersList[position]
        holder.tvEmail.text = current.email
        holder.tvPassword.text = current.password
        holder.tvName.text = current.name
        holder.itemView.setOnLongClickListener {v:View ->
            longPress(position, v)
            true
        }
        holder.itemView.setBackgroundResource(R.color.unselected)
    }

    /**
     * Sets the list of users
     */
    internal fun setUsers(users: List<User>) {
        this.usersList = users
        notifyDataSetChanged()
    }

    /**
     * Adds and removes users from the selected view
     */
    private fun longPress(position: Int, v: View) {

        if(selectedUsers.contains(usersList[position])) {
            selectedUsers.remove(usersList[position])
            v.setBackgroundResource(R.color.unselected)
        } else {
            selectedUsers.add(usersList[position])
            v.setBackgroundResource(R.color.selected)
        }


    }
}

