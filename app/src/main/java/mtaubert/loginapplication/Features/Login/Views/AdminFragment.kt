package mtaubert.loginapplication.Features.Login.Views


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.runBlocking
import mtaubert.loginapplication.R
import mtaubert.loginapplication.Utils.Adapters.UserListAdapter
import mtaubert.loginapplication.Utils.Fragments.BaseLoginFragment
import mtaubert.loginapplication.databinding.LoginFragmentAdminBinding

class AdminFragment : BaseLoginFragment() {

    companion object {
        fun newInstance(): AdminFragment {
            return AdminFragment()
        }
    }

    private lateinit var userAdapter: UserListAdapter //Adapter for the recycler view

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<LoginFragmentAdminBinding>(inflater,
            R.layout.login_fragment_admin,container,false)

        setupRecyclerView(binding)

        binding.deleteButton.setOnClickListener {
            deleteUsersFromList()
        }

        return binding.root
    }

    /**
     * Sets up the recycler view
     */
    private fun setupRecyclerView(binding:LoginFragmentAdminBinding) {
        val userList = binding.userListView
        userAdapter = UserListAdapter()
        userList.adapter = userAdapter
        userList.layoutManager = LinearLayoutManager(context!!)
        loadUserList()
    }

    /**
     * Gets a list of users registered and saved in the DB
     */
    private fun loadUserList() = runBlocking {
        val users = loginViewModel.getAllUsers()
        userAdapter.setUsers(users)
    }

    /**
     * Deletes one or multiple users
     */
    private fun deleteUsersFromList() = runBlocking {
        val selectedUsers = userAdapter.selectedUsers
        val warning = loginViewModel.deleteUsersFromDB(selectedUsers)

        if(warning != null) { //Show message if user tried to delete an empty list of users
            Toast.makeText(activity, warning, Toast.LENGTH_LONG).show()
        } else {
            loadUserList()
        }
    }
}
