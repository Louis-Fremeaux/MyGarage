package tech.fremeaux.mygarage.ui.view

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.TextButton
import tech.fremeaux.mygarage.data.clearCache
import tech.fremeaux.mygarage.data.clearCar
import tech.fremeaux.mygarage.data.getDatabaseSize

@Composable
fun SettingsScreen() {

    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var showDialogCar by remember { mutableStateOf(false) }
    var dbSize by remember { mutableStateOf(getDatabaseSize(context)) }

    Column (Modifier.padding(20.dp,30.dp,20.dp,0.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                "SETTINGS",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.displayLarge
            )
        }
        Row(Modifier.fillMaxWidth().padding(vertical = 40.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Stockage local: $dbSize",
                color = MaterialTheme.colorScheme.onBackground,
            )
            Button(
                onClick = { showDialog = true },
                modifier = Modifier
            ) {
                Text("Clear cache")
            }
        }
        Row(Modifier.fillMaxWidth().padding(vertical = 40.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(
            "Voiture du garage",
            color = MaterialTheme.colorScheme.onBackground,
        )
        Button(
            onClick = { showDialogCar = true },
            modifier = Modifier
        ) {
            Text("Clear garage")
        }
    }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Confirmer la suppression") },
                text = { Text("Voulez-vous vraiment vider le cache des marques / model ? Cette action est irréversible.") },
                confirmButton = {
                    TextButton(onClick = {
                        clearCache(context)
                        dbSize = getDatabaseSize(context)
                        showDialog = false
                    }) { Text("Confirmer", color = Color.Red) }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) { Text("Annuler") }
                }
            )
        }
        if (showDialogCar) {
            AlertDialog(
                onDismissRequest = { showDialogCar = false },
                title = { Text("Confirmer la suppression") },
                text = { Text("Voulez-vous vraiment vider votre garage? Cette action est irréversible.") },
                confirmButton = {
                    TextButton(onClick = {
                        clearCar(context)
                        dbSize = getDatabaseSize(context)
                        showDialogCar = false
                    }) { Text("Confirmer", color = Color.Red) }
                },
                dismissButton = {
                    TextButton(onClick = { showDialogCar = false }) { Text("Annuler") }
                }
            )
        }
    }

}