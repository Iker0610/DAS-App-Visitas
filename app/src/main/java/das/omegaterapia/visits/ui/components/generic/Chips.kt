package das.omegaterapia.visits.ui.components.generic

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.dp

private const val SurfaceOverlayOpacity = 0.12f

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun selectedChoiceChipColors(
    backgroundColor: Color = MaterialTheme.colors.secondary.copy(alpha = 0.25f).compositeOver(MaterialTheme.colors.surface),
    contentColor: Color = MaterialTheme.colors.secondary,
    leadingIconContentColor: Color = contentColor.copy(alpha = 0.75f),

    disabledBackgroundColor: Color =
        MaterialTheme.colors.onSurface
            .copy(alpha = ContentAlpha.disabled * SurfaceOverlayOpacity)
            .compositeOver(MaterialTheme.colors.surface),
    disabledContentColor: Color = contentColor.copy(alpha = ContentAlpha.disabled * ChipDefaults.ContentOpacity),
    disabledLeadingIconContentColor: Color = leadingIconContentColor.copy(alpha = ContentAlpha.disabled * ChipDefaults.LeadingIconOpacity),
): ChipColors = ChipDefaults.chipColors(
    backgroundColor = backgroundColor,
    contentColor = contentColor,
    leadingIconContentColor = leadingIconContentColor,
    disabledBackgroundColor = disabledBackgroundColor,
    disabledContentColor = disabledContentColor,
    disabledLeadingIconContentColor = disabledLeadingIconContentColor
)


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun selectedOutlinedChoiceChipColors(
    backgroundColor: Color = MaterialTheme.colors.secondary.copy(alpha = SurfaceOverlayOpacity).compositeOver(MaterialTheme.colors.surface),
    contentColor: Color = MaterialTheme.colors.secondary,
    leadingIconContentColor: Color = contentColor.copy(alpha = 0.85f),

    disabledBackgroundColor: Color =
        MaterialTheme.colors.onSurface
            .copy(alpha = ContentAlpha.disabled * SurfaceOverlayOpacity)
            .compositeOver(MaterialTheme.colors.surface),
    disabledContentColor: Color = contentColor.copy(alpha = ContentAlpha.disabled * ChipDefaults.ContentOpacity),
    disabledLeadingIconContentColor: Color = leadingIconContentColor.copy(alpha = ContentAlpha.disabled * ChipDefaults.LeadingIconOpacity),
): ChipColors = ChipDefaults.chipColors(
    backgroundColor = backgroundColor,
    contentColor = contentColor,
    leadingIconContentColor = leadingIconContentColor,
    disabledBackgroundColor = disabledBackgroundColor,
    disabledContentColor = disabledContentColor,
    disabledLeadingIconContentColor = disabledLeadingIconContentColor
)


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun unselectedOutlinedChoiceChipColors(
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = MaterialTheme.colors.onSurface,
    leadingIconContentColor: Color = contentColor.copy(alpha = ChipDefaults.LeadingIconOpacity),

    disabledBackgroundColor: Color =
        MaterialTheme.colors.onSurface
            .copy(alpha = ContentAlpha.disabled * SurfaceOverlayOpacity)
            .compositeOver(MaterialTheme.colors.surface),
    disabledContentColor: Color = contentColor.copy(alpha = ContentAlpha.disabled * ChipDefaults.ContentOpacity),
    disabledLeadingIconContentColor: Color = leadingIconContentColor.copy(alpha = ContentAlpha.disabled * ChipDefaults.LeadingIconOpacity),
): ChipColors = ChipDefaults.chipColors(
    backgroundColor = backgroundColor,
    contentColor = contentColor,
    leadingIconContentColor = leadingIconContentColor,
    disabledBackgroundColor = disabledBackgroundColor,
    disabledContentColor = disabledContentColor,
    disabledLeadingIconContentColor = disabledLeadingIconContentColor
)


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChoiceChip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selected: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = RoundedCornerShape(50),
    colors: ChipColors = if (selected) selectedChoiceChipColors() else ChipDefaults.chipColors(),
    leadingIcon: @Composable (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    Chip(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        shape = shape,
        border = null,
        colors = colors,
        leadingIcon = leadingIcon,
        content = content
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OutlinedChoiceChip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = CutCornerShape(30),
    colors: ChipColors = if (selected) selectedOutlinedChoiceChipColors() else unselectedOutlinedChoiceChipColors(),
    leadingIcon: @Composable (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    Chip(
        onClick = onClick,
        modifier = modifier,
        enabled = true,
        interactionSource = interactionSource,
        shape = shape,
        border = BorderStroke(1.dp, color = colors.leadingIconContentColor(enabled = true).value),
        colors = colors,
        leadingIcon = leadingIcon,
        content = content
    )
}