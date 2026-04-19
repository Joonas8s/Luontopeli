package com.example.luontopeli.data.repository

import com.example.luontopeli.data.local.dao.NatureSpotDao
import com.example.luontopeli.data.local.entity.NatureSpot
import com.example.luontopeli.data.remote.firebase.AuthManager
import com.example.luontopeli.data.remote.firebase.FirestoreManager
import com.example.luontopeli.data.remote.firebase.StorageManager
import kotlinx.coroutines.flow.Flow

/**
 * Repository-luokka luontolöytöjen hallintaan (Repository-suunnittelumalli).
 *
 * Toimii välittäjänä tietolähteiden (Room-tietokanta) ja ViewModelien välillä.
 * Data tallennetaan ensin paikallisesti (offline-ensin) ja yritetään
 * sen jälkeen synkronoida Firebaseen.
 */
class NatureSpotRepository(
    private val dao: NatureSpotDao,
    private val firestoreManager: FirestoreManager,
    private val storageManager: StorageManager,
    private val authManager: AuthManager
) {
    /** Flow-virta kaikista luontolöydöistä aikajärjestyksessä (uusin ensin) */
    val allSpots: Flow<List<NatureSpot>> = dao.getAllSpots()

    /** Flow-virta löydöistä joilla on validi GPS-sijainti (kartalla näytettävät) */
    val spotsWithLocation: Flow<List<NatureSpot>> = dao.getSpotsWithLocation()

    /**
     * Tallentaa uuden luontolöydön paikalliseen tietokantaan ja yrittää Firebase-synkronointia.
     */
    suspend fun insertSpot(spot: NatureSpot) {
        val spotWithUser = spot.copy(userId = authManager.currentUserId)

        // 1. Tallenna paikallisesti HETI (toimii offline-tilassakin)
        dao.insert(spotWithUser.copy(synced = false))

        // 2. Yritä synkronoida Firebaseen
        syncSpotToFirebase(spotWithUser)
    }

    /**
     * Synkronoi yksittäinen kohde Firebaseen.
     */
    private suspend fun syncSpotToFirebase(spot: NatureSpot) {
        try {
            // 2a. "Lataa kuva" (StorageManager on tässä vaiheessa vielä stub)
            val firebaseImageUrl = spot.imageLocalPath?.let { localPath ->
                storageManager.uploadImage(localPath, spot.id).getOrNull()
            }

            // 2b. Tallenna metadata Firestoreen
            val spotWithUrl = spot.copy(imageFirebaseUrl = firebaseImageUrl)
            firestoreManager.saveSpot(spotWithUrl).getOrThrow()

            // 2c. Merkitse Room:ssa synkronoiduksi
            dao.markSynced(spot.id, firebaseImageUrl ?: "")
        } catch (e: Exception) {
            // Synkronointi epäonnistui – synced = false pysyy Room:ssa
        }
    }

    /** Poistaa luontolöydön paikallisesta tietokannasta. */
    suspend fun deleteSpot(spot: NatureSpot) {
        dao.delete(spot)
    }

    /** Hakee löydöt jotka eivät ole synkronoituja. */
    suspend fun getUnsyncedSpots(): List<NatureSpot> {
        return dao.getUnsyncedSpots()
    }

    /** Synkronoi kaikki odottavat kohteet. */
    suspend fun syncPendingSpots() {
        val unsyncedSpots = dao.getUnsyncedSpots()
        unsyncedSpots.forEach { spot ->
            syncSpotToFirebase(spot)
        }
    }
}
