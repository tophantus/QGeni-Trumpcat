package com.example.qgeni.ui.screens.dictionary


import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.qgeni.data.model.word.Word
import com.example.qgeni.data.model.word.WordAccessHistory
import com.example.qgeni.data.model.word.WordMeaning
import com.example.qgeni.data.model.word.WordType
import com.example.qgeni.ui.theme.QGenITheme


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WordCard(
    word: WordAccessHistory,
    checked: Boolean,
    onCheckBoxClick: (Boolean) -> Unit,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onItemCLick: (String) -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                )
                .clip(
                    shape = RoundedCornerShape(10.dp)
                )
                .background(
                    color = MaterialTheme.colorScheme.onPrimary
                )
                .padding(
                    8.dp
                )
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = {check ->
                    onCheckBoxClick(check)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.primary,
                    checkmarkColor = MaterialTheme.colorScheme.onPrimary
                ),
            )
            Column (
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        onClick = {
                            onItemCLick(word.text)
                        }
                    )
            ) {
                Row (
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = MaterialTheme.typography.bodyLarge.toSpanStyle().copy(
                                    color = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                append(word.text)
                            }
                            withStyle(
                                style = MaterialTheme.typography.labelMedium.toSpanStyle().copy(
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            ) {
                                append(" " + "/" + word.pronunciation + "/" + "  ("+word.type+")")
                            }
                        },
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .clipToBounds(),
                        maxLines = 1
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = word.meaning ?: "",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Clip,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clipToBounds(),
                    maxLines = 1
                )
            }
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (isFavorite) {
                                    Icons.Outlined.Favorite
                                  } else {
                                    Icons.Outlined.FavoriteBorder
                                  },
                    contentDescription = "Favorite",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun WordCardLightPreview() {
    QGenITheme(dynamicColor = false) {
        WordCard(
            WordAccessHistory(
                1,
                "apple",
                "ˈæpəl",
                "noun",
                "qua tao",
            ),
            onFavoriteClick = {},
            onItemCLick = {},
            checked = true,
            onCheckBoxClick = {},
            isFavorite = false
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun WordCardDarkPreview() {
    QGenITheme(dynamicColor = false, darkTheme = true) {
        WordCard(
            WordAccessHistory(
                1,
                "apple",
                "ˈæpəl",
                "noun",
                "qua tao",
            ),
            onFavoriteClick = {},
            checked = false,
            onCheckBoxClick = {},
            isFavorite = false,
            onItemCLick = {}
        )
    }
}