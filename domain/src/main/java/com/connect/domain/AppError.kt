package com.connect.domain

sealed class AppError(val message: String) {

    sealed class NetworkError(errorMsg: String) : AppError(errorMsg) {
        object InvalidRequest : NetworkError("Invalid")
        object NotFound : NetworkError("Not Found")
        object UnknownError : NetworkError("Unknown Error")
    }

    //CUSTOME ERRORS
    sealed class TibberError(errorMsg: String) : AppError(errorMsg) {
        object PowerUpNotFound : TibberError("PowerUp not found under that Id")
        object CustomerTibberError : TibberError("This is another customer error")
    }

    object UnknownGenericError : AppError("Unknown Error")


}