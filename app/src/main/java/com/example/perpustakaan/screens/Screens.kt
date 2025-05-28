package com.example.perpustakaan.screens

import kotlinx.serialization.Serializable

/**
 * Sealed class that with each of its children representing a screen to navigate to as well
 * as including any arguments necessary for navigation.
 */
sealed class Screens {

    @Serializable
    data object Home : Screens()

    @Serializable
    data object New : Screens()

    @Serializable
    data class Edit(
        val uid: Int
    ) : Screens()

}