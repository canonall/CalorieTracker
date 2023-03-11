package com.canonal.core.domain.use_case

class FilterOutDigitsUseCase {
    operator fun invoke(text: String): String {
        return text.filter { character ->
            character.isDigit()
        }
    }
}
