package pl.cdv.mappoint

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "MySharedPreferences",
        Context.MODE_PRIVATE
    )
    fun saveNameToSharedPreferences(key:String,name: String) {
        with(sharedPreferences.edit()) {
            putString(key, name)
            apply()
        }
    }

    fun getNameFromSharedPreferences(key:String): String? {
        return sharedPreferences.getString(key, "")
    }
}
