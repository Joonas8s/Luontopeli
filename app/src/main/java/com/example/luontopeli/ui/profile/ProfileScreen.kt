package com.example.luontopeli.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.luontopeli.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val currentUser by viewModel.currentUser.collectAsState()
    val totalSpots by viewModel.totalSpots.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profiilikuvake
        Icon(
            Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(16.dp))

        if (currentUser != null) {
            Text(
                text = if (currentUser!!.isAnonymous) "Anonyymi käyttäjä"
                else currentUser!!.email ?: "Käyttäjä",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "ID: ${currentUser!!.uid.take(8)}...",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(24.dp))

            // Tilastot
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Tilastot", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem(value = "${totalSpots}", label = "Löytöä")
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            OutlinedButton(onClick = { viewModel.signOut() }) {
                Text("Kirjaudu ulos")
            }
        } else {
            Text("Et ole kirjautunut", style = MaterialTheme.typography.titleMedium)
            Button(onClick = { viewModel.signInAnonymously() }) {
                Text("Kirjaudu anonyymisti")
            }
        }
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
