package das.omegaterapia.visits.activities.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import das.omegaterapia.visits.data.visitList
import das.omegaterapia.visits.activities.main.composables.components.VisitList
import das.omegaterapia.visits.ui.theme.OmegaterapiaTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val username = intent.getStringExtra("LOGED_USERNAME") ?: "NOT USERNAME GIVEN"

        setContent {
            OmegaterapiaTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    val groupedVisits = mapOf(
                        "Lunes" to visitList.subList(0, 5),
                        "Martes" to visitList.subList(5, 12),
                        "Miércoles" to emptyList(),
                        "Jueves" to visitList.subList(12, 20),
                        "Viernes" to visitList.subList(20, visitList.size),
                    )
                    OmegaterapiaTheme {
                        Surface {
                            VisitList(groupedVisitCards = groupedVisits)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OmegaterapiaTheme {
        Greeting("Android")
    }
}