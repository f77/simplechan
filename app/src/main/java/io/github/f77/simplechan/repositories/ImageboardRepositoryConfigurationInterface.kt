package io.github.f77.simplechan.repositories

import okhttp3.OkHttpClient

interface ImageboardRepositoryConfigurationInterface {
    val schema: String

    val domain: String

    val httpClient: OkHttpClient
}
