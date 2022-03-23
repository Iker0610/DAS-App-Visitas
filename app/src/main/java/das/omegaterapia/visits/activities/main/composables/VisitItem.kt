package das.omegaterapia.visits.activities.main.composables

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import das.omegaterapia.visits.data.visitList
import das.omegaterapia.visits.model.entities.VisitCard
import das.omegaterapia.visits.ui.components.form.TextIconButton
import das.omegaterapia.visits.ui.components.generic.CenteredColumn
import das.omegaterapia.visits.ui.components.generic.CenteredRow
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
    onLongClick: (VisitCard) -> Unit = {},
    elevation: Dp = 4.dp,
) {
    // Styling
    val typography = MaterialTheme.typography
    val mainText = applyTextStyle(typography.subtitle1, ContentAlpha.high) {
        Text(text = visitCard.client.toString(), overflow = TextOverflow.Ellipsis)
    }
    val directionText = applyTextStyle(MaterialTheme.typography.overline, ContentAlpha.high) {
        Column {
            Text(visitCard.client.direction.address.uppercase(), overflow = TextOverflow.Clip)
            Text("${visitCard.client.direction.town} - ${visitCard.client.direction.zip}".uppercase(),
                overflow = TextOverflow.Clip)
        }
    }

    // Date Formatter
    val date = visitCard.visitDate.format(DateTimeFormatter.ofPattern("d MMM")).trim('.').uppercase()
    val time = visitCard.visitDate.format(DateTimeFormatter.ofPattern("HH:mm"))


    // Collapse Logic
    val canBeExpanded = visitCard.companions.isNotEmpty() || visitCard.observations.isNotBlank()
    val showExpandedContent = isExpanded && (canBeExpanded)
    val expandCollapseIcon = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore


    // UI
    Card(modifier = modifier.fillMaxWidth(), elevation = elevation) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick(visitCard) }
                    .padding(16.dp)
            ) {
                CenteredColumn {
                    if (visitCard.isVIP) {
                        Icon(Icons.Filled.StarOutline, "VIP Client", Modifier.height(16.dp), tint = MaterialTheme.colors.secondary)
                        Spacer(modifier = Modifier.height(3.dp))
                    }
                    Text(text = date, style = typography.overline)
                    if (!visitCard.isVIP) Spacer(modifier = Modifier.height(6.dp))
                    Text(text = time, style = typography.subtitle2)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(Modifier.weight(1f)) {
                    directionText()
                    Spacer(modifier = Modifier.height(4.dp))
                    mainText()
                }

                if (canBeExpanded) {
                    Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier
                        .requiredWidth(IntrinsicSize.Max)
                        .padding(start = 16.dp)) {
                        Icon(expandCollapseIcon, contentDescription = "Show more", Modifier.size(24.dp))
                    }
                }
            }


            // Divider(modifier = Modifier.padding(horizontal = dividerPadding))

            //-------------------------------

            AnimatedVisibility(showExpandedContent) {
                Column(Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                    if (visitCard.companions.isNotEmpty()) {
                        Text(
                            "Client's Companions",
                            style = typography.subtitle2,
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        for (client in visitCard.companions) {
                            Text(text = "- $client", style = typography.body2)
                        }

                        if (visitCard.observations.isNotBlank()) Spacer(Modifier.height(16.dp))
                    }
                    if (visitCard.observations.isNotBlank()) {
                        Text(
                            "Observations",
                            style = typography.subtitle2,
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(visitCard.observations, style = typography.body2)
                    }
                }
            }

            //-------------------------------

            Divider()

            Row(Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceAround) {
                val context = LocalContext.current
                val callIntent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", visitCard.client.phoneNum, null))
                val mapsIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + Uri.encode(visitCard.client.direction.toString())))


                TextIconButton(
                    icon = Icons.Filled.PhoneInTalk,
                    text = visitCard.client.phoneNum,
                    contentPadding = PaddingValues(vertical = 0.dp, horizontal = 16.dp),
                    onClick = { context.startActivity(callIntent) })

                TextIconButton(
                    icon = Icons.Filled.PinDrop,
                    text = "Open in Maps",
                    contentPadding = PaddingValues(vertical = 0.dp, horizontal = 16.dp),
                    onClick = { context.startActivity(mapsIntent) })
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
            VisitCardItem(modifier = Modifier.padding(8.dp), visitCard = visitList[7], isExpanded = true)
        }
    }
}