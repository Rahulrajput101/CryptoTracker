package com.plcoding.cryptotracker.crpto.presentation.coin_detail.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import com.plcoding.cryptotracker.crpto.domain.CoinPrice
import com.plcoding.cryptotracker.crpto.presentation.coin_detail.ChartStyle
import com.plcoding.cryptotracker.crpto.presentation.coin_detail.DataPoint
import com.plcoding.cryptotracker.crpto.presentation.coin_detail.ValueLabel
import com.plcoding.cryptotracker.ui.theme.CryptoTrackerTheme
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt
import kotlin.random.Random


@Composable
fun LineChart(
    dataPoints: List<DataPoint>,
    style: ChartStyle,
    visibleDataPointsIndices: IntRange,
    unit: String,
    modifier: Modifier = Modifier,
    selectedDataPoint: DataPoint? = null,
    onSelectedDataPoint: (DataPoint) -> Unit = {},
    onXLabelWidthChange: (Float) -> Unit = {},
    showHelperLines: Boolean = true
) {

    val textStyle = LocalTextStyle.current.copy(
        fontSize = style.labelFontSize
    )

    val visibleDataPoints = remember(dataPoints,visibleDataPointsIndices) {
        dataPoints.slice(visibleDataPointsIndices)
    }

    val maxYValue = remember (visibleDataPoints){
        visibleDataPoints.maxOfOrNull { it.y } ?: 0f
    }

    val minYValue = remember (visibleDataPoints){
        visibleDataPoints.minOfOrNull { it.y } ?: 0f
    }

    val measurer = rememberTextMeasurer()

    var xLabelWidth by remember {
        mutableFloatStateOf(0f)
    }

    LaunchedEffect(xLabelWidth) {
        onXLabelWidthChange(xLabelWidth)
    }

    val selectedDataPointIndex = remember(selectedDataPoint) {
        dataPoints.indexOf(selectedDataPoint)
    }

    var drawPoints by remember {
        mutableStateOf(listOf<DataPoint>())
    }
    var isShowingDataPoint by remember {
        mutableStateOf(selectedDataPoint != null)
    }


    Canvas(
        modifier = modifier
            .fillMaxSize()
    ) {

        val minLabelSpacingYpx = style.minYLabelSpacing.toPx()
        val verticalPaddingPx = style.verticalPadding.toPx()
        val horizontalPaddingPx = style.horizontalPadding.toPx()
        val xAxisLabelSpacingPx = style.xAxisLabelSpacing.toPx()

        val xLabelTextLayoutResults = visibleDataPoints.map {
            measurer.measure(
                text = it.xLabel,
                style = textStyle.copy(textAlign = TextAlign.Center)
            )
        }

        val maxXLabelWidth = xLabelTextLayoutResults.maxOfOrNull { it.size.width } ?: 0
        val maxXLabelHeight = xLabelTextLayoutResults.maxOfOrNull { it.size.height } ?: 0
        val maxXLabelLineCount = xLabelTextLayoutResults.maxOfOrNull { it.lineCount } ?: 0
        val xLabelLineHeight = maxXLabelHeight/ maxXLabelLineCount

        val viewPortHeightPx = size.height -
                (maxXLabelHeight +2+verticalPaddingPx
                        +xLabelLineHeight+xAxisLabelSpacingPx)
        // Y-Label Calculation /-
        val labelViewPortHeightPx = viewPortHeightPx + xLabelLineHeight
        val labelCountExcludingLastLabel = ((labelViewPortHeightPx / (xLabelLineHeight+minLabelSpacingYpx))).toInt()

        // (2659 -2583)/2 = 32 steps
        val valueIncrement = (maxYValue - minYValue) / labelCountExcludingLastLabel

        val yLabels = (0..labelCountExcludingLastLabel).map {
            ValueLabel(
                value = maxYValue - (valueIncrement * it),
                unit = unit
            )
        }
        val yLabelTextLayoutResults = yLabels.map {
            measurer.measure(
                text = it.formatted(),
                style = textStyle
            )
        }
        val maxYLabelWidth = yLabelTextLayoutResults.maxOfOrNull{it.size.width} ?: 0


        // Y-Label Calculation -/

        val viewPortTopY = verticalPaddingPx +xLabelLineHeight+10f
        val viewPortRightX = size.width
        val viewPortBottomY = viewPortTopY+viewPortHeightPx

        val viewPortLeftX = 2f* horizontalPaddingPx+maxYLabelWidth

        /**
         * The Rect object calculates its size based on the differences
         * between the left and right values for the width,
         * and the top and bottom values for the height.
         * val width = right - left
         * val height = bottom - top
         */
        val viewPort = Rect(
            left = viewPortLeftX,
            top = viewPortTopY,
            right = viewPortRightX,
            bottom = viewPortBottomY
        )

        drawRect(
            color = Color.Green.copy(alpha = 0.3f),
            topLeft = viewPort.topLeft,
            size = viewPort.size
        )


        xLabelWidth = maxXLabelWidth+ xAxisLabelSpacingPx


        xLabelTextLayoutResults.forEachIndexed { index, result ->

            val x = viewPortLeftX+xAxisLabelSpacingPx/2f +
                    xLabelWidth*index
            drawText(
                textLayoutResult = result,
                topLeft = Offset(
                    x = x,
                    y = viewPortBottomY+xAxisLabelSpacingPx
                ),
                color = if(index == selectedDataPointIndex) style.selectedColor else style.unselectedColor
            )

            if(showHelperLines){
                drawLine(
                    color = if(index == selectedDataPointIndex) style.selectedColor else style.unselectedColor,
                    start = Offset(
                        x = x+result.size.width/2,
                        y = viewPortBottomY
                    ),
                    end = Offset(
                        x = x+result.size.width/2,
                        y = viewPortTopY
                    ),
                    strokeWidth = if (selectedDataPointIndex == index){
                        style.helperLinesThicknessPx*1.8f
                    }else{
                        style.helperLinesThicknessPx
                    }
                )
            }

            if (selectedDataPointIndex == index){
                val valueLabel = ValueLabel(
                    value = visibleDataPoints[index].y,
                    unit = unit
                )

                val valueResult = measurer.measure(
                    text = valueLabel.formatted(),
                    style = textStyle.copy(
                        color = style.selectedColor
                    ),
                    maxLines = 1
                )

                val textPositionX = if (selectedDataPointIndex == visibleDataPointsIndices.last){
                    x-valueResult.size.width
                }else{
                    x-valueResult.size.width/2f
                }+ result.size.width/2f

                val isTextInVisibleRange =
                    (size.width - textPositionX).roundToInt() in 0..size.width.roundToInt()


                if (isTextInVisibleRange)
                drawText(
                    textLayoutResult = valueResult,
                    topLeft = Offset(
                        x = textPositionX,
                        y = viewPortTopY - valueResult.size.height -10f
                    )

                )
            }

        }




        val heightRequiredForLabels = xLabelLineHeight *
                (labelCountExcludingLastLabel+1)

        //for giving space in y elements
        val remainingHeightForLabels = labelViewPortHeightPx - heightRequiredForLabels
        val spaceBetweenLabels = remainingHeightForLabels / labelCountExcludingLastLabel



        yLabelTextLayoutResults.forEachIndexed { index, result ->
            val x = horizontalPaddingPx + maxYLabelWidth -result.size.width.toFloat()
            val y = viewPortTopY +
                    index *( xLabelLineHeight +spaceBetweenLabels)-
                    xLabelLineHeight/2f

            drawText(
                textLayoutResult = result,
                topLeft = Offset(
                    x = x,
                    y = y
                ),
                color = style.unselectedColor
            )

            if (showHelperLines){
                drawLine(
                    color = style.unselectedColor,
                    start = Offset(
                        x = viewPortLeftX,
                        y = y+result.size.height.toFloat()/2
                    ),
                    end = Offset(
                        x = viewPortRightX,
                        y = y+result.size.height.toFloat()/2
                    ),
                    strokeWidth = style.helperLinesThicknessPx
                )
            }


        }







    }


}

