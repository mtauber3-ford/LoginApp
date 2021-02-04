package mtaubert.loginapplication.Utils

import java.io.IOException

class InvalidSearchException(val details: String) : IOException() {

    override val message: String
        get() = details
}