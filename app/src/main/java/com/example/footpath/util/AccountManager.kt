package com.example.footpath.util

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class StoredAccount(
    val userId: String,
    val email: String,
    val role: String,
    val token: String
)

class AccountManager(context: Context) {

    private val gson = Gson()

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "user_accounts_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val SAVED_ACCOUNTS_KEY = "saved_accounts_list"
        private const val ACTIVE_ACCOUNT_ID_KEY = "active_account_id"
    }

    fun getAccounts(): List<StoredAccount> {
        val json = sharedPreferences.getString(SAVED_ACCOUNTS_KEY, null) ?: return emptyList()
        val type = object : TypeToken<List<StoredAccount>>() {}.type
        return gson.fromJson(json, type)
    }

    fun addOrUpdateAccount(account: StoredAccount) {
        val accounts = getAccounts().toMutableList()

        accounts.removeAll { it.userId.startsWith("temp_") }

        val existingAccountIndex = accounts.indexOfFirst {
            it.email == account.email
        }

        if (existingAccountIndex != -1) {
            accounts[existingAccountIndex] = account
        } else {
            accounts.add(account)
        }
        saveAccountsList(accounts)
    }

    fun setActiveAccount(userId: String) {
        sharedPreferences.edit().putString(ACTIVE_ACCOUNT_ID_KEY, userId).commit()
    }

    fun getActiveAccount(): StoredAccount? {
        val activeId = sharedPreferences.getString(ACTIVE_ACCOUNT_ID_KEY, null)
        return getAccounts().find { it.userId == activeId }
    }

    fun getActiveToken(): String? {
        return getActiveAccount()?.token
    }

    fun logout(userId: String) {
        val accounts = getAccounts().toMutableList()
        val activeAccount = getActiveAccount()

        accounts.removeAll { it.userId == userId }

        if (activeAccount?.userId == userId) {
            if (accounts.isNotEmpty()) {
                setActiveAccount(accounts.first().userId)
            } else {
                sharedPreferences.edit().remove(ACTIVE_ACCOUNT_ID_KEY).commit()
            }
        }

        saveAccountsList(accounts)
    }

    fun logoutAll() {
        sharedPreferences.edit().clear().commit()
    }

    private fun saveAccountsList(accounts: List<StoredAccount>) {
        val json = gson.toJson(accounts)
        sharedPreferences.edit().putString(SAVED_ACCOUNTS_KEY, json).commit()
    }
}