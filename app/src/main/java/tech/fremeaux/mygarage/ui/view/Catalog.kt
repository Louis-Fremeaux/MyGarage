package tech.fremeaux.mygarage.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import tech.fremeaux.mygarage.data.repo.CarRepository

@Composable
fun CatalogScreen(){
    val repo = CarRepository(LocalContext.current)

    Column (Modifier.padding(20.dp,30.dp)){
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("CATALOGUE", color = Color(0, 188, 212, 255))
            Button(onClick = {  }) {
                Text("🔎")
            }
        }
        Button(onClick = { repo.addCar("BMW", "335i xdrive") }) {
            Text("Ajouter")
        }

    }
}