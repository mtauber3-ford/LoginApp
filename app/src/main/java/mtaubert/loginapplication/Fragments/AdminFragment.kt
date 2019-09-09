package mtaubert.loginapplication.Fragments


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
import mtaubert.loginapplication.Activities.LoginActivity
import mtaubert.loginapplication.Data.User
import mtaubert.loginapplication.DataAccessObjects.UserDAO
import mtaubert.loginapplication.R
import mtaubert.loginapplication.UserListAdapter
import mtaubert.loginapplication.databinding.FragmentAdminBinding

class AdminFragment : Fragment() {

    private lateinit var userAdapter: UserListAdapter //Adapter for the recycler view
    private lateinit var userDao: UserDAO

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAdminBinding>(inflater,
            R.layout.fragment_admin,container,false)

        userDao = (activity as LoginActivity).db.userDao()

        setupRecyclerView(binding)

        binding.deleteButton.setOnClickListener {
            deleteUsersFromList()
        }

        return binding.root
    }

    /**
     * Sets up the recycler view
     */
    private fun setupRecyclerView(binding:FragmentAdminBinding) {
        val userList = binding.userListView
        userAdapter = UserListAdapter(context!!)
        userList.adapter = userAdapter
        userList.layoutManager = LinearLayoutManager(context!!)
        loadUserList()
    }

    /**
     * Gets a list of users registered and saved in the DB
     */
    private fun loadUserList() {
        GlobalScope.launch {
            val users = userDao.getAllUsers()
            activity?.runOnUiThread(Runnable { userAdapter.setUsers(users) })
        }
    }

    /**
     * Deletes one or multiple users
     */
    private fun deleteUsersFromList() {
        val selectedUsers = userAdapter.selectedUsers
        if(selectedUsers.isEmpty()) { //No users selected to delete
            Toast.makeText(activity, "No users to delete selected!", Toast.LENGTH_LONG).show()
        } else { //Some number of users selected
            GlobalScope.launch {
                for(user: User in selectedUsers) {
                    userDao.deleteUser(user)
                }
                loadUserList()
            }
        }
    }
}
