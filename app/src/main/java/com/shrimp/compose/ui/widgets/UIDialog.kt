package com.shrimp.compose.ui.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shrimp.compose.ui.theme.AppTheme

@Composable
fun DialogTitle(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 13.sp,
    color: Color = AppTheme.colors.textPrimary,
    textAlign: TextAlign = TextAlign.Start,
) {
    Text(
        text = text,
        fontSize = fontSize,
        modifier = modifier,
        color = color,
        overflow = TextOverflow.Ellipsis,
        textAlign = textAlign
    )
}

@Composable
fun DialogContent(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 13.sp,
    color: Color = AppTheme.colors.textSecondary,
    maxLines: Int = 99,
    textAlign: TextAlign = TextAlign.Start,
    canCopy: Boolean = false,
) {
    if (canCopy) {
        SelectionContainer {
            Text(
                text = text,
                fontSize = fontSize,
                modifier = modifier,
                maxLines = maxLines,
                color = color,
                overflow = TextOverflow.Ellipsis,
                textAlign = textAlign
            )
        }
    } else {
        Text(
            text = text,
            fontSize = fontSize,
            color = color,
            overflow = TextOverflow.Ellipsis,
            textAlign = textAlign
        )
    }
}

@Composable
fun SampleAlertDialog(
    title: String,
    content: String,
    cancelText: String = "取消",
    confirmText: String = "继续",
    onConfirmClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        title = {
            DialogTitle(text = title)
        },
        text = {
            DialogContent(text = content)
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onConfirmClick.invoke()
                onDismiss.invoke()
            }) {
                DialogContent(text = confirmText, color = AppTheme.colors.confirm)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss.invoke() }) {
                DialogContent(text = cancelText)
            }
        },
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    )
}



