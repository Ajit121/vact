package com.score.vact.vo

import java.io.IOException

data class ResponseException(val errorCode: Int, val errorMessage: String) : IOException()