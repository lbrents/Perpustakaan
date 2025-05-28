package com.example.perpustakaan.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector

enum class UserStatus(
    val label: String,
    val icon: ImageVector
) {

    READING("Reading", Icons.Default.Favorite),
    WAITING("Waiting", Icons.Default.Notifications),
    COMPLETED("Completed", Icons.Default.CheckCircle),
    PLANNING("Planning", Icons.Default.FavoriteBorder),
    UNSORTED("Unsorted", Icons.Default.Warning),
    UNINTERESTED("Uninterested", Icons.Default.Close);

    companion object {

        /**
         * Converts the extended int to its corresponding enum object.
         *
         * If the extended int does not match any of the enum's ordinals,
         * returns the default: READING.
         */
        @Suppress("KotlinConstantConditions") // The IDE kept throwing warnings for this
        fun Int.toUserStatus(): UserStatus {
            return entries.firstOrNull { it.ordinal == this } ?: READING
        }

        /**
         * Converts the extended string to its corresponding enum object.
         *
         * If the extended string does not match any of the enum's labels,
         * returns the default: IN_PRODUCTION.
         */
        fun String.toUserStatus(): UserStatus {
            return entries.firstOrNull { it.label == this } ?: READING
        }

        /**
         * Returns a list of each entry's label in order.
         */
        val labels = entries.map { it.label }

    }

}