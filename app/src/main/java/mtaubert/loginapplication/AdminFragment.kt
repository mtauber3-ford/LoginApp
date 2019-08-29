package mtaubert.loginapplication


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import mtaubert.loginapplication.DBHelper.DBHelper
import mtaubert.loginapplication.databinding.FragmentAdminBinding
import mtaubert.loginapplication.databinding.FragmentLoginBinding

class AdminFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAdminBinding>(inflater,
            R.layout.fragment_admin,container,false)

        val db = DBHelper(context!!)
        //val userAdapter = UserListAdapter(context!!, R.layout.user_entry_item, (activity as LoginActivity).db.allUser)
        val userAdapter = UserListAdapter(context!!, R.layout.user_entry_item, db.allUser)
        binding.userListView.adapter = userAdapter

        binding.userListView.setOnItemLongClickListener { parent, view, position, id ->
            userAdapter.longPress(position, view)
            if(userAdapter.selectedUsers.size > 0) {
                binding.deleteButton.visibility = View.VISIBLE
            } else {
                binding.deleteButton.visibility = View.INVISIBLE
            }
            true
        }

        binding.deleteButton.setOnClickListener {
            db.removeUsers(userAdapter.selectedUsers)
            //userAdapter.notifyDataSetChanged()
            val userAdapter = UserListAdapter(context!!, R.layout.user_entry_item, db.allUser)
            binding.userListView.adapter = userAdapter
        }

        return binding.root
    }
}
