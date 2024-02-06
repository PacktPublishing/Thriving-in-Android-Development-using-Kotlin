package com.packt.stories.di

import com.packt.stories.domain.SaveCaptureUseCase
import com.packt.stories.ui.editor.StoryEditorViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.factory
import org.koin.dsl.module

val storyModule = module {
    viewModel<StoryEditorViewModel>()
    factoryOf(::SaveCaptureUseCase)
}