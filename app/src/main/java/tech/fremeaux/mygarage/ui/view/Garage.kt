package tech.fremeaux.mygarage.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import tech.fremeaux.mygarage.data.repo.CarRepository

@Composable
fun GarageScreen(){
    val repo = CarRepository(LocalContext.current)
    var cars by remember { mutableStateOf(repo.getCars()) }


    Column (Modifier.padding(20.dp,30.dp)){
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                    append("MON ")
                }
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append("GARAGE 🏎️")
                }
            },
            style = MaterialTheme.typography.displayLarge
        )
        Text(cars.size.toString()+" véhicule(s)",color = MaterialTheme.colorScheme.tertiary)

        if (cars.isEmpty()){
            Box(Modifier.fillMaxSize(), Alignment.Center){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.DirectionsCar, "d", Modifier.size(80.dp))
                    Text("Nothing here....")
                }
            }
        }
        else{
            LazyColumn() {
                items(cars) { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                            .background(
                                color = Color.LightGray,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween)
                        {
                            Column() {
                                Text(text = item.make)
                                Text(text = item.model)
                            }

                            Button(onClick = {
                                repo.deleteCar(item.id)
                                cars = repo.getCars()
                            }) { Text("🗑️") }
                        }
                    }
                }
                item {
                    Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.Center) {
                        Text("← Swipe pour supprimer", color = MaterialTheme.colorScheme.tertiary)
                    }
                }
            }
        }
    }
}