package com.example.footpath

import android.app.Application
import android.preference.PreferenceManager
import com.example.footpath.util.AccountManager
import com.example.footpath.util.TokenManager
import org.osmdroid.config.Configuration

class FootPathApp : Application() {

    companion object {
        lateinit var accountManager: AccountManager
            private set
    }

    override fun onCreate() {
        super.onCreate()

        accountManager = AccountManager(applicationContext)

        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )
    }
}