package com.canonal.core.domain.use_case

import com.canonal.core.domain.model.Gender

class InitialHeightUseCase {
    operator fun invoke(gender: Gender): String {
        return when (gender) {
            Gender.Female -> "160"
            Gender.Male -> "180"
        }
    }
}
