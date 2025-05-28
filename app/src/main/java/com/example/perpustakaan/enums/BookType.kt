package com.example.perpustakaan.enums

enum class BookType(
    val label: String
) {

    MANGA("Manga"),
    LIGHT_NOVEL("Light Novel");

    companion object {

        /**
         * Converts the extended int to its corresponding enum object.
         *
         * If the extended int does not match any of the enum's ordinals,
         * returns the default: IN_PRODUCTION.
         */
        @Suppress("KotlinConstantConditions") // The IDE kept throwing warnings for this
        fun Int.toBookType(): BookType {
            return entries.firstOrNull { it.ordinal == this } ?: MANGA
        }

        /**
         * Converts the extended string to its corresponding enum object.
         *
         * If the extended string does not match any of the enum's labels,
         * returns the default: IN_PRODUCTION.
         */
        fun String.toBookType(): BookType {
            return entries.firstOrNull { it.label == this } ?: MANGA
        }

        /**
         * Returns a list of each entry's label in order.
         */
        val labels = entries.map { it.label }

    }

}