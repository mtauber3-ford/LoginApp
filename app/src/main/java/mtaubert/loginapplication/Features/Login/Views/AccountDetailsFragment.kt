package mtaubert.loginapplication.Features.Login.Views

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
import kotlinx.coroutines.runBlocking
import mtaubert.loginapplication.Data.DB.Model.User
import mtaubert.loginapplication.Features.Login.Models.LoginModel
import mtaubert.loginapplication.Features.Login.ViewModels.LoginViewModel
import mtaubert.loginapplication.R
import mtaubert.loginapplication.Utils.Fragments.BaseLoginFragment
import mtaubert.loginapplication.databinding.FragmentAccountDetailsBinding

class AccountDetailsFragment : BaseLoginFragment() {


    companion object {
        fun newInstance(): AccountDetailsFragment {
            return AccountDetailsFragment()
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentAccountDetailsBinding>(inflater,
            R.layout.fragment_account_details,container,false)

        //Add the back button to the top
        (activity as LoginActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)

        populateEditTexts(binding)
        setupButtonListeners(binding)

        return binding.root
    }

    private fun setupButtonListeners(binding: FragmentAccountDetailsBinding) {
        val preferences = (activity as LoginActivity).getPreferences(Context.MODE_PRIVATE)
        val darkMode = preferences.getString("current_theme", "LightTheme") == "DarkTheme"
        if(binding.darkModeSwitch.isChecked != darkMode) {
            binding.darkModeSwitch.isChecked = darkMode
        }
        binding.darkModeSwitch.setOnClickListener {
            toggleDarkMode()
        }

        binding.saveButton.setOnClickListener {
            saveNewUserDetails(
                binding.emailInput.text.toString(),
                binding.passwordInput.text.toString(),
                binding.nameInput.text.toString())
        }
    }

    private fun saveNewUserDetails(email: String, password: String, name: String) = runBlocking {
        val user = User(email, password, name)
        val saved = loginViewModel.updateUserDetails(user)

        if(saved == null) {
            Toast.makeText(activity, "Details saved!", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(activity, saved, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Sets the values for the editTextViews to show the user's info
     */
    private fun populateEditTexts(binding:FragmentAccountDetailsBinding) {
        val user = loginViewModel.getCurrentUser()
        if (user != null) {
            binding.nameInput.setText(user.name)
            binding.emailInput.setText(user.email)
            binding.passwordInput.setText(user.password)
        }
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
     * Sets the theme to dark or light mode
     */
    private fun toggleDarkMode() {
        (activity as LoginActivity).toggleTheme()
        val currentTheme = (activity as LoginActivity).getPreferences(Context.MODE_PRIVATE).getString("current_theme", null)
        Toast.makeText(activity, "Switched to : $currentTheme" , Toast.LENGTH_SHORT).show()
    }
}
