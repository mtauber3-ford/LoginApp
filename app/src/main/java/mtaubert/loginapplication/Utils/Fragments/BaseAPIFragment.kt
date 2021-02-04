package mtaubert.loginapplication.Utils.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import mtaubert.loginapplication.Features.API.Models.APIModel
import mtaubert.loginapplication.Features.API.ViewModels.APIViewModel
import mtaubert.loginapplication.Features.Login.ViewModels.LoginViewModel

open class BaseAPIFragment : Fragment() {
    protected lateinit var apiViewModel: APIViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        apiViewModel = ViewModelProviders.of(this.activity!!).get(APIViewModel::class.java) //Grabs the apiViewmodel
        super.onCreate(savedInstanceState)
    }
}