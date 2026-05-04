package tech.fremeaux.mygarage.ui.view

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NoTransfer
import androidx.compose.material.icons.filled.SignalWifiConnectedNoInternet4
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tech.fremeaux.mygarage.data.api.ApiService
import tech.fremeaux.mygarage.data.model.Car
import tech.fremeaux.mygarage.data.model.Make
import tech.fremeaux.mygarage.data.model.Model
import tech.fremeaux.mygarage.data.repo.CarRepository
import tech.fremeaux.mygarage.data.repo.MakeRepository
import tech.fremeaux.mygarage.data.repo.ModelRepository
import tech.fremeaux.mygarage.ui.theme.Blue
import tech.fremeaux.mygarage.ui.theme.Gold
import tech.fremeaux.mygarage.ui.theme.Green
import tech.fremeaux.mygarage.ui.theme.Purple

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

    val modelRepo = ModelRepository(LocalContext.current)
    var model by remember { mutableStateOf<List<Model>>(emptyList()) }
    var selectedModel: Model? by remember {mutableStateOf(null)}

    val carRepo = CarRepository(LocalContext.current)
    var car by remember { mutableStateOf<Car?>(null) }


    Column (Modifier.padding(20.dp,30.dp,20.dp,0.dp)){
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("CATALOGUE", color = MaterialTheme.colorScheme.onBackground ,style = MaterialTheme.typography.displayLarge)
        }

        when(currentCatalogue){
            CataloguePage.MAKES->{
                LaunchedEffect(Unit) {
                    makes = withContext(Dispatchers.IO) { makeRepo.getMakes() }
                    loading = false
                }

                Text(makes.size.toString()+" Marques ·  via CarApi", color = MaterialTheme.colorScheme.tertiary)

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
                                    AsyncImage(
                                        model = "https://github.com/filippofilip95/car-logos-dataset/blob/master/logos/optimized/${item.name.lowercase().replace(" ","-")}.png?raw=true",
                                        contentDescription = item.name
                                    )
                                }
                            }
                        }
                    }
                }
            }


            CataloguePage.MODELS->{
                LaunchedEffect(Unit) {
                    loading = true
                    model = withContext(Dispatchers.IO) { modelRepo.getModel(selectedMake) }
                    loading = false
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        modifier = Modifier,
                        onClick = { currentCatalogue = CataloguePage.MAKES; model=emptyList() },
                        colors = ButtonDefaults.elevatedButtonColors(containerColor = MaterialTheme.colorScheme.background, contentColor = MaterialTheme.colorScheme.secondary),
                        shape = RoundedCornerShape(16.dp)
                    )
                    { Text("← Retour", color = MaterialTheme.colorScheme.secondary) }

                    Text(" Marque : ${selectedMake?.name}", color = MaterialTheme.colorScheme.tertiary)
                }

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
                            Text("Pas de model....")
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
                LaunchedEffect(Unit) {
                    loading = true
                    car = withContext(Dispatchers.IO) { carRepo.parseCar(ApiService().get("https://carapi.app/api/engines/v2?limit=1&make=${selectedMake?.name}&model=${selectedModel?.name}")) }
                    loading = false
                }
                Button(
                    onClick = { currentCatalogue = CataloguePage.MODELS; /*car=null*/ },
                    colors = ButtonColors(Color(0,0,0,0),Color.Black,Color.Black,Color.Black,)
                ) {
                    Text("← Retour", color = MaterialTheme.colorScheme.secondary)
                }

                if (loading) {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Chargement...")
                            CircularProgressIndicator()
                        }
                    }
                }else if(car==null){
                    Box(Modifier.fillMaxSize(), Alignment.Center){
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.NoTransfer, "", Modifier.size(80.dp))
                            Text("Pas de details du model")
                        }
                    }
                }else {
                    val repo = CarRepository(LocalContext.current)
                    Column() {
                        Column(Modifier.padding(vertical = 15.dp)) {
                            Text(car!!.make+" · "+car!!.year, color = MaterialTheme.colorScheme.tertiary, style = MaterialTheme.typography.titleLarge)
                            Text(car!!.model, color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight(800))
                            Text(car!!.transmission+" · "+car!!.drive /*+" · "+car!!.fuel*/, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.tertiary)
                        }
                        Row(Modifier.fillMaxWidth().padding(vertical = 5.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                           Card(
                                modifier = Modifier,
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
                                   modifier = Modifier.padding(10.dp),
                                   contentAlignment = Alignment.Center
                               ) {
                                   Column() {
                                       Text("PUISSANCE", color = MaterialTheme.colorScheme.tertiary)
                                       Text(car!!.hp.toString()+" ch", color = Gold, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight(800))
                                       LinearProgressIndicator(
                                       progress = { car!!.hp/1000.toFloat() },
                                       modifier = Modifier.width(155.dp),
                                       color = Gold,
                                       trackColor = MaterialTheme.colorScheme.background,
                                       )
                                   }
                               }
                           }
                            Card(
                                modifier = Modifier,
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
                                    modifier = Modifier.padding(10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column() {
                                        Text("COUPLE", color = MaterialTheme.colorScheme.tertiary)
                                        Text(car!!.nm.toString()+" nm", color = Purple, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight(800))
                                        LinearProgressIndicator(
                                            progress = { car!!.nm/1000.toFloat() },
                                            modifier = Modifier.width(155.dp),
                                            color = Purple,
                                            trackColor = MaterialTheme.colorScheme.background,
                                        )
                                    }
                                }
                            }
                        }
                        Row(Modifier.fillMaxWidth().padding(vertical = 5.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Card(
                                modifier = Modifier,
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
                                    modifier = Modifier.padding(10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column() {
                                        Text("0 -> 100", color = MaterialTheme.colorScheme.tertiary)
                                        Text(car!!.hp.toString()+" s", color = Blue, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight(800))
                                        LinearProgressIndicator(
                                            progress = { car!!.hp/1000.toFloat() },
                                            modifier = Modifier.width(155.dp),
                                            color = Blue,
                                            trackColor = MaterialTheme.colorScheme.background,
                                        )
                                    }
                                }
                            }
                            Card(
                                modifier = Modifier,
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
                                    modifier = Modifier.padding(10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column() {
                                        Text("VIT. MAX", color = MaterialTheme.colorScheme.tertiary)
                                        Text(car!!.hp.toString()+" km/h", color = Green, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight(800))
                                        LinearProgressIndicator(
                                            progress = { car!!.hp/1000.toFloat() },
                                            modifier = Modifier.width(155.dp),
                                            color = Green,
                                            trackColor = MaterialTheme.colorScheme.background,
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Row(Modifier.fillMaxWidth().padding(vertical = 15.dp), Arrangement.Center) {
                        Button(onClick = { repo.addCar(car!!.make,car!!.model,car!!.hp,car!!.nm,color.random().value.toLong(),car!!.year,car!!.fuel,car!!.drive,car!!.transmission) },
                            shape = RoundedCornerShape(14.dp),
                            contentPadding = PaddingValues(10.dp),
                            modifier = Modifier.shadow(10.dp, shape = RoundedCornerShape(10.dp), ambientColor = MaterialTheme.colorScheme.primary, spotColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("+ Ajouter au Garage", color = MaterialTheme.colorScheme.surface ,style = MaterialTheme.typography.headlineMedium)

                        }
                    }
                }
            }
        }
    }
}