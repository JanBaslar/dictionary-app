package cz.janbaslar.dictionary.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun IconWithText(text: String, icon: ImageVector, error: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Icon",
            tint = if (error) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.surfaceTint,
            modifier = Modifier
                .size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            color = if (error) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.surfaceTint,
            modifier = Modifier
                .wrapContentSize(align = Alignment.Center)
        )
    }
}