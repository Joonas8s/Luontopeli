package com.example.luontopeli.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.luontopeli.data.local.AppDatabase
import com.example.luontopeli.data.remote.firebase.AuthManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel profiilinäkymälle.
 */
class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val authManager = AuthManager()
    private val db = AppDatabase.getDatabase(application)
    private val auth = FirebaseAuth.getInstance()

    private val _currentUser = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    private val _totalSpots = MutableStateFlow(0)
    val totalSpots: StateFlow<Int> = _totalSpots.asStateFlow()

    init {
        // Päivitä löytöjen määrä
        viewModelScope.launch {
            db.natureSpotDao().getAllSpots().collect { spots ->
                _totalSpots.value = spots.size
            }
        }

        // Seurataan kirjautumistilaa
        auth.addAuthStateListener { firebaseAuth ->
            _currentUser.value = firebaseAuth.currentUser
        }
    }

    fun signInAnonymously() {
        viewModelScope.launch {
            authManager.signInAnonymously()
        }
    }

    fun signOut() {
        authManager.signOut()
    }
}
