package org.thoughtcrime.securesms

import android.content.Context
import com.omh.android.auth.api.OmhAuthClient
import com.omh.android.auth.api.OmhAuthProvider
import org.wordpress.android.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    @Provides
    fun providesOmhAuthClient(@ApplicationContext context: Context): OmhAuthClient {
        return OmhAuthProvider.Builder()
            .addNonGmsPath()
            .addGmsPath()
            .build()
            .provideAuthClient(
                context = context,
                scopes = listOf(
                    "openid",
                    "email",
                    "profile",
                    "https://www.googleapis.com/auth/drive",
                    "https://www.googleapis.com/auth/drive.file"
                ),
                clientId = BuildConfig.CLIENT_ID
            )
    }
}
