package com.example.perpustakaan.screens.homeScreen

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.perpustakaan.database.SeriesDao
import com.example.perpustakaan.database.DisplaySeries
import com.example.perpustakaan.enums.AuthorStatus
import com.example.perpustakaan.enums.AuthorStatus.Companion.toAuthorStatus
import com.example.perpustakaan.enums.BookType
import com.example.perpustakaan.enums.BookType.Companion.toBookType
import com.example.perpustakaan.enums.Language
import com.example.perpustakaan.enums.LibraryOrWishlist
import com.example.perpustakaan.enums.LibraryOrWishlist.Companion.toLibraryOrWishlist
import com.example.perpustakaan.enums.Priority
import com.example.perpustakaan.enums.Priority.Companion.toPriority
import com.example.perpustakaan.enums.SortType
import com.example.perpustakaan.enums.UserStatus
import com.example.perpustakaan.enums.UserStatus.Companion.toUserStatus
import com.example.perpustakaan.screens.StateManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val seriesDao: SeriesDao
) : ViewModel() {

    /* ----------------------------------------------------------------------------------------- */
    // STATE

    val searchExpanded      = StateManager(false)
    val searchText          = StateManager("")
    val menuExpanded        = StateManager(false)
    val deleteDialogFocused = StateManager(false)

    val seriesList          = StateManager(listOf<DisplaySeries>())
    val seriesOffset        = StateManager(0)
    val seriesQuery         = StateManager("")
    val focusedSeriesUid    = StateManager(-1)
    val isLoading           = StateManager(false)

    // Sort and filter states
    val selectedSort        = StateManager(SortType.UID)
    val wishlistFilter      = StateManager(LibraryOrWishlist.labels)
    val bookTypeFilter      = StateManager(BookType.labels)
    val authorStatusFilter  = StateManager(AuthorStatus.labels)
    val userStatusFilter    = StateManager(UserStatus.labels)
    val priorityFilter      = StateManager(Priority.labels)
    val ratingFilter        = StateManager(0..10)

    /* ----------------------------------------------------------------------------------------- */
    // LOGIC

    /**
     * Shows a notification toast with the given message.
     */
    fun notifyUser(snackbar: SnackbarHostState, message: String) = viewModelScope.launch {
        snackbar.showSnackbar(message)
    }

    /**
     * Finds the series title stored in the view model using the given uid.
     * Additionally, does not search the database. Just whatever is already loaded.
     *
     * If no title is found using the associated uid, the string "null" is returned.
     */
    fun findSeriesTitle(uid: Int): String {
        return seriesList.state.value.firstOrNull { it.uid == uid }?.title ?: "null"
    }

    /**
     * Using the current state of the view model, generates a sql query to fetch a limited number
     * of sorted and filters displaySeries objects at a specified offset.
     */
    private fun genSqlQueryString(): String {

        val where = listOf(
            // WISHLIST FILTER
            (if (wishlistFilter.value.isEmpty()) "" else {
                "isOnWishlist IN (" + wishlistFilter.value.joinToString(", ") {
                    it.toLibraryOrWishlist().ordinal.toString()
                } + ")"
            }),
            // BOOK TYPE FILTER
            (if (bookTypeFilter.value.isEmpty()) "" else {
                "bookType IN (" + bookTypeFilter.value.joinToString(", ") {
                    it.toBookType().ordinal.toString()
                } + ")"
            }),
            // AUTHOR STATUS FILTER
            (if (authorStatusFilter.value.isEmpty()) "" else {
                "authorStatus IN (" + authorStatusFilter.value.joinToString(", ") {
                    it.toAuthorStatus().ordinal.toString()
                } + ")"
            }),
            // USER STATUS FILTER
            (if (userStatusFilter.value.isEmpty()) "" else {
                "userStatus IN (" + userStatusFilter.value.joinToString(", ") {
                    it.toUserStatus().ordinal.toString()
                } + ")"
            }),
            // PRIORITY FILTER
            (if (priorityFilter.value.isEmpty()) "" else {
                "priority IN (" + priorityFilter.value.joinToString(", ") {
                    it.toPriority().ordinal.toString()
                } + ")"
            }),
            // RATING FILTER
            "rating BETWEEN ${ratingFilter.value.start} AND ${ratingFilter.value.endInclusive}",
            // SEARCH TEXT FILTER
            (if (searchText.value.isBlank()) "" else {
                "(LOWER(titleEn) LIKE '%${searchText.value}%' OR LOWER(titleJp) LIKE '%${searchText.value}%')"
            })
        )
            .filter { it.isNotBlank() }
            .joinToString(" AND ")
        val order = when (selectedSort.value) {
            SortType.UID           -> "uid"
            SortType.AUTHOR_STATUS -> "authorStatus"
            SortType.USER_STATUS   -> "userStatus"
            SortType.PRIORITY      -> "priority"
            SortType.RATING        -> "rating DESC"
        }

        return """
            SELECT * FROM Series
            WHERE $where
            ORDER BY $order
            LIMIT 10 OFFSET ${seriesOffset.value}
        """.trimIndent()

    }

    /**
     * Generates the custom sql query and converts the result to displaySeries objects.
     *
     * This function is used only to fetch displaySeries based on the current view model state,
     * not specifically for any action done by the user.
     */
    private suspend fun fetchCurrentQuery() = seriesDao
        .fetch(SimpleSQLiteQuery(genSqlQueryString()))
        .map { series ->
            DisplaySeries(
                uid = series.uid,
                title = when (series.primaryLanguage) {
                    Language.ENGLISH -> series.titleEn
                    Language.JAPANESE -> series.titleJp
                },
                bookType = series.bookType,
                isOnWishlist = series.isOnWishlist,
                userStatus = series.userStatus,
                volumesOwned = series.volumesOwned
            )
        }

    /**
     * Resets the current fetch offset and reloads the displaySeries list.
     */
    private suspend fun reloadSeriesList(): List<DisplaySeries> {
        // Reset the window offset
        seriesOffset upd 0
        // Fetch and update using custom sql query
        return fetchCurrentQuery()
    }

    /**
     * Searches the database using the current view model state and automatically updates the
     * current displaySeries list.
     *
     * This function should only be called on the search action of the search bar.
     */
    fun searchDatabase() = viewModelScope.launch(Dispatchers.IO) {
        seriesList upd reloadSeriesList()
    }

    /**
     * Using the current view model state, loads the next 10 displaySeries into the list.
     *
     * This function should only be called when the list is scrolled to its end.
     */
    fun loadNext() = viewModelScope.launch(Dispatchers.IO) {
        // If the next part of the list is still loading while this function is triggered, return
        if (isLoading.value) return@launch

        isLoading upd true
        seriesOffset upd seriesOffset.value + 10

        val nextSeries = fetchCurrentQuery()
        if (nextSeries.isEmpty()) {
            seriesOffset upd seriesOffset.value - 10
            isLoading upd false
            return@launch
        }

        seriesList upd seriesList.value + nextSeries
        isLoading upd false
    }

    /**
     * Simply deletes the series in the database associated with the focusedSeriesUid field in the
     * view model. After the deletion, reloads the display list.
     */
    fun deleteFocusedFromDatabase() = viewModelScope.launch(Dispatchers.IO) {
        seriesDao.delete(focusedSeriesUid.value)
        focusedSeriesUid upd -1
        seriesList upd reloadSeriesList()
    }

    /**
     * Performed when the screen is initially loaded, initializes the state of the entire screen.
     */
    fun initialize() = viewModelScope.launch(Dispatchers.IO) {
        searchExpanded      upd false
        searchText          upd ""
        menuExpanded        upd false
        deleteDialogFocused upd false
        seriesOffset        upd 0
        seriesQuery         upd ""
        focusedSeriesUid    upd -1
        isLoading           upd false
        selectedSort        upd SortType.UID
        wishlistFilter      upd LibraryOrWishlist.labels
        bookTypeFilter      upd BookType.labels
        authorStatusFilter  upd AuthorStatus.labels
        userStatusFilter    upd UserStatus.labels
        priorityFilter      upd Priority.labels
        ratingFilter        upd 0..10
        seriesList          upd reloadSeriesList()
    }

    /* ----------------------------------------------------------------------------------------- */
    // COMPANION

    companion object {

        @Suppress("UNCHECKED_CAST")
        class Factory(
            private val seriesDao: SeriesDao
        ) : ViewModelProvider.Factory {
            override fun <T: ViewModel> create(modelClass: Class<T>): T {
                return HomeScreenViewModel(seriesDao) as T
            }
        }

    }

}