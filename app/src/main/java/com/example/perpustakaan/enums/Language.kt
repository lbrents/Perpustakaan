package com.example.perpustakaan.enums

enum class Language(
    val label: String
) {

    ENGLISH("English"),
    JAPANESE("Japanese");

    companion object {

        /**
         * Converts the extended int to its corresponding enum object.
         *
         * If the extended int does not match any of the enum's ordinals,
         * returns the default: ENGLISH.
         */
        @Suppress("KotlinConstantConditions") // The IDE kept throwing warnings for this
        fun Int.toLanguage(): Language {
            return entries.firstOrNull { it.ordinal == this } ?: ENGLISH
        }

        /**
         * Converts the extended string to its corresponding enum object.
         *
         * If the extended string does not match any of the enum's labels,
         * returns the default: ENGLISH.
         */
        fun String.toLanguage(): Language {
            return entries.firstOrNull { it.label == this } ?: ENGLISH
        }

        /**
         * Returns a list of each entry's label in order.
         */
        val labels = entries.map { it.label }

    }

}