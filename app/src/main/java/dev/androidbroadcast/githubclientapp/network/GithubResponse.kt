package dev.androidbroadcast.githubclientapp.network

data class Repository(
    val name: String,
    val description: String,
    val fork: Boolean,
    val owner: Owner
)


data class RepositoryDetails(
    val name: String,
    val description: String,
    val forks_count: Int,
    val watchers_count: Int,
    val open_issues_count: Int,
    val parent: ParentRepo?
)

data class ParentRepo(
    val full_name: String
)

data class Owner(
    val login: String
)