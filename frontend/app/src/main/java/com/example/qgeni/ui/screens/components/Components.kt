package com.example.qgeni.ui.screens.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.qgeni.R
import com.example.qgeni.ui.theme.QGenITheme

/*
    Các thành phần dùng nhiều trong toàn bộ ứng dụng
 */

@Composable
fun ExpandableImageWithHandle(
    @DrawableRes
    imageResourceId: Int = R.drawable.fairy3,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    maxHeight: Dp = 300.dp,
) {
    var isExpanded by remember { mutableStateOf(true) }
    val imageHeight by animateDpAsState(
        targetValue = if (isExpanded) maxHeight else 20.dp,
        animationSpec = tween(durationMillis = 400),
        label = "imageHeight"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(imageHeight)
    ) {
        Box (
            modifier = Modifier.background(color = backgroundColor)
        ){
            // Ảnh có thể co giãn chiều cao
            Image(
                painter = painterResource(id = imageResourceId),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isExpanded) imageHeight - 10.dp else 0.dp)
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )
        }

        // Thanh điều khiển ở dưới cùng ảnh (không chặn UI khác)
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = imageHeight - 10.dp) // nằm sát đáy ảnh
                .height(2.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onBackground),
            contentAlignment = Alignment.Center
        ) {
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = imageHeight - 20.dp) // nằm sát đáy ảnh
                .height(20.dp)
                .width(30.dp)
                .background(MaterialTheme.colorScheme.onBackground, shape = RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = { isExpanded = !isExpanded },
            ) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}



@Composable
fun NextButton(
    onPrimary: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        colors =
            if (!onPrimary) {
                IconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary
                )
            } else {
                IconButtonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.onPrimary
                )
            },
        modifier = Modifier
            .size(60.dp)
            .clip(shape = RoundedCornerShape(size = 100.dp))
            .testTag("next_button")
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "next_button",
            tint =
                if (!onPrimary) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.primary
                },
            modifier = Modifier.fillMaxSize()
        )
    }
}
@Composable
fun CustomSolidButton(
    onClick: () -> Unit,
    text: String,
    bgColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onPrimary

) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .border(
                width = 5.dp,
                color = bgColor,
                shape = RoundedCornerShape(10.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = bgColor
        ),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = textColor
        )
    }
}

@Composable
fun CustomOutlinedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    color: Color,
    enabled: Boolean = true,
    style: TextStyle = MaterialTheme.typography.labelMedium
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        enabled = enabled,
        modifier = modifier
            .border(
                width = 1.dp,
                color = color,
                shape = RoundedCornerShape(10.dp)
            )
            .testTag("upload file")
    ) {
        Text(
            text = text,
            style = style,
            color = color
        )
    }
}

@Composable
fun CurvedBackground(modifier: Modifier = Modifier) {
    val color = MaterialTheme.colorScheme.onPrimary
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(320.dp)
            .background(MaterialTheme.colorScheme.primary)
            .drawBehind {
                val width = size.width
                val height = size.height

                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(0f, height * 0.6f)
                    cubicTo(
                        width * 0.25f, height * 0.4f,
                        width * 0.75f, height * 0.8f,
                        width, height * 0.6f
                    )
                    lineTo(width, height)
                    lineTo(0f, height)
                    close()
                }
                drawPath(
                    path = path,
                    color = color
                )
            }
    )
}


@Composable
fun CustomIconSwitch2(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    useIcon: Boolean,
    enabledIconResId: ImageVector,   // Icon khi bật (on)
    disabledIconResId: ImageVector, // Icon khi tắt (off)
    testTag: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(0.dp)
            .wrapContentSize()
    ) {
        // Custom Switch với icon
        Box(
            modifier = Modifier
                .size(60.dp, 30.dp)
                .clip(CircleShape)
                .background(
                    color = MaterialTheme.colorScheme.primary
                )
                .padding(4.dp)
                .clickable { onCheckedChange(!checked) }
                .testTag(testTag)
        ) {
            // Vòng tròn bên trong switch
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .align(
                        if (checked) Alignment.CenterEnd
                        else Alignment.CenterStart
                    )
                    .clip(CircleShape)
                    .background(
                        color = MaterialTheme.colorScheme.secondary
                    )
            ) {
                if (useIcon) {
                    Icon(
                        if (checked) enabledIconResId else disabledIconResId,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(3.dp)
                            .fillMaxSize(),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CustomSwitchOffPreview() {
    QGenITheme(dynamicColor = false) {
        CustomIconSwitch2(
            checked = false,
            onCheckedChange = {},
            useIcon = true,
            enabledIconResId = Icons.Outlined.Notifications,
            disabledIconResId = Icons.Outlined.NotificationsOff,
            "a"
        )
    }
}

@Preview
@Composable
fun CustomSwitchOnPreview() {
    QGenITheme(dynamicColor = false) {
        CustomIconSwitch2(
            checked = true,
            onCheckedChange = {},
            useIcon = true,
            enabledIconResId = Icons.Outlined.Notifications,
            disabledIconResId = Icons.Outlined.NotificationsOff,
            "a"
        )
    }
}



@Preview
@Composable
fun ExpandableImageWithHandleLightPreview() {
    QGenITheme(dynamicColor = false, darkTheme = false) {
        ExpandableImageWithHandle()
    }
}


@Preview
@Composable
fun ExpandableImageWithHandleDarkPreview() {
    QGenITheme(dynamicColor = false, darkTheme = true) {
        ExpandableImageWithHandle()
    }
}

@Preview
@Composable
fun CurvedBackgroundPreview() {
    QGenITheme(dynamicColor = false) {
        CurvedBackground()
    }
}


@Preview
@Composable
fun CustomOutlinedButtonPreview() {
    QGenITheme(dynamicColor =  false) {
        CustomOutlinedButton(
            onClick = {},
            text = "NỘP BÀI",
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
fun CustomSolidButtonPreview() {
    QGenITheme(dynamicColor =  false) {
        CustomSolidButton(
            onClick = {},
            text = "NỘP BÀI"
        )
    }
}

@Preview
@Composable
fun CustomSolidButtonPreview2() {
    QGenITheme(dynamicColor =  false) {
        CustomSolidButton(
            onClick = {},
            text = "NỘP BÀI",
            bgColor = MaterialTheme.colorScheme.onPrimary,
            textColor = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
fun NextButtonLightPreview() {
    QGenITheme(dynamicColor = false) {
        NextButton(
            onPrimary = true,
            onClick = {}
        )
    }
}

@Preview
@Composable
fun NextButtonDarkPreview() {
    QGenITheme(dynamicColor = false, darkTheme = true) {
        NextButton(
            onPrimary = true,
            onClick = {}
        )
    }
}