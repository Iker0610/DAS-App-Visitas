package das.omegaterapia.visits.ui.components.visits

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import das.omegaterapia.visits.data.visitList
import das.omegaterapia.visits.model.entities.VisitCard
import das.omegaterapia.visits.ui.components.generic.CenteredColumn
import das.omegaterapia.visits.ui.components.generic.CenteredRow
import das.omegaterapia.visits.ui.components.form.TextIconButton
import das.omegaterapia.visits.ui.theme.OmegaterapiaTheme
import java.time.format.DateTimeFormatter
import kotlin.random.Random


private fun applyTextStyle(
    textStyle: TextStyle,
    contentAlpha: Float,
    icon: @Composable (() -> Unit),
): @Composable (() -> Unit) {
    return {
        CompositionLocalProvider(LocalContentAlpha provides contentAlpha) {
            ProvideTextStyle(textStyle, icon)
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VisitCardItem(
    modifier: Modifier = Modifier,
    visitCard: VisitCard,
    isExpanded: Boolean = false,
    onClick: (VisitCard) -> Unit = {},
    elevation: Dp = 4.dp,
) {
    val expandCollapseIcon = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore

    // Styling
    val typography = MaterialTheme.typography
    val mainText = applyTextStyle(typography.subtitle1, ContentAlpha.high) {
        Text(text = visitCard.mainClient.toString(), overflow = TextOverflow.Ellipsis)
    }
    val directionText = applyTextStyle(MaterialTheme.typography.overline, ContentAlpha.high) {
        Column {
            Text(visitCard.mainClient.direction.address.uppercase(), overflow = TextOverflow.Clip)
            Text("${visitCard.mainClient.direction.town} - ${visitCard.mainClient.direction.zip}".uppercase(),
                overflow = TextOverflow.Clip)
        }
    }

    // Date Formatter
    val date = visitCard.visitData.visitDate.format( DateTimeFormatter.ofPattern("d MMM")).trim('.').uppercase()
    val time = visitCard.visitData.visitDate.format( DateTimeFormatter.ofPattern("hh:mm"))

    // UI
    Card(modifier = modifier.fillMaxWidth(), elevation = elevation) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                // This `Column` animates its size when its content changes.
                .animateContentSize()
        ) {
            CenteredRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick(visitCard) }
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CenteredRow(horizontalArrangement = Arrangement.Start) {
                    CenteredColumn {
                        if (visitCard.visitData.isVIP) {
                            Icon(Icons.Filled.StarOutline, "VIP Client", Modifier.height(16.dp))
                            Spacer(modifier = Modifier.height(3.dp))
                        }
                        Text(text = date, style = typography.overline)
                        if (!visitCard.visitData.isVIP) Spacer(modifier = Modifier.height(6.dp))
                        Text(text = time, style = typography.subtitle2)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(Modifier.wrapContentHeight()) {
                        directionText()
                        Spacer(modifier = Modifier.height(4.dp))
                        mainText()
                    }
                }


                Icon(
                    expandCollapseIcon,
                    contentDescription = "Show more",
                    Modifier
                        .requiredSize(24.dp)
                        .defaultMinSize(24.dp)
                )
            }

            //-------------------------------

            if (isExpanded && (visitCard.visitData.companions.isNotEmpty() || visitCard.visitData.observations.isNotBlank())) {
                Divider()
                Column(Modifier.padding(vertical = 16.dp)) {
                    if (visitCard.visitData.companions.isNotEmpty()) {
                        Text("Client's Companions", style = typography.subtitle2, modifier = Modifier.padding(bottom = 4.dp))
                        for (client in visitCard.visitData.companions) {
                            Text(text = "- $client", style = typography.body2)
                        }

                        if (visitCard.visitData.observations.isNotBlank()) Spacer(Modifier.height(16.dp))
                    }
                    if (visitCard.visitData.observations.isNotBlank()) {
                        Text("Observations", style = typography.subtitle2, modifier = Modifier.padding(bottom = 4.dp))
                        Text(visitCard.visitData.observations, style = typography.body2)
                    }
                }
            }

            //-------------------------------

            Divider()

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                TextIconButton(
                    icon = Icons.Filled.PhoneInTalk,
                    text = visitCard.mainClient.phoneNum,
                    contentPadding = PaddingValues(vertical = 0.dp, horizontal = 16.dp),
                    onClick = { /*TODO*/ })

                TextIconButton(
                    icon = Icons.Filled.PinDrop,
                    text = "Open in Maps",
                    contentPadding = PaddingValues(vertical = 0.dp, horizontal = 16.dp),
                    onClick = { /*TODO*/ })
            }
        }
    }
}


//-----------------------------------------------------------------------------------------------------------------------

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Preview(showBackground = true)
@Composable
fun VisitCardItemPreview() {
    OmegaterapiaTheme {
        val (expanded, setExpanded) = remember { mutableStateOf(false) }
        Surface(color = Color.Black) {
            VisitCardItem(
                modifier = Modifier.padding(8.dp),
                visitCard = visitList[Random.nextInt(visitList.size)],
                isExpanded = expanded,
                onClick = { setExpanded(!expanded) })
        }
    }
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Preview(showBackground = true)
@Composable
fun VisitCardItemExpandedPreview() {
    OmegaterapiaTheme {
        Surface(color = Color.Black) {
            VisitCardItem(modifier = Modifier.padding(8.dp), visitCard = visitList[0], isExpanded = true)
        }
    }
}