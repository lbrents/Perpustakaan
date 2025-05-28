package com.example.perpustakaan.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.perpustakaan.database.DisplaySeries
import com.example.perpustakaan.screens.Components.Action.C_IconButton
import kotlin.math.roundToInt

/**
 * Singleton object that contains all of the components used throughout the project.
 */
object Components {

    /**
     * Components that are intended to facilitate performing some type of action.
     */
    object Action {

        @Composable
        fun C_IconButton(
            icon: ImageVector,
            enabled: Boolean = true,
            onClick: () -> Unit = {}
        ) { IconButton(onClick, enabled = enabled) { Icon(icon, null) } }

        @Composable
        fun C_NewSeriesButton(
            onClick: () -> Unit
        ) {

            ExtendedFloatingActionButton(
                text = { Text("Add") },
                icon = { Icon(Icons.Default.Add, null) },
                onClick = onClick
            )

        }

    }

    /**
     * Components that communicate something to the user.
     */
    object Communication {

        @Composable
        fun C_SnackbarNotifier(
            hostState: SnackbarHostState
        ) {

            SnackbarHost(
                hostState = hostState,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            )

        }

    }

    /**
     * Components that act as containers for other components.
     */
    object Containment {

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun C_MenuDrawer(
            expanded: Boolean,
            onDismiss: () -> Unit,
            content: @Composable (ColumnScope.() -> Unit)
        ) {

            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

            if (expanded) {

                ModalBottomSheet(
                    onDismissRequest = onDismiss,
                    sheetState = sheetState,
                    content = content,
                    modifier = Modifier
                        .padding(top = 46.dp)
                )

            }

        }

