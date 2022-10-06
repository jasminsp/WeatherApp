package com.jasminsp.weatherapp.fistView

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.jasminsp.weatherapp.R


@Composable
fun SearchBar(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
            .padding(horizontal = 60.dp, vertical = 20.dp)
    ) {
        TextField(
            value = "",
            onValueChange = {},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent),

            shape = MaterialTheme.shapes.large,
            placeholder = {
                context.getString(R.string.placeholderSearch)
            },
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .shadow(elevation = 30.dp, shape = RoundedCornerShape(size = 10.dp), clip = false)


        )
    }
}
