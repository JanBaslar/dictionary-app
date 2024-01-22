package cz.janbaslar.dictionary.ui.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchButton(onSearch: () -> Unit) {
    Button(
        onClick = onSearch,
        modifier = Modifier
            .fillMaxHeight()
    ) {
        Icon(
            Icons.Filled.Search,
            contentDescription = "Search",
            Modifier.size(28.dp)
        )
    }
}