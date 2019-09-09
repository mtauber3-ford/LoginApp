package mtaubert.loginapplication.Fragments

/**
 * Account Details
 * Fragment that shows the user their details and allows editing
 */

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mtaubert.loginapplication.Activities.LoginActivity
import mtaubert.loginapplication.Data.User
import mtaubert.loginapplication.R
import mtaubert.loginapplication.databinding.FragmentAccountDetailsBinding

class AccountDetailsFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentAccountDetailsBinding>(inflater,
            R.layout.fragment_account_details,container,false)

        //Add the back button to the top
        (activity as LoginActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)

        populateEditTexts(binding)

        setButtonListeners(binding)

        return binding.root
    }

    /**
     * Back button pressed
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                (activity as LoginActivity).onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Sets the values for the editTextViews to show the user's info
     */
    private fun populateEditTexts(binding:FragmentAccountDetailsBinding) {
        val user = (activity as LoginActivity).currentUser
        if (user != null) {
            binding.nameInput.setText(user.name)
            binding.emailInput.setText(user.email)
            binding.passwordInput.setText(user.password)
        }
    }

    /**
     * Sets up button listeners
     */
    private fun setButtonListeners(binding:FragmentAccountDetailsBinding) {
        binding.saveButton.setOnClickListener {
           saveDetails(binding.emailInput.text.toString(), binding.passwordInput.text.toString(), binding.nameInput.text.toString())
        }

        val preferences = (activity as LoginActivity).getPreferences(Context.MODE_PRIVATE)

        val darkMode = preferences.getString("current_theme", "LightTheme") == "DarkTheme"
        if(binding.darkModeSwitch.isChecked != darkMode) {
            binding.darkModeSwitch.isChecked = darkMode
        }
//        binding.darkModeSwitch.isChecked = true
        binding.darkModeSwitch.setOnClickListener {
            toggleDarkMode()
        }
    }

    /**
     * Sets the theme to dark or light mode
     */
    private fun toggleDarkMode() {
        (activity as LoginActivity).toggleTheme()
        val currentTheme = (activity as LoginActivity).getPreferences(Context.MODE_PRIVATE).getString("current_theme", null)
        Toast.makeText(activity, "Switched to : $currentTheme" , Toast.LENGTH_LONG).show()
    }


    /**
     * Saves the new user details to the db
     */
    private fun saveDetails(email: String, password: String, name: String) {
        val userDao = (activity as LoginActivity).db.userDao()
        GlobalScope.launch {
            if((activity as LoginActivity).currentUser != null) {
                val users = userDao.getUser(email)
                val  updatedUser = User(email, password, name)
                if(users.isEmpty()) { //User has set a new email
                    userDao.deleteUser((activity as LoginActivity).currentUser!!) //Get rid of the saved user data for the old email
                    userDao.insert(updatedUser) //Add the user back with the new email
                } else { //User just changed password or name
                    userDao.updateUser(updatedUser)
                }
                (activity as LoginActivity).currentUser = updatedUser
                activity?.runOnUiThread{
                    Toast.makeText(activity, "Updated account details!" , Toast.LENGTH_LONG).show()
                }
            } else {
                activity?.runOnUiThread{
                    Toast.makeText(activity, "An error occurred, please try again!" , Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
