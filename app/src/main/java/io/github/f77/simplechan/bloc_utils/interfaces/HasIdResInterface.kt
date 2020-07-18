package io.github.f77.simplechan.bloc_utils.interfaces

import androidx.annotation.IdRes

interface HasIdResInterface {
    @get:IdRes
    val id: Int
}
