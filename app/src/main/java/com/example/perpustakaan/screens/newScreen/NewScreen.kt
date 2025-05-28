package com.example.perpustakaan.screens.newScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.perpustakaan.enums.AuthorStatus
import com.example.perpustakaan.enums.AuthorStatus.Companion.toAuthorStatus
import com.example.perpustakaan.enums.BookType
import com.example.perpustakaan.enums.BookType.Companion.toBookType
import com.example.perpustakaan.enums.Language
import com.example.perpustakaan.enums.Priority
import com.example.perpustakaan.enums.Priority.Companion.toPriority
import com.example.perpustakaan.enums.UserStatus
import com.example.perpustakaan.enums.UserStatus.Companion.toUserStatus
import com.example.perpustakaan.screens.Components.Compound.C_TopBar
import com.example.perpustakaan.screens.Components.Input.C_NumberEdit
import com.example.perpustakaan.screens.Components.Input.C_NumberEditWithError
import com.example.perpustakaan.screens.Components.Input.C_TextEdit
import com.example.perpustakaan.screens.Components.Input.C_TextEditWithError
import com.example.perpustakaan.screens.Components.Selection.C_CheckBox
import com.example.perpustakaan.screens.Components.Selection.C_ExChipSelect
import com.example.perpustakaan.screens.Components.Selection.C_Slider

@Composable
fun NewScreen(
    vm: NewScreenViewModel,
    nv: NavHostController
) {

    /* ----------------------------------------------------------------------------------------- */
    // STATE

    val isOnWishlist by vm.isOnWishlist.state.collectAsStateWithLifecycle()

    val titleEn by vm.titleEn.state.collectAsStateWithLifecycle()
    val titleJp by vm.titleJp.state.collectAsStateWithLifecycle()
    val description by vm.description.state.collectAsStateWithLifecycle()
    val bookType by vm.bookType.state.collectAsStateWithLifecycle()
    val authorStatus by vm.authorStatus.state.collectAsStateWithLifecycle()
    val totalVolumes by vm.totalVolumes.state.collectAsStateWithLifecycle()

    val userStatus by vm.userStatus.state.collectAsStateWithLifecycle()
    val volumesOwned by vm.volumesOwned.state.collectAsStateWithLifecycle()
    val priority by vm.priority.state.collectAsStateWithLifecycle()
    val rating by vm.rating.state.collectAsStateWithLifecycle()
    val notes by vm.notes.state.collectAsStateWithLifecycle()

    val titlesEdited by vm.titlesEdited.state.collectAsStateWithLifecycle()

    val titleDisplayError by remember { derivedStateOf {
        titlesEdited && titleEn.isBlank() && titleJp.isBlank()
    } }
    val primaryLanguage by remember { derivedStateOf {
        if (titleEn.isNotBlank()) {
            Language.ENGLISH
        } else if (titleJp.isNotBlank()) {
            Language.JAPANESE
        } else {
            null
        }
    } }
    val totalVolumesEnabled by remember { derivedStateOf {
        authorStatus == AuthorStatus.COMPLETED
    } }
    val enableVolumesOwned by remember { derivedStateOf {
        isOnWishlist.not()
    } }
    val volumesOwnedDisplayError by remember { derivedStateOf {
        enableVolumesOwned && volumesOwned.isBlank()
    } }
    val readyToContinue by remember { derivedStateOf {
        (primaryLanguage != null) && !volumesOwnedDisplayError
    } }

    /* ----------------------------------------------------------------------------------------- */
    // INIT STATE

    // Initialization triggered whenever the screen is entered
    LifecycleStartEffect(Unit) {
        vm.initialize()
        onStopOrDispose { /* Do nothing here? */ }
    }

    /* ----------------------------------------------------------------------------------------- */
    // UI

    Scaffold(
        topBar = { C_TopBar(
            title = "Add New Series",
            leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
            onLeadingClick = { nv.popBackStack() },
            actionIcon = Icons.Default.Check,
            actionEnabled = readyToContinue,
            onActionClick = { vm.finishAddingSeries(); nv.popBackStack() }
        ) },
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            // Add to wishlist
            C_CheckBox(
                checked = isOnWishlist,
                onCheckedChange = { vm.isOnWishlist upd it; if (it) vm.volumesOwned upd ""  },
                label = "Add to wishlist"
            )

            // English title
            C_TextEditWithError(
                value = titleEn,
                onValueChange = { vm.titleEn upd it; vm.titlesEdited upd true },
                label = "English Title",
                leadingIcon = if (titleDisplayError) Icons.Default.Clear else Icons.Default.Star,
                isError = titleDisplayError
            )

            // Japanese title
            C_TextEditWithError(
                value = titleJp,
                onValueChange = { vm.titleJp upd it; vm.titlesEdited upd true },
                label = "Japanese Title",
                leadingIcon = if (titleDisplayError) Icons.Default.Clear else Icons.Default.Star,
                isError = titleDisplayError,
                errorText = "At least one title field must be filled"
            )

            // Description
            C_TextEdit(
                value = description,
                onValueChange = { vm.description upd it },
                label = "Description",
            )

            // Book Type
            Spacer(Modifier.height(16.dp))
            Text("Book Type", style = MaterialTheme.typography.titleMedium)
            C_ExChipSelect(
                selected = bookType.label,
                options = BookType.labels,
                onSelectedChange = { vm.bookType upd it.toBookType() }
            )

            // Author production status
            Spacer(Modifier.height(16.dp))
            Text("Author Status", style = MaterialTheme.typography.titleMedium)
            C_ExChipSelect(
                selected = authorStatus.label,
                options = AuthorStatus.labels,
                onSelectedChange = { vm.authorStatus upd it.toAuthorStatus() }
            )

            // Total volumes
            Spacer(Modifier.height(4.dp))
            C_NumberEdit(
                value = totalVolumes,
                onValueChange = { vm.totalVolumes upd it },
                enabled = totalVolumesEnabled,
                label = "Total Volumes"
            )

            HorizontalDivider(Modifier.padding(vertical = 32.dp))

            // User read status
            Text("User Status", style = MaterialTheme.typography.titleMedium)
            C_ExChipSelect(
                selected = userStatus.label,
                options = UserStatus.labels,
                onSelectedChange = { vm.userStatus upd it.toUserStatus() }
            )

            // Volumes owned
            Spacer(Modifier.height(16.dp))
            C_NumberEditWithError(
                value = volumesOwned,
                onValueChange = { vm.volumesOwned upd it },
                enabled = enableVolumesOwned,
                label = "Volumes Owned",
                isError = volumesOwnedDisplayError,
                errorText = "Volumes owned can not be blank if it is in the library",
                includeSpecialCharacters = true
            )

            // Buy priority
            Spacer(Modifier.height(16.dp))
            Text("Priority", style = MaterialTheme.typography.titleMedium)
            C_ExChipSelect(
                selected = priority.label,
                options = Priority.labels,
                onSelectedChange = { vm.priority upd it.toPriority() }
            )

            // Rating
            Spacer(Modifier.height(16.dp))
            Text("Rating", style = MaterialTheme.typography.titleMedium)
            C_Slider(
                value = rating,
                onValueChange = { vm.rating upd it }
            )

            // Notes
            Spacer(Modifier.height(16.dp))
            C_TextEdit(
                value = notes,
                onValueChange = { vm.notes upd it },
                label = "Notes"
            )

            Spacer(Modifier.height(128.dp))

        }

    }

}