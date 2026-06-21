package com.example.jagajalan.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREF_NAME, Context.MODE_PRIVATE
    )

    fun saveUser(userId: String, userName: String, email: String) {
        prefs.edit().apply {
            putString(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, userName)
            putString(KEY_EMAIL, email)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    fun getUserId(): String = prefs.getString(KEY_USER_ID, "") ?: ""
    fun getUserName(): String = prefs.getString(KEY_USER_NAME, "") ?: ""
    fun getEmail(): String = prefs.getString(KEY_EMAIL, "") ?: ""
    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun clearUser() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val PREF_NAME = "jagajalan_prefs"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_EMAIL = "email"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }
}