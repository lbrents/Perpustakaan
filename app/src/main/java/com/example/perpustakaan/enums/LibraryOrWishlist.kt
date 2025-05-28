package com.example.perpustakaan.enums

enum class LibraryOrWishlist(
    val label: String
) {

    LIBRARY("Library"),
    WISHLIST("Wishlist");

    companion object {

        /**
         * Converts the extended int to its corresponding enum object.
         *
         * If the extended int does not match any of the enum's ordinals,
         * returns the default: LIBRARY.
         */
        @Suppress("KotlinConstantConditions") // The IDE kept throwing warnings for this
        fun Int.toLibraryOrWishlist(): LibraryOrWishlist {
            return entries.firstOrNull { it.ordinal == this } ?: LIBRARY
        }

        /**
         * Converts the extended string to its corresponding enum object.
         *
         * If the extended string does not match any of the enum's labels,
         * returns the default: LIBRARY.
         */
        fun String.toLibraryOrWishlist(): LibraryOrWishlist {
            return entries.firstOrNull { it.label == this } ?: LIBRARY
        }

        /**
         * Returns a list of each entry's label in order.
         */
        val labels = entries.map { it.label }

    }

}