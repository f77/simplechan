package io.github.f77.simplechan.repositories

import okhttp3.OkHttpClient

abstract class AbstractRepository : ImageboardRepositoryInterface {
    override val httpClient: OkHttpClient = OkHttpClient()

    override val schema: String = "https://"
}
