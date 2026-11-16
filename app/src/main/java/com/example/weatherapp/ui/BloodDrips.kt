package com.example.weatherapp.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp

// Decoration: blood dripping down from the bottom edge of the top bar.
// Drawn as ONE smooth outline. Each drip spreads out where it leaves the bar, narrows to a thin neck,
// and ends in a round drop - all joined with curves, so there are no corners.
@Composable
fun BloodDrips(modifier: Modifier = Modifier) {
    val color = MaterialTheme.colorScheme.primaryContainer // same colour as the top bar

    Canvas(modifier.fillMaxWidth().height(30.dp)) {
        // Each drip: where it is across the screen (0..1), how long (0..1), how wide its neck is (dp)
        val drips = listOf(
            Triple(0.05f, 0.45f, 6), Triple(0.14f, 0.95f, 9), Triple(0.25f, 0.35f, 5),
            Triple(0.37f, 0.70f, 8), Triple(0.49f, 0.42f, 6), Triple(0.60f, 0.58f, 7),
            Triple(0.72f, 1.00f, 10), Triple(0.83f, 0.38f, 5), Triple(0.93f, 0.75f, 7)
        )

        val outline = Path()
        outline.moveTo(0f, 0f)
        drips.forEach { (x, length, neckDp) ->
            val centre = x * size.width
            val neck = neckDp.dp.toPx() / 2                // half the width of the thin part
            val drop = neck * 1.35f                        // radius of the round drop at the end
            val dropY = length * size.height - drop        // height of the drop's centre
            val neckEnd = maxOf(dropY - drop * 1.4f, 0f)   // where the neck starts widening into the drop
            val spread = neck * 2.5f                       // how far the drip spreads along the bar
            val spreadEnd = minOf(neckEnd, neck * 4)       // how far down that spread reaches

            // Left side, going down: bar -> neck -> widen into the drop
            outline.lineTo(centre - neck - spread, 0f)
            outline.cubicTo(centre - neck - spread * 0.4f, 0f, centre - neck, spreadEnd * 0.4f, centre - neck, spreadEnd)
            outline.lineTo(centre - neck, neckEnd)
            outline.cubicTo(centre - neck, neckEnd + drop * 0.6f, centre - drop, dropY - drop * 0.7f, centre - drop, dropY)
            // Round the bottom of the drop (a half circle)
            outline.cubicTo(centre - drop, dropY + drop * 4 / 3, centre + drop, dropY + drop * 4 / 3, centre + drop, dropY)
            // Right side, going back up: the same curves mirrored
            outline.cubicTo(centre + drop, dropY - drop * 0.7f, centre + neck, neckEnd + drop * 0.6f, centre + neck, neckEnd)
            outline.lineTo(centre + neck, spreadEnd)
            outline.cubicTo(centre + neck, spreadEnd * 0.4f, centre + neck + spread * 0.4f, 0f, centre + neck + spread, 0f)
        }
        outline.lineTo(size.width, 0f)
        outline.close()

        drawPath(outline, color)
    }
}
