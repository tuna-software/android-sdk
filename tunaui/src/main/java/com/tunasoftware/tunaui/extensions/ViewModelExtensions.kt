package com.tunasoftware.tunaui.extensions

import android.app.Application
import androidx.lifecycle.AndroidViewModel

val AndroidViewModel.context get() = getApplication<Application>()