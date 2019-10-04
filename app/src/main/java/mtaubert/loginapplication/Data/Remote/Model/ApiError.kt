package mtaubert.loginapplication.Data.Remote.Model

data class ApiError(
    val status: Int,
    val code: String,
    val details: String,
    val type: String,
    val warnings: Array<String>
)