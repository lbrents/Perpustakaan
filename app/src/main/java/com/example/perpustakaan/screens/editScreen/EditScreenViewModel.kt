package com.example.perpustakaan.screens.editScreen

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

class EditScreenViewModel(
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

    /* ----------------------------------------------------------------------------------------- */
    // LOGIC

    /**
     * Given a uid, fetches the corresponding series object from the database.
     */
    private suspend fun fetchSeries(uid: Int): Series {
        return seriesDao.fetch(uid)
    }

    /**
     * Given a uid, updates the corresponding series in the database with the view model's state.
     */
    fun updateSeries(uid: Int, primaryLanguage: Language) = viewModelScope.launch(Dispatchers.IO) {
        seriesDao.update(Series(
            uid             = uid,
            primaryLanguage = primaryLanguage,
            titleEn         = titleEn.value,
            titleJp         = titleJp.value,
            description     = description.value,
            bookType        = bookType.value,
            authorStatus    = authorStatus.value,
            totalVolumes    = totalVolumes.value,
            isOnWishlist    = isOnWishlist.value,
            userStatus      = userStatus.value,
            volumesOwned    = volumesOwned.value,
            priority        = priority.value,
            rating          = rating.value,
            notes           = notes.value
        ))
    }

    /**
     * Performed when the screen is initially loaded, initializes the state of the entire screen.
     */
    fun initialize(uid: Int) = viewModelScope.launch(Dispatchers.IO) {
        val series = fetchSeries(uid)
        isOnWishlist upd series.isOnWishlist
        titleEn      upd series.titleEn
        titleJp      upd series.titleJp
        description  upd series.description
        bookType     upd series.bookType
        authorStatus upd series.authorStatus
        totalVolumes upd series.totalVolumes
        userStatus   upd series.userStatus
        volumesOwned upd series.volumesOwned
        priority     upd series.priority
        rating       upd series.rating
        notes        upd series.notes
    }

    /* ----------------------------------------------------------------------------------------- */
    // COMPANION

    companion object {

        @Suppress("UNCHECKED_CAST")
        class Factory(
            private val seriesDao: SeriesDao
        ) : ViewModelProvider.Factory {
            override fun <T: ViewModel> create(modelClass: Class<T>): T {
                return EditScreenViewModel(seriesDao) as T
            }
        }

    }

}