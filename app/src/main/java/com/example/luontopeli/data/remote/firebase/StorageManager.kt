package com.example.luontopeli.data.remote.firebase

/**
 * Offline-tilassa toimiva tallennushallinta (no-op).
 * Kuvat säilytetään vain laitteen paikallisessa tallennustilassa.
 */
class StorageManager {
    suspend fun uploadImage(localFilePath: String, spotId: String): Result<String> {
        return Result.success(localFilePath) // Palauta paikallinen polku
    }
    suspend fun deleteImage(spotId: String): Result<Unit> = Result.success(Unit)
}
