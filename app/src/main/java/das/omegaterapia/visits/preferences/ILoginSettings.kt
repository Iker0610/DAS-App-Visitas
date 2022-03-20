package das.omegaterapia.visits.preferences

interface ILoginSettings {
    suspend fun getLastLoggedUser(): String?
    suspend fun setLastLoggedUser(value: String)
}