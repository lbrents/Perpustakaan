package com.example.perpustakaan.screens.homeScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.perpustakaan.enums.AuthorStatus
import com.example.perpustakaan.enums.BookType
import com.example.perpustakaan.enums.LibraryOrWishlist
import com.example.perpustakaan.enums.Priority
import com.example.perpustakaan.enums.SortType
import com.example.perpustakaan.enums.SortType.Companion.toSortType
import com.example.perpustakaan.enums.UserStatus
import com.example.perpustakaan.screens.Components.Action.C_NewSeriesButton
import com.example.perpustakaan.screens.Components.Communication.C_SnackbarNotifier
import com.example.perpustakaan.screens.Components.Compound.C_DeleteSeriesDialog
import com.example.perpustakaan.screens.Components.Compound.C_SeriesList
import com.example.perpustakaan.screens.Components.Containment.C_MenuDrawer
import com.example.perpustakaan.screens.Components.Containment.C_SearchInterface
import com.example.perpustakaan.screens.Components.Input.C_SearchBar
import com.example.perpustakaan.screens.Components.Selection.C_InChipSelect
import com.example.perpustakaan.screens.Components.Selection.C_ListDropdown
import com.example.perpustakaan.screens.Components.Selection.C_RangeSlider
import com.example.perpustakaan.screens.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    vm: HomeScreenViewModel,
    nv: NavHostController
) {

    /* ----------------------------------------------------------------------------------------- */
    // STATE

    // Viewmodel state
    val searchExpanded by vm.searchExpanded.state.collectAsStateWithLifecycle()
    val searchText by vm.searchText.state.collectAsStateWithLifecycle()
    val menuExpanded by vm.menuExpanded.state.collectAsStateWithLifecycle()
    val deleteDialogFocused by vm.deleteDialogFocused.state.collectAsStateWithLifecycle()

    val seriesList by vm.seriesList.state.collectAsStateWithLifecycle()
    val focusedSeriesUid by vm.focusedSeriesUid.state.collectAsStateWithLifecycle()
    val focusedSeriesTitle by remember { derivedStateOf { vm.findSeriesTitle(focusedSeriesUid) } }

    // Sort and filter states
    val selectedSort by vm.selectedSort.state.collectAsStateWithLifecycle()
    val wishlistFilter by vm.wishlistFilter.state.collectAsStateWithLifecycle()
    val bookTypeFilter by vm.bookTypeFilter.state.collectAsStateWithLifecycle()
    val authorStatusFilter by vm.authorStatusFilter.state.collectAsStateWithLifecycle()
    val userStatusFilter by vm.userStatusFilter.state.collectAsStateWithLifecycle()
    val priorityFilter by vm.priorityFilter.state.collectAsStateWithLifecycle()
    val ratingFilter by vm.ratingFilter.state.collectAsStateWithLifecycle()

    // Non viewmodel state
    val snackbarHostState = remember { SnackbarHostState() }

    /* ----------------------------------------------------------------------------------------- */
    // INIT STATE

    // Initialization triggered whenever the screen is entered
    LifecycleStartEffect(Unit) {
        vm.initialize()
        onStopOrDispose { /* Do nothing here? */ }
    }

    /* ----------------------------------------------------------------------------------------- */
    // UI

    // Shown when an item is held
    C_DeleteSeriesDialog(
        focused = deleteDialogFocused,
        body = "This action will permanently delete the series \"$focusedSeriesTitle\" from your library.",
        onDismiss = { vm.deleteDialogFocused upd false },
        onDelete = {
            vm.notifyUser(snackbarHostState, "The series \"$focusedSeriesTitle\" has been deleted.")
            vm.deleteFocusedFromDatabase()
        }
    )

    // Shown on searchbar menu click
    C_MenuDrawer(
        expanded   = menuExpanded,
        onDismiss  = { vm.menuExpanded upd false }
    ) { /* TODO: Implement whatever goes in the extra space */ }

    // Main screen ui
    Scaffold(
        snackbarHost = { C_SnackbarNotifier(snackbarHostState) },
        floatingActionButton = { C_NewSeriesButton(onClick = { nv.navigate(Screens.New) }) },
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .semantics { isTraversalGroup = true }
        ) {

            C_SearchBar(
                searchText       = searchText,
                onSearchChange   = { vm.searchText upd it },
                onSearch         = { vm.searchDatabase() },
                expanded         = searchExpanded,
                onExpandedChange = {
                    vm.searchExpanded upd it
                    if (it == false) vm.searchDatabase()
                },
                onMenuClick      = { vm.menuExpanded upd true },
                modifier         = Modifier
                    .align(Alignment.TopCenter)
                    .semantics { traversalIndex = 0f }
            ) {
                C_SearchInterface {

                    C_ListDropdown(
                        selected = selectedSort.label,
                        options = SortType.labels,
                        onSelectedChange = { option: String ->
                            vm.selectedSort upd option.toSortType()
                        },
                        label = "Sort By"
                    )

                    HorizontalDivider(Modifier.padding(vertical = 24.dp))

                    Text("Include Wishlist", style = MaterialTheme.typography.titleMedium)
                    C_InChipSelect(
                        selected = wishlistFilter,
                        options = LibraryOrWishlist.labels,
                        onSelectedChange = { vm.wishlistFilter upd it }
                    )

                    Spacer(Modifier.height(16.dp))

                    Text("Book Type", style = MaterialTheme.typography.titleMedium)
                    C_InChipSelect(
                        selected = bookTypeFilter,
                        options = BookType.labels,
                        onSelectedChange = { vm.bookTypeFilter upd it }
                    )

                    Spacer(Modifier.height(16.dp))

                    Text("Author Status", style = MaterialTheme.typography.titleMedium)
                    C_InChipSelect(
                        selected = authorStatusFilter,
                        options = AuthorStatus.labels,
                        onSelectedChange = { vm.authorStatusFilter upd it }
                    )

                    Spacer(Modifier.height(16.dp))

                    Text("User Status", style = MaterialTheme.typography.titleMedium)
                    C_InChipSelect(
                        selected = userStatusFilter,
                        options = UserStatus.labels,
                        onSelectedChange = { vm.userStatusFilter upd it }
                    )

                    Spacer(Modifier.height(16.dp))

                    Text("Priority", style = MaterialTheme.typography.titleMedium)
                    C_InChipSelect(
                        selected = priorityFilter,
                        options = Priority.labels,
                        onSelectedChange = { vm.priorityFilter upd it }
                    )

                    Spacer(Modifier.height(16.dp))

                    Text("Rating", style = MaterialTheme.typography.titleMedium)
                    C_RangeSlider(
                        range = ratingFilter,
                        onRangeChange = { vm.ratingFilter upd it }
                    )

                    Spacer(Modifier.height(256.dp))

                }
            }

            C_SeriesList(
                seriesList = seriesList,
                onItemClick = { nv.navigate(Screens.Edit(it)) },
                onItemHold = { vm.focusedSeriesUid upd it; vm.deleteDialogFocused upd true },
                onEndReached = { vm.loadNext() }
            )

        }

    }

}