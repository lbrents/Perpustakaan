package com.example.perpustakaan.database

import com.example.perpustakaan.enums.BookType
import com.example.perpustakaan.enums.UserStatus

data class DisplaySeries(

    val uid: Int, // Identifier, not for display
    val title: String,
    val bookType: BookType,
    val isOnWishlist: Boolean,
    val userStatus: UserStatus,
    val volumesOwned: String

)
