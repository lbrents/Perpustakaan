package com.example.perpustakaan.enums

enum class AuthorStatus(
    val label: String
) {

    IN_PRODUCTION("Ongoing"),
    COMPLETED("Completed"),
    ON_HIATUS("Hiatus");

    companion object {

        /**
         * Converts the extended int to an AuthorStatus object.
         *
         * If the extended int does not match any of the AuthorStatus ordinals,
         * returns the default: IN_PRODUCTION.
         */
        @Suppress("KotlinConstantConditions") // The IDE kept throwing warnings for this
        fun Int.toAuthorStatus(): AuthorStatus {
            return entries.firstOrNull { it.ordinal == this } ?: IN_PRODUCTION
        }

        /**
         * Converts the extended string to an AuthorStatus object.
         *
         * If the extended string does not match any of the AuthorStatus labels,
         * returns the default: IN_PRODUCTION.
         */
        fun String.toAuthorStatus(): AuthorStatus {
            return entries.firstOrNull { it.label == this } ?: IN_PRODUCTION
        }

        /**
         * Returns a list of each entry's label in order.
         */
        val labels = entries.map { it.label }

    }

}