package com.example.perpustakaan.enums

enum class SortType(
    val label: String
) {

    UID("Default"),
    AUTHOR_STATUS("Author Status"),
    USER_STATUS("User Status"),
    PRIORITY("Priority"),
    RATING("Rating");

    companion object {

        /**
         * Converts the extended int to its corresponding enum object.
         *
         * If the extended int does not match any of the enum's ordinals,
         * returns the default: UID.
         */
        @Suppress("KotlinConstantConditions") // The IDE kept throwing warnings for this
        fun Int.toSortType(): SortType {
            return entries.firstOrNull { it.ordinal == this } ?: UID
        }

        /**
         * Converts the extended string to its corresponding enum object.
         *
         * If the extended string does not match any of the enum's labels,
         * returns the default: UID.
         */
        fun String.toSortType(): SortType {
            return entries.firstOrNull { it.label == this } ?: UID
        }

        /**
         * Returns a list of each entry's label in order.
         */
        val labels = entries.map { it.label }

    }

}