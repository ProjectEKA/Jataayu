package `in`.projecteka.jataayu.util.sharedPref

import android.content.Context
import androidx.preference.PreferenceManager

fun Context.putInt(key: String, value: Int) {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    val editor = sharedPreferences.edit()
    editor.putInt(key, value)
    editor.apply()
}

fun Context.getInt(key: String, defaultValue: Int): Int {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    return sharedPreferences.getInt(key, defaultValue)
}

fun Context.putLong(key: String, value: Long) {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    val editor = sharedPreferences.edit()
    editor.putLong(key, value)
    editor.apply()
}

fun Context.getLong(key: String, defaultValue: Long): Long {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    return sharedPreferences.getLong(key, defaultValue)
}

fun Context.putBoolean(key: String, value: Boolean) {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    val editor = sharedPreferences.edit()
    editor.putBoolean(key, value)
    editor.apply()
}

fun Context.getBoolean(key: String, defaultValue: Boolean): Boolean {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    return sharedPreferences.getBoolean(key, defaultValue)
}

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

fun Context.putString(key: String, value: String) {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    val editor = sharedPreferences.edit()
    editor.putString(key, value)
    editor.apply()
}

fun Context.getString(key: String, defaultValue: String): String? {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    return sharedPreferences.getString(key, defaultValue)
}
