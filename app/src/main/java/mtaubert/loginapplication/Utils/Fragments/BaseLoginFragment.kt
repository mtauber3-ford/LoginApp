package mtaubert.loginapplication.Utils.Fragments

import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import mtaubert.loginapplication.Features.Login.ViewModels.LoginViewModel

open class BaseLoginFragment : Fragment() {
    protected lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        loginViewModel = ViewModelProviders.of(this.activity!!).get(LoginViewModel::class.java)
        super.onCreate(savedInstanceState)
    }
}