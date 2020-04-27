package `in`.projecteka.jataayu.util.sharedPref

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager

fun Context.addToStringSet(key: String, value: String) {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    val editor = sharedPreferences.edit()
    val set = getStringSet(key)
    if (!set.contains(value)) {
        set.add(value)
        editor.remove(key).apply()
        editor.putStringSet(key, set)
        editor.commit()
    }
}

fun Context.getStringSet(key: String): HashSet<String> {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    val set = sharedPreferences.getStringSet(key, HashSet())
    return HashSet(set)
}