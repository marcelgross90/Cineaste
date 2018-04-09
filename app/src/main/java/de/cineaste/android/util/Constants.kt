package de.cineaste.android.util

interface Constants {
    companion object {

        val DATABASE_VERSION = 4
        val DATABASE_NAME = "cineaste.db"

        val POSTER_BASE_URI = "https://image.tmdb.org/t/p/%s<posterName>?api_key=<API_KEY>"
        val POSTER_URI_SMALL = String.format(POSTER_BASE_URI, "w342")
        val POSTER_URI_ORIGINAL = String.format(POSTER_BASE_URI, "original")
    }
}