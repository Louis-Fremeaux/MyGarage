package tech.fremeaux.mygarage.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.layout.LazyLayout
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Garage
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SignalWifiConnectedNoInternet4
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tech.fremeaux.mygarage.AppDestinations
import tech.fremeaux.mygarage.data.api.ApiService
import tech.fremeaux.mygarage.data.model.Car
import tech.fremeaux.mygarage.data.model.Make
import tech.fremeaux.mygarage.data.model.Model
import tech.fremeaux.mygarage.data.model.parseModels
import tech.fremeaux.mygarage.data.repo.CarRepository
import tech.fremeaux.mygarage.data.repo.MakeRepository

enum class CataloguePage() {
    MAKES(), MODELS(), CAR(),
}

@Composable
fun CatalogScreen(){
    var loading by remember { mutableStateOf(true) }
    var currentCatalogue by rememberSaveable { mutableStateOf(CataloguePage.MAKES) }

    val makeRepo = MakeRepository(LocalContext.current)
    var makes by remember { mutableStateOf<List<Make>>(emptyList()) }
    var selectedMake: Make? by remember {mutableStateOf(null)}

    var model by remember { mutableStateOf<List<Model>>(emptyList()) }
    var selectedModel: Model? by remember {mutableStateOf(null)}
    var car by remember { mutableStateOf<String>("") }


    Column (Modifier.padding(20.dp,30.dp,20.dp,0.dp)){
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("CATALOGUE", color = MaterialTheme.colorScheme.onBackground ,style = MaterialTheme.typography.displayLarge)
            Button(onClick = {  }) {
                Text("🔎")
            }
        }

        when(currentCatalogue){
            CataloguePage.MAKES->{
                LaunchedEffect(Unit) {
                    makes = withContext(Dispatchers.IO) { makeRepo.getMakes() }
                    loading = false
                }

                Text(makes.size.toString()+" Models", color = MaterialTheme.colorScheme.tertiary)


                /*val repo = CarRepository(LocalContext.current)
                Button(onClick = { repo.addCar("BMW", "335i xdrive", 306, color.random().value.toLong()) }) {
                    Text("Ajouter")
                }*/

                if (makes.isEmpty() and loading) {
                    Box(Modifier.fillMaxSize(), Alignment.Center){
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Chargement...")
                            CircularProgressIndicator()
                        }
                    }
                }
                if(makes.isEmpty()){
                    Box(Modifier.fillMaxSize(), Alignment.Center){
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.SignalWifiConnectedNoInternet4, "", Modifier.size(80.dp))
                            Text("Pas de connexion....")
                        }
                    }
                }else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 128.dp),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(makes) { item ->
                            ElevatedCard(
                                onClick = {
                                    selectedMake = item
                                    currentCatalogue = CataloguePage.MODELS
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.elevatedCardColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = MaterialTheme.colorScheme.secondary
                                ),
                                elevation = CardDefaults.elevatedCardElevation(
                                    defaultElevation = 4.dp
                                )
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = item.name,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }
                    }
                }
            }


            CataloguePage.MODELS->{
                loading=true
                LaunchedEffect(Unit) {
                    model = withContext(Dispatchers.IO) { parseModels(ApiService().get("https://carapi.app/api/models/v2?make=${selectedMake?.name}")) }
                    loading = false
                }
                Button(onClick = { currentCatalogue = CataloguePage.MAKES; model=emptyList() }) {
                    Text("←")
                }
                Text(" Marque : ${selectedMake?.name}", color = MaterialTheme.colorScheme.tertiary)

                if (model.isEmpty() and loading) {
                    Box(Modifier.fillMaxSize(), Alignment.Center){
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Chargement...")
                            CircularProgressIndicator()
                        }
                    }
                }
                if(model.isEmpty()){
                    Box(Modifier.fillMaxSize(), Alignment.Center){
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.SignalWifiConnectedNoInternet4, "", Modifier.size(80.dp))
                            Text("Pas de connexion....")
                        }
                    }
                }else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 128.dp),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(model) { item ->
                            ElevatedCard(
                                onClick = {
                                    selectedModel = item
                                    currentCatalogue = CataloguePage.CAR
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.elevatedCardColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = MaterialTheme.colorScheme.secondary
                                ),
                                elevation = CardDefaults.elevatedCardElevation(
                                    defaultElevation = 4.dp
                                )
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = item.name,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }
                    }
                }
            }


            CataloguePage.CAR->{
                loading=true
                LaunchedEffect(Unit) {
                    car = withContext(Dispatchers.IO) { ApiService().get("https://carapi.app/api/engines/v2?make=${selectedMake?.name}&model=${selectedModel?.name}") }
                    loading = false
                }
                Button(onClick = { currentCatalogue = CataloguePage.MAKES; model=emptyList() }) {
                    Text("←")
                }
                Text(" Marque : ${selectedMake?.name}", color = MaterialTheme.colorScheme.tertiary)
                Text(" Model : ${selectedModel?.name}", color = MaterialTheme.colorScheme.tertiary)

                if (model.isEmpty() and loading) {
                    Box(Modifier.fillMaxSize(), Alignment.Center){
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Chargement...")
                            CircularProgressIndicator()
                        }
                    }
                }
                if(model.isEmpty()){
                    Box(Modifier.fillMaxSize(), Alignment.Center){
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.SignalWifiConnectedNoInternet4, "", Modifier.size(80.dp))
                            Text("Pas de connexion....")
                        }
                    }
                }else {
                    Text(car)
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 128.dp),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        /*items(car) { item ->
                            ElevatedCard(
                                onClick = {
                                    //selectedMake = item
                                    //currentCatalogue = CataloguePage.MODELS
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.elevatedCardColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = MaterialTheme.colorScheme.secondary
                                ),
                                elevation = CardDefaults.elevatedCardElevation(
                                    defaultElevation = 4.dp
                                )
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = item.name,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }*/
                    }
                }
            }
        }
    }
}