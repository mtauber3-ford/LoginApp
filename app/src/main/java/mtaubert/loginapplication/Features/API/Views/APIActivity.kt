package mtaubert.loginapplication.Features.API.Views

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import mtaubert.loginapplication.Data.DB.Model.User
import mtaubert.loginapplication.Data.Remote.Model.Card
import mtaubert.loginapplication.Features.API.ViewModels.APIViewModel
import mtaubert.loginapplication.R
import mtaubert.loginapplication.Utils.Activities.BaseActivity

class APIActivity: BaseActivity() {

    lateinit var apiViewModel: APIViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity_main)
        actionBar?.title = "Scryfall Search"
        apiViewModel = ViewModelProviders.of(this).get(APIViewModel::class.java)
        apiViewModel.setCurrentUser(intent.extras?.get("currentUser") as User)
        changeFragment("landing")

    }

    fun contextualBackButtonPressed() {
        if(apiViewModel.getCurrentSearch() != null && apiViewModel.getCurrentSearch()!!.data.size != 1) {
            showCards(apiViewModel.getCurrentSearch()!!.data)
        } else {
            apiViewModel.clearSearchCache()
            changeFragment("landing")
        }
    }

    /**
     * Loads either card view to show one or a list of cards to the user
     */
    fun showCards(cards:List<Card>) {
        val fragment = if(cards.size == 1) {
            apiViewModel.setCurrentCard(cards[0])
            OneCardViewFragment.newInstance(cards[0])
        } else {
            MultiCardViewFragment.newInstance(cards)
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.root_layout, fragment, "cards")
            .commit()
    }

    /**
     * Generic fragment changing
     */
    fun changeFragment(target: String) {
        val fragment = when(target) {
            "landing" -> LandingFragment.newInstance()
            //"oneCard" -> OneCardViewFragment.newInstance()
            else -> LandingFragment.newInstance()
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.root_layout, fragment, target)
            .commit()
    }
}