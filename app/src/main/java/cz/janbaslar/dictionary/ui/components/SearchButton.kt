package cz.janbaslar.dictionary.ui.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchButton(onSearch: () -> Unit) {
    FilledIconButton(
        onClick = onSearch,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = 8.dp)
            .width(64.dp)
    ) {
        Icon(
            Icons.Filled.Search,
            contentDescription = "Search",
            Modifier.size(28.dp)
        )
    }
}