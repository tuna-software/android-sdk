package com.tunasoftware.tunaui

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

val Fragment.navigator get() = findNavController()

