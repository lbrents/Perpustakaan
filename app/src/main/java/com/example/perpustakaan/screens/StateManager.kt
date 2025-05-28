package com.example.perpustakaan.screens

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Constructs and maintains a MutableStateFlow. Updating is done using the infix upd function.
 */
class StateManager<T>(initialValue: T) {

    private val _state = MutableStateFlow(initialValue)
    val state = _state.asStateFlow()

    val value: T
        get() = state.value

    infix fun upd(value: T) { _state.update { value } }

}