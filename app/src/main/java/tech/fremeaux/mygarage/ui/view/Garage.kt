package tech.fremeaux.mygarage.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import tech.fremeaux.mygarage.data.repo.CarRepository

@Composable
fun GarageScreen(){
    val repo = CarRepository(LocalContext.current)
    val cars = repo.getCars()


    Column (Modifier.padding(20.dp,30.dp)){
        Text("Mon Garage 🏎️", color = MaterialTheme.colorScheme.primary)
        Text(cars.size.toString()+" véhicule(s)",color = Color.LightGray)

        if (cars.isEmpty()){
            Box(Modifier.fillMaxSize()){
                Icon(Icons.Default.DirectionsCar, "d")
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

                            /*Button(onClick = {
                                repo.deleteCar(item.id)
                                cars = repo.getCars()
                            }) { Text("🗑️") }*/
                        }
                    }
                }
            }
        }
    }
}