@Preview(widthDp = 1000)
@Composable
private fun PreviewLineChart() {
  CryptoTrackerTheme {
      val coinHistoryRandomized = remember {
          (1..20).map {
             CoinPrice(
                 priceUsd = Random.nextFloat() *1000.0,
                 dateTime = ZonedDateTime.now().plusHours(it.toLong())
             )
          }
      }

      val style = ChartStyle(
          chartLineColor = Color.Black,
          unselectedColor = Color(0xFF7C7C7C),
          selectedColor = Color.Black,
          helperLinesThicknessPx = 1f,
          axisLinesThicknessPx = 5f,
          labelFontSize = 14.sp,
          minYLabelSpacing = 25.dp,
          verticalPadding = 8.dp,
          horizontalPadding = 8.dp,
          xAxisLabelSpacing = 8.dp
      )

      val dataPoints = remember {
          coinHistoryRandomized.map {
              DataPoint(
                  x = it.dateTime.hour.toFloat(),
                  y = it.priceUsd.toFloat(),
                  xLabel =DateTimeFormatter
                      .ofPattern("ha\nM/d")
                      .format(it.dateTime)

              )
          }
      }

      LineChart(
          dataPoints = dataPoints,
          style = style,
          visibleDataPointsIndices = 0..19,
          unit = "$",
          modifier = Modifier
              .width(700.dp)
              .height(300.dp)
              .background(Color.White),
          selectedDataPoint = dataPoints[1]
      )
  }
}