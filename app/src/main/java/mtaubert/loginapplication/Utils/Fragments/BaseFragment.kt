package mtaubert.loginapplication.Utils.Fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import mtaubert.loginapplication.Features.Login.Models.LoginModel

abstract class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
    }
}