        @Composable
        fun C_SearchInterface(
            content: @Composable ColumnScope.() -> Unit
        ) {

            Surface(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) { content() }

            }

        }

    }

    /**
     * Components that serve to take text input from the user.
     */
    object Input {

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun C_SearchBar(
            expanded: Boolean,
            onExpandedChange: (Boolean) -> Unit,
            searchText: String,
            onSearchChange: (String) -> Unit,
            onSearch: (String) -> Unit,
            onMenuClick: () -> Unit,
            modifier: Modifier = Modifier,
            content: @Composable (ColumnScope.() -> Unit)
        ) {

            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = searchText,
                        onQueryChange = onSearchChange,
                        onSearch = { onSearch(it); onExpandedChange(false) },
                        expanded = expanded,
                        onExpandedChange = onExpandedChange,
                        placeholder = { Text("Search library") },
                        leadingIcon = {
                            AnimatedContent(expanded) { targetState ->
                                if (targetState) {
                                    C_IconButton(Icons.AutoMirrored.Filled.ArrowBack) {
                                        onExpandedChange(false)
                                    }
                                } else {
                                    Icon(Icons.Default.Search, null)
                                }
                            }
                        },
                        trailingIcon = {
                            AnimatedContent(expanded) { targetState ->
                                if (targetState) {
                                    C_IconButton(Icons.Default.Close) { onSearchChange("") }
                                } else {
                                    C_IconButton(Icons.Default.Menu) { onMenuClick() }
                                }
                            }
                        }
                    )
                },
                expanded = expanded,
                onExpandedChange = onExpandedChange,
                windowInsets = WindowInsets(0, 0, 0, 0),
                modifier = modifier,
                content = content
            )

        }

        @Composable
        fun C_TextEdit(
            value: String,
            onValueChange: (String) -> Unit,
            enabled: Boolean = true,
            label: String? = null,
            leadingIcon: ImageVector? = null
        ) {

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                label = { if (label != null) Text(label) },
                leadingIcon = if (leadingIcon != null) { { Icon(leadingIcon, null) } } else null,
                modifier = Modifier
                    .fillMaxWidth()
            )

        }

        @Composable
        fun C_TextEditWithError(
            value: String,
            onValueChange: (String) -> Unit,
            enabled: Boolean = true,
            label: String? = null,
            leadingIcon: ImageVector? = null,
            isError: Boolean = false,
            errorText: String? = null
        ) {

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                label = { if (label != null) Text(label) },
                leadingIcon = if (leadingIcon != null) { { Icon(leadingIcon, null) } } else null,
                supportingText = if (errorText != null) { {if (isError) Text(errorText)} } else null,
                isError = isError,
                modifier = Modifier
                    .fillMaxWidth()
            )

        }

        @Composable
        fun C_NumberEdit(
            value: String,
            onValueChange: (String) -> Unit,
            enabled: Boolean = true,
            label: String? = null,
            leadingIcon: ImageVector? = null,
            includeSpecialCharacters: Boolean = false
        ) {

            OutlinedTextField(
                value = value,
                onValueChange = {
                    if (includeSpecialCharacters) onValueChange(it)
                    else if (it.isDigitsOnly()) onValueChange(it)
                },
                enabled = enabled,
                label = { if (label != null) Text(label) },
                leadingIcon = if (leadingIcon != null) { { Icon(leadingIcon, null) } } else null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
            )

        }

        @Composable
        fun C_NumberEditWithError(
            value: String,
            onValueChange: (String) -> Unit,
            enabled: Boolean = true,
            label: String? = null,
            leadingIcon: ImageVector? = null,
            isError: Boolean = false,
            errorText: String? = null,
            includeSpecialCharacters: Boolean = false
        ) {

            OutlinedTextField(
                value = value,
                onValueChange = {
                    if (includeSpecialCharacters) onValueChange(it)
                    else if (it.isDigitsOnly()) onValueChange(it)
                },
                enabled = enabled,
                label = { if (label != null) Text(label) },
                leadingIcon = if (leadingIcon != null) { { Icon(leadingIcon, null) } } else null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                supportingText = if (errorText != null) { {if (isError) Text(errorText)} } else null,
                isError = isError,
                modifier = Modifier
                    .fillMaxWidth()
            )

        }

    }

    /**
     * Components that serve to select some option.
     */
    object Selection {

        @Composable
        fun C_CheckBox(
            checked: Boolean,
            onCheckedChange: (Boolean) -> Unit,
            label: String
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onCheckedChange(checked.not()) }
            ) {
                Text(label, modifier = Modifier.padding(start = 8.dp))
                Checkbox(checked, onCheckedChange)
            }

        }

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun C_ListDropdown(
            selected: String,
            onSelectedChange: (String) -> Unit,
            options: List<String>,
            label: String,
            modifier: Modifier = Modifier
        ) {

            // Expansion state of the dropdown box
            var expanded by remember { mutableStateOf(false) }

            // Container for the actual dropdown box
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = expanded.not() },
                modifier = modifier
            ) {

                // Text field able to spawn the menu
                OutlinedTextField(
                    readOnly = true,
                    value = selected,
                    onValueChange = { /* Do nothing here */ },
                    label = { Text(label) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                        .fillMaxWidth()
                )

                // Menu with all selectable options
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    // Each menu item from the options list
                    options.forEach { option: String ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = { onSelectedChange(option); expanded = false }
                        )
                    }
                }

            }

        }

        @Composable
        fun C_InChipSelect(
            selected: List<String>,
            options: List<String>,
            onSelectedChange: (List<String>) -> Unit
        ) {

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                options.forEach { option: String ->
                    FilterChip(
                        selected = option in selected,
                        onClick = {
                            val newOptions = if (option in selected) selected.minus(option) else selected.plus(option)
                            onSelectedChange(newOptions)
                        },
                        label = { Text(option) }
                    )
                }
            }

        }

        @Composable
        fun C_ExChipSelect(
            selected: String,
            options: List<String>,
            onSelectedChange: (String) -> Unit
        ) {

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                options.forEach { option: String ->
                    FilterChip(
                        selected = (option == selected),
                        onClick = { onSelectedChange(option) },
                        label = { Text(option) }
                    )
                }
            }

        }

        @Composable
        fun C_Slider(
            value: Int,
            onValueChange: (Int) -> Unit
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = if (value == 0) "None" else value.toString(),
                    modifier = Modifier
                        .width(64.dp)
                )

                Slider(
                    value = value.toFloat(),
                    onValueChange = { onValueChange(it.roundToInt()) },
                    valueRange = 0f..10f,
                    steps = 9,
                    modifier = Modifier
                        .padding(end = 32.dp)
                )

            }

        }

        @Composable
        fun C_RangeSlider(
            range: IntRange,
            onRangeChange: (IntRange) -> Unit
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "${if (range.start == 0) "None" else range.start} to " +
                        "${if (range.endInclusive == 0) "None" else range.endInclusive}",
                    modifier = Modifier
                        .width(112.dp)
                )

                RangeSlider(
                    value = range.start.toFloat()..range.endInclusive.toFloat(),
                    onValueChange = { onRangeChange(it.start.roundToInt()..<it.endInclusive.roundToInt() + 1) },
                    valueRange = 0f..10f,
                    steps = 9,
                    modifier = Modifier
                        .padding(end = 32.dp)
                )

            }

        }

    }

    /**
     * Components that are created using majority other components.
     */
    object Compound {

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun C_TopBar(
            title: String,
            leadingIcon: ImageVector? = null,
            onLeadingClick: (() -> Unit)? = null,
            actionIcon: ImageVector? = null,
            actionEnabled: Boolean = true,
            onActionClick: (() -> Unit)? = null
        ) {

            TopAppBar(
                title = { Text(title) },
                navigationIcon = if (leadingIcon != null) {
                    { C_IconButton(leadingIcon) { if (onLeadingClick != null) onLeadingClick() } }
                } else { {} },
                actions = if (actionIcon != null) {
                    { C_IconButton(actionIcon, actionEnabled) { if (onActionClick != null) onActionClick() } }
                } else { {} }
            )

        }

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun C_DeleteSeriesDialog(
            focused: Boolean,
            body: String,
            onDismiss: () -> Unit,
            onDelete: () -> Unit
        ) {
            if (focused) {

                BasicAlertDialog(
                    onDismissRequest = onDismiss,
                ) {
                    Surface(
                        shape = MaterialTheme.shapes.large,
                        tonalElevation = AlertDialogDefaults.TonalElevation,
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            Icon(Icons.Default.Delete, null, modifier = Modifier.scale(1.5f))
                            Spacer(Modifier.height(16.dp))
                            Text(body)
                            Spacer(Modifier.height(24.dp))
                            TextButton(
                                onClick = { onDelete(); onDismiss() },
                                modifier = Modifier
                                    .align(Alignment.End)
                            ) { Text("Delete") }
                        }
                    }
                }

            }
        }

        @Composable
        fun C_SeriesItem(
            displaySeries: DisplaySeries,
            onClick: (Int) -> Unit,
            onLongClick: (Int) -> Unit,
            modifier: Modifier = Modifier
        ) {

            ListItem(
                headlineContent = { Text(displaySeries.title) },
                overlineContent = { Text(displaySeries.bookType.label) },
                leadingContent  = { Icon(displaySeries.userStatus.icon, null) },
                trailingContent = { Text(displaySeries.volumesOwned) },
                modifier = modifier
                    .combinedClickable(
                        onClick = { onClick(displaySeries.uid) },
                        onLongClick = { onLongClick(displaySeries.uid) }
                    )
            )

        }

        @Composable
        fun C_SeriesList(
            seriesList: List<DisplaySeries>,
            onItemClick: (Int) -> Unit,
            onItemHold: (Int) -> Unit,
            onEndReached: () -> Unit
        ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 74.dp)
            ) {

                items(seriesList) { series: DisplaySeries ->
                    C_SeriesItem(
                        displaySeries = series,
                        onClick = onItemClick,
                        onLongClick = onItemHold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                }

                item {
                    Spacer(Modifier.height(32.dp))
                    onEndReached()
                }

            }

        }

    }

}