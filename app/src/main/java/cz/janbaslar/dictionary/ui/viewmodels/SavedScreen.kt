package cz.janbaslar.dictionary.ui.viewmodels

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.janbaslar.dictionary.data.models.SimpleWordDefinition
import cz.janbaslar.dictionary.service.SharedPreferencesService
import cz.janbaslar.dictionary.ui.components.IconWithText


private fun deleteWord(
    definition: SimpleWordDefinition,
    allDefinitions: List<SimpleWordDefinition>,
    sharedPreferencesService: SharedPreferencesService
): List<SimpleWordDefinition> {
    val updatedDefinitions = allDefinitions.minus(definition)
    sharedPreferencesService.saveDefinitions(updatedDefinitions)
    return updatedDefinitions
}

private fun filterDefinitions(
    all: List<SimpleWordDefinition>,
    prefix: String
): List<SimpleWordDefinition> {
    return all.filter { simpleWordDefinition ->
        simpleWordDefinition.word.startsWith(
            prefix
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedScreen(
    sharedPreferencesService: SharedPreferencesService,
    lastWord: MutableState<String>
) {
    var word by remember { lastWord }
    var savedDefinitions by remember { mutableStateOf(sharedPreferencesService.getSavedDefinitions()) }
    var filteredDefinitions by remember {
        mutableStateOf(
            filterDefinitions(
                savedDefinitions,
                word
            )
        )
    }

    Box(modifier = Modifier.background(color = MaterialTheme.colorScheme.background)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(bottom = 16.dp)
            ) {
                OutlinedTextField(
                    value = word,
                    onValueChange = {
                        word = it
                        filteredDefinitions = filterDefinitions(savedDefinitions, word)
                    },
                    label = { Text("Filter saved words") },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = "Search the word"
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = null
                    ),
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }

            if (filteredDefinitions.isEmpty()) {
                IconWithText(
                    text = "There is nothing here.",
                    icon = Icons.Filled.MenuBook,
                    error = false
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {

                    }
                    filteredDefinitions.forEach { definition ->
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
                                                    filteredDefinitions =
                                                        filterDefinitions(savedDefinitions, word)
                                                },
                                                colors = IconButtonDefaults.iconButtonColors(
                                                    containerColor = MaterialTheme.colorScheme.error,
                                                    contentColor = MaterialTheme.colorScheme.surface
                                                )
                                            ) {
                                                Icon(
                                                    Icons.Filled.BookmarkRemove,
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
    }
}