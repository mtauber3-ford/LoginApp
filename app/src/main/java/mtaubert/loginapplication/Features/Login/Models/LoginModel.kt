package mtaubert.loginapplication.Features.Login.Models

import mtaubert.loginapplication.Data.DB.Model.User

data class LoginModel(var currentUser: User? = null)