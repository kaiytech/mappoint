package pl.cdv.fitWalk

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "MySharedPreferences",
        Context.MODE_PRIVATE
    )
    fun saveKeyToSharedPreferences(key:String,value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getKeyFromSharedPreferences(key:String): String? {
        return sharedPreferences.getString(key, "")
    }
    fun clearKeyFromSharedPreferences(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
}
