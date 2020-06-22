package `in`.projecteka.jataayu.module

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val MASTER_KEY_NAME = "JataayuPrefKey"
internal const val ENCRYPTED_PREFS = "encrypted_pref"
internal const val DEFAULT_PREFS = "default_pref"

internal const val PREFS_NAME = "JataayuPreferences"

val preferenceModule = module {

    single(named(MASTER_KEY_NAME)) { masterKeyValue() }

    single(named(ENCRYPTED_PREFS)) {
        encryptedSharedPrefs(androidContext(), get(named(MASTER_KEY_NAME)))
    }

    single(named(DEFAULT_PREFS)) { PreferenceManager.getDefaultSharedPreferences(androidContext()) }
}

fun encryptedSharedPrefs(context: Context, masterKeyValue: String): SharedPreferences = EncryptedSharedPreferences.create(
    PREFS_NAME,
    masterKeyValue,
    context,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
)

private fun masterKeyValue() =
    MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
