package mtaubert.loginapplication.Utils.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import mtaubert.loginapplication.Features.Login.ViewModels.LoginViewModel

open class BaseLoginFragment : Fragment() {
    protected lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        loginViewModel = ViewModelProviders.of(this.activity!!).get(LoginViewModel::class.java) //Grabs the loginviewmodel
        super.onCreate(savedInstanceState)
    }
}