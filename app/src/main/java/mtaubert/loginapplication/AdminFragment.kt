package mtaubert.loginapplication


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mtaubert.loginapplication.Data.User
import mtaubert.loginapplication.databinding.FragmentAdminBinding

class AdminFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAdminBinding>(inflater,
            R.layout.fragment_admin,container,false)


        val userList = binding.userListView
        val userAdapter = UserListAdapter(context!!)
        userList.adapter = userAdapter
        userList.layoutManager = LinearLayoutManager(context!!)
        loadUserList(userAdapter)

//        binding.userListView.addItemLongClickListener { parent, view, position, id ->
//            userAdapter.longPress(position, view)
//            if(userAdapter.selectedUsers.size > 0) {
//                binding.deleteButton.visibility = View.VISIBLE
//            } else {
//                binding.deleteButton.visibility = View.INVISIBLE
//            }
//            true
//
//
        binding.deleteButton.setOnClickListener {
            val selectedUsers = userAdapter.selectedUsers
            if(selectedUsers.isEmpty()) {
                Toast.makeText(activity, "No users to delete selected!", Toast.LENGTH_LONG).show()
            } else {
                GlobalScope.launch {
                    val userDao = (activity as LoginActivity).db.userDao()
                    for(user: User in selectedUsers) {
                        userDao.deleteUser(user)
                    }
                   loadUserList(userAdapter)
                }
            }
           //TODO: Add functionality for deleting users
        }

        return binding.root
    }

    private fun loadUserList(adapter: UserListAdapter) {
        GlobalScope.launch {
            val users = (activity as LoginActivity).db.userDao().getAllUsers()
            activity?.runOnUiThread(Runnable { adapter.setUsers(users) })
        }
    }
}
