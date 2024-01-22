package cz.janbaslar.dictionary.ui.viewmodels

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cz.janbaslar.dictionary.R
import cz.janbaslar.dictionary.service.FreeDictionaryApiService
import cz.janbaslar.dictionary.ui.components.SearchButton

@Composable
fun SearchScreen(navController: NavController, lastWord: MutableState<String>) {
    var word by remember { lastWord }
    val keyboardController = LocalSoftwareKeyboardController.current
    val freeDictionaryApiService = FreeDictionaryApiService();

    var callResult by remember { mutableStateOf("Not called yet") }

    fun searchWord() {
        keyboardController?.hide()
        freeDictionaryApiService.getWordDefinition(word, object : FreeDictionaryApiService.DictionaryCallback {
            override fun onSuccess(result: String) {
                // Handle success
                callResult = result;
            }

            override fun onFailure(error: String) {
                // Handle failure
                callResult = error
            }
        })
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .padding(16.dp)
        ) {
            TextField(
                value = word,
                onValueChange = {
                    word = it
                },
                label = { Text(stringResource(R.string.enter_a_word)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        searchWord()
                    }
                ),
                textStyle = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(end = 8.dp)
            )

            SearchButton(onSearch = { searchWord() })
        }
        Text(callResult)
    }
}

