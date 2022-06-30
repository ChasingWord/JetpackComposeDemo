package com.shrimp.compose.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shrimp.compose.R
import com.shrimp.compose.ui.theme.AppTheme
import com.shrimp.compose.ui.theme.color_f5f5f5

val PASSWORD_VISIBLE_ICON_PADDING = 40.dp

@Preview
@Composable
fun CustomEditViewPreview(){
    var inputCheckCodeOrPwd by remember { mutableStateOf("") }
    CustomEditView(
        modifier = Modifier
            .padding(29.dp, 20.dp, 29.dp, 0.dp)
            .background(color_f5f5f5, RoundedCornerShape(5.dp)),
        text = inputCheckCodeOrPwd,
        onValueChanged = {
            inputCheckCodeOrPwd = it
        },
        onDeleteClick = {
            inputCheckCodeOrPwd = ""
        },
        hintText = "请输入密码",
        isPassword = true,
        contentPaddingValues = PaddingValues(10.dp, 10.dp, (40.dp + PASSWORD_VISIBLE_ICON_PADDING), 10.dp),
        deleteIcon = R.drawable.delete_gray_60,
        deleteIconPaddingEnd = 10.dp,
        showIndicatorLine = false,
        isHideKeyboard = true,
    )
}

@Composable
fun CustomEditView(
    modifier: Modifier = Modifier,
    text: String,
    onValueChanged: (String) -> Unit,
    onDeleteClick: () -> Unit,
    hintText: String? = null,
    isPassword: Boolean = false,
    showPassword: Boolean = false,
    contentPaddingValues: PaddingValues = PaddingValues(0.dp),
    deleteIcon: Int = 0,
    deleteIconPaddingEnd: Dp = 0.dp,
    showIndicatorLine: Boolean = false,
    imeAction: ImeAction? = null,
    isHideKeyboard: Boolean = true,
    keyboardActions: KeyboardActions? = null,
) {
    var isShowPassword by remember { mutableStateOf(showPassword) }

    BasicLabelEditView(
        modifier = modifier,
        text = text,
        hintText = hintText,
        onValueChanged = { onValueChanged.invoke(it) },
        onDeleteClick = { onDeleteClick.invoke() },
        visualTransformation = if (isPassword && !isShowPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text,
        contentPaddingValues = contentPaddingValues,
        showIndicatorLine = showIndicatorLine,
        imeAction = imeAction,
        isHideKeyboard = isHideKeyboard,
        keyboardActions = keyboardActions,
    ) {
        Row(modifier = Modifier
            .align(Alignment.CenterEnd)) {
            if (isPassword)
                Icon(painter = painterResource(id = if (isShowPassword) R.drawable.icon_show_password_48 else R.drawable.icon_hide_password_48),
                    contentDescription = "",
                    modifier = Modifier
                        .clickable {
                            isShowPassword = !isShowPassword
                            onDeleteClick.invoke()
                        }
                        .padding(0.dp, 0.dp, deleteIconPaddingEnd, 0.dp)
                        .align(Alignment.CenterVertically))
            if (deleteIcon > 0 && text.isNotEmpty())
                Icon(painter = painterResource(id = deleteIcon), contentDescription = "",
                    modifier = Modifier
                        .clickable { onDeleteClick.invoke() }
                        .padding(0.dp, 0.dp, deleteIconPaddingEnd, 0.dp)
                        .align(Alignment.CenterVertically))
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BasicLabelEditView(
    modifier: Modifier = Modifier,
    text: String,
    onValueChanged: (String) -> Unit,
    onDeleteClick: () -> Unit,
    labelText: String? = null,
    labelTextColor: Color = AppTheme.colors.textPrimary,
    hintText: String? = null,
    hintTextColor: Color = AppTheme.colors.textSecondary,
    inputCursorColor: Color = AppTheme.colors.textPrimary,
    inputTextColor: Color = AppTheme.colors.textPrimary,
    borderColor: Color = AppTheme.colors.textSecondary,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardActions: KeyboardActions? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction? = null,
    isHideKeyboard: Boolean = true,
    contentPaddingValues: PaddingValues = PaddingValues(0.dp),
    showSystemDeleteIcon: Boolean = false,
    showIndicatorLine: Boolean = false,
    functionLayout: @Composable BoxScope.() -> Unit,
) {
    val keyboardService = LocalTextInputService.current
    val focusManager = LocalFocusManager.current
    Box(modifier = modifier) {
        CustomTextField(
            value = text,
            onValueChange = { onValueChanged(it) },
            textStyle = TextStyle(fontSize = 16.sp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .pointerInteropFilter { false },
            label = if (labelText == null) null else {
                {
                    Text(
                        text = labelText,
                        color = labelTextColor,
                    )
                }
            },
            placeholder = if (hintText == null) null else {
                {
                    Text(text = hintText, color = hintTextColor)
                }
            },
            // 直接使用系统的尾部按钮样式，会有个默认48dp的宽高，导致控件高度不能自由控制
            trailingIcon = if (showSystemDeleteIcon) {
                {
                    if (text.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null,
                            tint = AppTheme.colors.divider,
                            modifier = Modifier.clickable { onDeleteClick() }
                        )
                    }
                }
            } else null,
            singleLine = true,
            maxLines = 1,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = borderColor,
                unfocusedBorderColor = borderColor,
                textColor = inputTextColor,
                placeholderColor = hintTextColor,
                cursorColor = inputCursorColor
            ),
            visualTransformation = visualTransformation,
            keyboardActions = keyboardActions ?: if (!isHideKeyboard) {
                KeyboardActions {
                    focusManager.moveFocus(FocusDirection.Next)
                }
            } else {
                KeyboardActions {
                    keyboardService?.hideSoftwareKeyboard()
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction ?: if (isHideKeyboard) ImeAction.Done else ImeAction.Next
            ),
            contentPaddingValues = contentPaddingValues,
            showIndicatorLine = showIndicatorLine,
        )
        functionLayout.invoke(this)
    }
}

@Composable
private fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    contentPaddingValues: PaddingValues = PaddingValues(0.dp),
    showIndicatorLine: Boolean = false,
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = value)) }
    val textFieldValue = textFieldValueState.copy(text = value)

    CustomTextField(
        enabled = enabled,
        readOnly = readOnly,
        value = textFieldValue,
        onValueChange = {
            textFieldValueState = it
            if (value != it.text) {
                onValueChange(it.text)
            }
        },
        modifier = modifier,
        singleLine = singleLine,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        maxLines = maxLines,
        interactionSource = interactionSource,
        colors = colors,
        contentPaddingValues = contentPaddingValues,
        showIndicatorLine = showIndicatorLine,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CustomTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(),
    contentPaddingValues: PaddingValues = PaddingValues(0.dp),
    showIndicatorLine: Boolean = false,
) {
    // If color is not provided via the text style, use content color as a default
    val textColor = textStyle.color.takeOrElse {
        colors.textColor(enabled).value
    }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    (BasicTextField(
        value = value,
        modifier = if (showIndicatorLine) modifier.indicatorLine(enabled,
            isError,
            interactionSource,
            colors)
        else modifier,
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = mergedTextStyle,
        cursorBrush = SolidColor(colors.cursorColor(isError).value),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = singleLine,
        maxLines = maxLines,
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.TextFieldDecorationBox(
                value = value.text,
                visualTransformation = visualTransformation,
                innerTextField = innerTextField,
                placeholder = placeholder,
                label = label,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                singleLine = singleLine,
                enabled = enabled,
                isError = isError,
                interactionSource = interactionSource,
                colors = colors,
                contentPadding = contentPaddingValues
            )
        }
    ))
}
