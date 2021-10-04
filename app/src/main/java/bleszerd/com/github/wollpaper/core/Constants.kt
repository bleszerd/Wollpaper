package bleszerd.com.github.wollpaper.core

import bleszerd.com.github.wollpaper.BuildConfig

object Constants {
    const val API_BASE_URL = "https://api.pexels.com/v1/"
    const val API_KEY = BuildConfig.API_KEY
    const val SEARCH_QUERY_ORIENTATION = "portrait"
    const val RESULTS_LIMIT_PER_PAGE = 80
    const val DEFAULT_SEARCH_KEYWORD = "beach"
}