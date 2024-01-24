package cz.janbaslar.dictionary.ui.viewmodels

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
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
import cz.janbaslar.dictionary.R
import cz.janbaslar.dictionary.data.models.ApiResponse
import cz.janbaslar.dictionary.service.FreeDictionaryApiService
import cz.janbaslar.dictionary.service.SharedPreferencesService
import cz.janbaslar.dictionary.ui.components.SearchButton
import cz.janbaslar.dictionary.ui.components.ShowWord

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    sharedPreferencesService: SharedPreferencesService,
    lastWord: MutableState<String>,
    lastApiResponse: MutableState<ApiResponse>
) {
    var word by remember { lastWord }
    val keyboardController = LocalSoftwareKeyboardController.current
    val freeDictionaryApiService = FreeDictionaryApiService()

    var apiResponse by remember { lastApiResponse }

    fun searchWord() {
        keyboardController?.hide()
        freeDictionaryApiService.getWordDefinition(
            word,
            object : FreeDictionaryApiService.DictionaryCallback {
                override fun onSuccess(result: ApiResponse) {
                    apiResponse = result;
                    word = "";
                }

                override fun onFailure(error: ApiResponse) {
                    apiResponse = error
                }
            })
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
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(end = 8.dp)
                )
                SearchButton(onSearch = { searchWord() })
            }
            ShowWord(apiResponse, sharedPreferencesService)
        }
    }
}

