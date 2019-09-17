package mtaubert.loginapplication.Features.API.Models

import mtaubert.loginapplication.Data.DB.Model.User

data class APIModel(var currentUser: User? = null, var apiAddress:String = "", var apiKey: String = "", var result: String = "")