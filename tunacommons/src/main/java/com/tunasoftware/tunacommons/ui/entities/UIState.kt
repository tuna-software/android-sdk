package com.tunasoftware.tunacommons.ui.entities

sealed class UIState<out T> {
    object Loading:UIState<Nothing>()
    class Error(val error: Throwable):UIState<Nothing>()
    class Success<T>(val result: T): UIState<T>()
}