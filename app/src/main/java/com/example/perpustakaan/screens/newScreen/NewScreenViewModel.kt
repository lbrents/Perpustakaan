package com.example.perpustakaan.screens.newScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.perpustakaan.database.Series
import com.example.perpustakaan.database.SeriesDao
import com.example.perpustakaan.enums.AuthorStatus
import com.example.perpustakaan.enums.BookType
import com.example.perpustakaan.enums.Language
import com.example.perpustakaan.enums.Priority
import com.example.perpustakaan.enums.UserStatus
import com.example.perpustakaan.screens.StateManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewScreenViewModel(
    private val seriesDao: SeriesDao
) : ViewModel() {

    /* ----------------------------------------------------------------------------------------- */
    // STATE

    val isOnWishlist = StateManager(false)

    val titleEn      = StateManager("")
    val titleJp      = StateManager("")
    val description  = StateManager("")
    val bookType     = StateManager(BookType.MANGA)
    val authorStatus = StateManager(AuthorStatus.IN_PRODUCTION)
    val totalVolumes = StateManager("")

    val userStatus   = StateManager(UserStatus.READING)
    val volumesOwned = StateManager("1")
    val priority     = StateManager(Priority.NIL)
    val rating       = StateManager(0)
    val notes        = StateManager("")

    val titlesEdited = StateManager(false)

    /* ----------------------------------------------------------------------------------------- */
    // LOGIC

    /**
     * Simply creates a series object using the current state of the view model.
     */
    private fun createSeries(): Series {
        return Series(
            uid = 0,
            primaryLanguage = if (titleEn.value.isNotBlank()) Language.ENGLISH else Language.JAPANESE,
            titleEn = titleEn.value,
            titleJp = titleJp.value,
            description = description.value,
            bookType = bookType.value,
            authorStatus = authorStatus.value,
            totalVolumes = totalVolumes.value,
            isOnWishlist = isOnWishlist.value,
            userStatus = userStatus.value,
            volumesOwned = volumesOwned.value,
            priority = priority.value,
            rating = rating.value,
            notes = notes.value
        )
    }

    /**
     * Creates a series object and adds it to the database.
     *
     * Should only be called when completing the series creation process.
     */
    fun finishAddingSeries() = viewModelScope.launch(Dispatchers.IO) {
        seriesDao.insert(createSeries())
    }

    /**
     * Performed when the screen is initially loaded, initializes the state of the entire screen.
     */
    fun initialize() {
        isOnWishlist upd false
        titleEn      upd ""
        titleJp      upd ""
        description  upd ""
        bookType     upd BookType.MANGA
        authorStatus upd AuthorStatus.IN_PRODUCTION
        totalVolumes upd ""
        userStatus   upd UserStatus.READING
        volumesOwned upd "1"
        priority     upd Priority.NIL
        rating       upd 0
        notes        upd ""
        titlesEdited upd false
    }

    /* ----------------------------------------------------------------------------------------- */
    // COMPANION

    companion object {

        @Suppress("UNCHECKED_CAST")
        class Factory(
            private val seriesDao: SeriesDao
        ) : ViewModelProvider.Factory {
            override fun <T: ViewModel> create(modelClass: Class<T>): T {
                return NewScreenViewModel(seriesDao) as T
            }
        }

    }

}