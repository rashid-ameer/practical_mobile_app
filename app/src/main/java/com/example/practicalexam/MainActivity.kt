package com.example.practicalexam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.practicalexam.ui.theme.PracticalExamTheme
import kotlinx.coroutines.delay
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticalExamTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }
}
@Composable
fun App(){
    val navController = rememberNavController()
    var profileArgs = listOf(navArgument("name"){type = NavType.StringType})
    NavHost(navController = navController, startDestination = "home"){
        composable("splash"){ Splash(navController) }
        composable("home"){ HomeScreen(navController)}
        composable("profile/{name}", arguments= profileArgs){backStackEntry->
            val cityName = backStackEntry.arguments?.getString("name")
            if (cityName != null) {
                ProfileScreen(cityName = cityName)
            }
        }
    }
}

@Composable
fun Splash(navController: NavHostController){
    LaunchedEffect(true) {
        delay(3000L) // delay for 3 seconds
        navController.navigate("home")
    }


    Column(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.logo), contentDescription = null)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    var cityName by remember {
        mutableStateOf("")
    }

    Scaffold (topBar = { TopAppBar(title = {
        Text(text = "Select City")
    }) }){ it ->
        Column(modifier = Modifier
            .padding(it)
            .fillMaxWidth()) {

            Box(modifier = Modifier.fillMaxWidth()) {
                Image(painter = painterResource(id = R.drawable.logo), contentDescription = null, modifier = Modifier.align(Alignment.Center))
            }
            Image(painter = painterResource(id = R.drawable.map), contentDescription = null, modifier = Modifier
                .fillMaxWidth(1f)
                .aspectRatio(1.67f))

            Spacer(modifier = Modifier.height(30.dp))
            
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField( value = cityName, onValueChange = {
                    cityName = it
                })

                Button(onClick = { navController.navigate("profile/$cityName")}){
                    Text(text = "Show Weather")
                }
            }


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(cityName: String){
    val context = LocalContext.current
    val arrayId = context.resources.getIdentifier("weather_info_${cityName.toLowerCase(Locale.ROOT)}", "array", context.packageName)
    val weatherInfo = context.resources.getStringArray(arrayId)

    val weatherImage = when (weatherInfo[3].toLowerCase(Locale.ROOT)) {
        "Sunny" -> R.drawable.sunny
        "Cloudy" -> R.drawable.cloudy
        "hot" -> R.drawable.hot
        "rainy" -> R.drawable.rain
        "snow" -> R.drawable.snow // replace with your default image
        else -> {
            R.drawable.sunny
        }
    }


    Scaffold(topBar = { TopAppBar(title = { Text(text = "Weather Details") })}) {
        Column(modifier = Modifier
            .padding(it)
            .fillMaxHeight(), verticalArrangement = Arrangement.Center) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = "City Name: $cityName", fontSize = 30.sp)
                Spacer(modifier = Modifier.height(20.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    Icon(painter = painterResource(id= R.drawable.thermostat), contentDescription = null)
                    Column {
                        Text(text = "Temperature")
                        Text(text = weatherInfo[1], fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    Icon(painter = painterResource(id= R.drawable.wind), contentDescription = null)
                    Column {
                        Text(text = "Humidity")
                        Text(text = weatherInfo[2], fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    Image(painter = painterResource(id = weatherImage), contentDescription = null, modifier = Modifier.size(30.dp))
                    Column {
                        Text(text = "Condition")
                        Text(text = weatherInfo[3], fontSize = 12.sp)
                    }
                }
            }
        }
    }

}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PracticalExamTheme {
    App()
    }
}