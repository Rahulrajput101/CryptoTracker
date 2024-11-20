package com.plcoding.cryptotracker.core.domain.util

import com.plcoding.cryptotracker.core.domain.Error

enum class NetworkError :Error{
    REQUEST_TIMEOUT,
    TOO_MANY_REQUEST,
    NO_INTERNET,
    SERVER_ERROR,
    SERIALIZATION,
    UNKNOWN,
}