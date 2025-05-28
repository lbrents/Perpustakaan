package com.example.perpustakaan.database

import androidx.room.TypeConverter
import com.example.perpustakaan.enums.AuthorStatus
import com.example.perpustakaan.enums.AuthorStatus.Companion.toAuthorStatus
import com.example.perpustakaan.enums.BookType
import com.example.perpustakaan.enums.BookType.Companion.toBookType
import com.example.perpustakaan.enums.Language
import com.example.perpustakaan.enums.Language.Companion.toLanguage
import com.example.perpustakaan.enums.Priority
import com.example.perpustakaan.enums.Priority.Companion.toPriority
import com.example.perpustakaan.enums.UserStatus
import com.example.perpustakaan.enums.UserStatus.Companion.toUserStatus

class SeriesTypeConverters {

    @TypeConverter
    fun authorStatusToOrdinal(state: AuthorStatus) = state.ordinal
    @TypeConverter
    fun ordinalToAuthorStatus(ordinal: Int) = ordinal.toAuthorStatus()

    @TypeConverter
    fun bookTypeToOrdinal(state: BookType) = state.ordinal
    @TypeConverter
    fun ordinalToBookType(ordinal: Int) = ordinal.toBookType()

    @TypeConverter
    fun languageToOrdinal(state: Language) = state.ordinal
    @TypeConverter
    fun ordinalToLanguage(ordinal: Int) = ordinal.toLanguage()

    @TypeConverter
    fun priorityToOrdinal(state: Priority) = state.ordinal
    @TypeConverter
    fun ordinalToPriority(ordinal: Int) = ordinal.toPriority()

    @TypeConverter
    fun userStatusToOrdinal(state: UserStatus) = state.ordinal
    @TypeConverter
    fun ordinalToUserStatus(ordinal: Int) = ordinal.toUserStatus()

}