package mtaubert.loginapplication.Utils.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mtaubert.loginapplication.Data.DB.Model.User
import mtaubert.loginapplication.R

class UserListAdapter internal constructor() : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

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
//    private lateinit var inflater = LayoutInflater.from()
    val selectedUsers = ArrayList<User>()

    /**
     * Creates and returns a view holder for the list
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val userView = inflater.inflate(R.layout.utils_user_entry_item, parent, false)
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

        holder.itemView.setOnLongClickListener {
            longPress(position, it)
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

        if(selectedUsers.contains(usersList[position])) { //Un-highlights
            selectedUsers.remove(usersList[position])
            v.setBackgroundResource(R.color.unselected)
        } else { //Highlights
            selectedUsers.add(usersList[position])
            v.setBackgroundResource(R.color.selected)
        }
    }
}

