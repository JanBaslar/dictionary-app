package cz.janbaslar.dictionary.ui.viewmodels

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import cz.janbaslar.dictionary.data.models.SimpleWordDefinition
import cz.janbaslar.dictionary.service.SharedPreferencesService


fun deleteWord(
    definition: SimpleWordDefinition,
    allDefinitions: List<SimpleWordDefinition>,
    sharedPreferencesService: SharedPreferencesService
): List<SimpleWordDefinition> {
    val updatedDefinitions = allDefinitions.minus(definition)
    sharedPreferencesService.saveDefinitions(updatedDefinitions)
    return updatedDefinitions
}

@Composable
fun SavedScreen(sharedPreferencesService: SharedPreferencesService) {
    var savedDefinitions by remember { mutableStateOf(sharedPreferencesService.getSavedDefinitions()) }

    Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            savedDefinitions.forEach { definition ->
                item {
                    Column {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                                .shadow(4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "${definition.word} (${definition.partOfSpeech})",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    FilledIconButton(
                                        onClick = {
                                            savedDefinitions = deleteWord(
                                                definition,
                                                savedDefinitions,
                                                sharedPreferencesService
                                            )
                                        },
                                        colors = IconButtonDefaults.iconButtonColors(
                                            containerColor = MaterialTheme.colorScheme.error,
                                            contentColor = MaterialTheme.colorScheme.surface
                                        )
                                    ) {
                                        Icon(
                                            Icons.Filled.DeleteOutline,
                                            contentDescription = "Save the word"
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    definition.meaning,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}