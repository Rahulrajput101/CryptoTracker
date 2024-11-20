package com.plcoding.cryptotracker.core.data.neworking

import com.plcoding.cryptotracker.BuildConfig

fun constructUrl(url: String): String{
    return when{
        // Case 1: If the input URL already contains the base URL, return it as-is.
        url.contains(BuildConfig.BASE_URL) -> url
        // Case 2: If the input URL starts with "/", remove the leading slash and append it to the base URL.
        url.startsWith("/") -> BuildConfig.BASE_URL+url.drop(1)
        // Case 3: For all other cases, directly append the input URL to the base URL.
        else -> BuildConfig.BASE_URL+url
    }
}