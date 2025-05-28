package com.example.perpustakaan.enums

enum class Priority(
    val label: String
) {

    TOP("Top"),
    MID("Mid"),
    LOW("Low"),
    NIL("Nil");

    companion object {

        /**
         * Converts the extended int to its corresponding enum object.
         *
         * If the extended int does not match any of the enum's ordinals,
         * returns the default: NIL.
         */
        @Suppress("KotlinConstantConditions") // The IDE kept throwing warnings for this
        fun Int.toPriority(): Priority {
            return entries.firstOrNull { it.ordinal == this } ?: NIL
        }

        /**
         * Converts the extended string to its corresponding enum object.
         *
         * If the extended string does not match any of the enum's labels,
         * returns the default: NIL.
         */
        fun String.toPriority(): Priority {
            return entries.firstOrNull { it.label == this } ?: NIL
        }

        /**
         * Returns a list of each entry's label in order.
         */
        val labels = entries.map { it.label }

    }

}