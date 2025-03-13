package dev.androidbroadcast.githubclientapp.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubApi {
    @GET("orgs/{org}/repos")
    suspend fun getRepositories(@Path("org") org: String): Response<List<Repository>>

    @GET("repos/{owner}/{repo}")
    suspend fun getRepositoryDetails(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
    ): Response<RepositoryDetails>
}