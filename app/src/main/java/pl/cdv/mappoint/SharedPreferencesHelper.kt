package pl.cdv.mappoint

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "MySharedPreferences",
        Context.MODE_PRIVATE
    )
    fun saveKeyToSharedPreferences(key:String,name: String) {
        with(sharedPreferences.edit()) {
            putString(key, name)
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
