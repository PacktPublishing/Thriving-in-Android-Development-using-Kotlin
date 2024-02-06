package com.packt.stories.ui.editor

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StoryEditorViewModel(): ViewModel() {

    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing

}
