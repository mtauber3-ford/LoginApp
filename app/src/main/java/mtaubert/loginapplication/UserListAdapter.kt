package mtaubert.loginapplication

import android.app.PendingIntent.getActivity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import mtaubert.loginapplication.DBHelper.DBHelper
import mtaubert.loginapplication.Model.User

class ViewHolder(v: View) {
    val tvEmail: TextView = v.findViewById(R.id.email_text)
    val tvPassword: TextView = v.findViewById(R.id.password_text)
    val tvName: TextView = v.findViewById(R.id.name_text)
    //val btnDelete: Button = v.findViewById(R.id.delete_button)
}

class UserListAdapter(context: Context, private val resource: Int, private val entries: ArrayList<User>) :
    ArrayAdapter<User>(context, resource) {

    private val inflater = LayoutInflater.from(context)
    val selectedUsers = ArrayList<User>()

    override fun getCount(): Int {
        return entries.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(resource, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        //val db = DBHelper(context)

        val currentEntry = entries[position]
        viewHolder.tvEmail.text = currentEntry.email
        viewHolder.tvPassword.text = currentEntry.password
        viewHolder.tvName.text = currentEntry.name

        return view
    }

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
    }

    fun longPress(position: Int, v: View) {

        if(selectedUsers.contains(entries[position])) {
            selectedUsers.remove(entries[position])
            v.setBackgroundResource(R.color.unselected)
        } else {
            selectedUsers.add(entries[position])
            v.setBackgroundResource(R.color.selected)
        }


    }
}

