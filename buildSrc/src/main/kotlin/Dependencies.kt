object Versions {
    // Auth
    const val omhAuth = "1.0.0-beta"
}

object Dependencies {
    // Auth
    val omhNonGmsAuthLibrary by lazy { "com.openmobilehub.android:auth-api-non-gms:${Versions.omhAuth}" }
    val omhGmsAuthLibrary by lazy { "com.openmobilehub.android:auth-api-gms:${Versions.omhAuth}" }
}
