/*
 * Copyright (c) 2025 mittwerk
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package dev.taureg.freelanceapp

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import java.net.InetSocketAddress
import java.net.Proxy
import java.time.Duration

object SymbioNetwork {
    private const val PROXY_HOST = "127.0.0.1"
    private const val PROXY_PORT = 9050

    private const val BASE_URL =
        "http://2tpgl3gr4bba5o2y7g4alm5av6q34ilmaw5g3wywa3xy5gemnmmkycid.onion:9992"

    private val json =
        Json {
            ignoreUnknownKeys = true
        }

    private val okHttpClient =
        OkHttpClient
            .Builder()
            .proxy(Proxy(Proxy.Type.SOCKS, InetSocketAddress(PROXY_HOST, PROXY_PORT)))
            .connectTimeout(Duration.ofSeconds(30))
            .readTimeout(Duration.ofSeconds(30))
            .build()

    val retrofit: Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
}

@Serializable
data class HelloResponse(
    val message: String? = null,
    val status: String? = null,
)

interface SymbioTestApi {
    @GET("hello")
    suspend fun sayHello(): HelloResponse
}

val api: SymbioTestApi = SymbioNetwork.retrofit.create(SymbioTestApi::class.java)
