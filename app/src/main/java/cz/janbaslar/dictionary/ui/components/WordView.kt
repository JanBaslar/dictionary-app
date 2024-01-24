package cz.janbaslar.dictionary.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import cz.janbaslar.dictionary.data.models.ApiResponse
import cz.janbaslar.dictionary.data.models.SimpleWordDefinition
import cz.janbaslar.dictionary.data.models.WordDefinition
import cz.janbaslar.dictionary.service.SharedPreferencesService

@Composable
fun ShowWord(response: ApiResponse, sharedPreferencesService: SharedPreferencesService) {
    if (response.isSuccessful()) {
        if (response.isEmpty()) {
            IconWithText(
                text = "Type a word and then search it.",
                icon = Icons.Filled.Search,
                error = false
            )
        } else {
            response.content?.let { WordPresentation(it, sharedPreferencesService) }
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
                error = true
            )
        }
    }
}

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

fun saveWord(definition: WordDefinition, sharedPreferencesService: SharedPreferencesService) {
    val savedDefinitions = sharedPreferencesService.getSavedDefinitions()
    val firstMeaning = definition.meanings.first()
    val simpleWordDefinition = SimpleWordDefinition(
        definition.word,
        firstMeaning.partOfSpeech,
        firstMeaning.definitions.first().definition
    )

    if (!savedDefinitions.contains(simpleWordDefinition)) {
        val updatedDefinitions = savedDefinitions.plus(simpleWordDefinition)
        sharedPreferencesService.saveDefinitions(updatedDefinitions)
    }
}

@Composable
fun WordPresentation(
    definition: WordDefinition,
    sharedPreferencesService: SharedPreferencesService
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(definition.word, style = MaterialTheme.typography.displaySmall)
                FilledIconButton(
                    onClick = { saveWord(definition, sharedPreferencesService) },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Icon(Icons.Filled.Bookmark, contentDescription = "Save the word")
                }
            }
        }

        definition.meanings.forEach { meaning ->
            item {
                Column {
                    Text(
                        meaning.partOfSpeech, style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 8.dp, top = 16.dp)
                    )
                    meaning.definitions.forEach { def ->
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
                                Text(
                                    def.definition,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                if (def.example != null) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        "Example: ${def.example}",
                                        style = TextStyle(
                                            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                                            fontStyle = FontStyle.Italic
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}