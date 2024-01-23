package cz.janbaslar.dictionary.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cz.janbaslar.dictionary.data.models.ApiResponse

@Composable
fun ShowWord(response: ApiResponse) {
    if (response.isSuccessful()) {
        if (response.isEmpty()) {
            IconWithText(
                text = "Type a word and then search it.",
                icon = Icons.Filled.Search,
                error = false
            )
        } else {
            response.content?.let {
                Text(it.word)
            }
        }
    } else {
        if (response.wordNotFound()) {
            IconWithText(
                text = response.getErrorMessage(),
                icon = Icons.Filled.QuestionMark,
                error = true
            )
        } else {
            IconWithText(
                text = response.getErrorMessage(),
                icon = Icons.Filled.Error,
                error = false
            )
        }
    }
}

@Composable
fun IconWithText(text: String, icon: ImageVector, error: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Icon",
            tint = if (error) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.surfaceTint,
            modifier = Modifier
                .size(72.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            color = if (error) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.surfaceTint,
            modifier = Modifier
                .wrapContentSize(align = Alignment.Center)
                .padding(bottom = 16.dp)
        )
    }
}