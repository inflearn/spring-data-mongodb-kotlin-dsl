package com.github.inflab.spring.data.mongodb.core

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeTrue

class LibraryTest : FreeSpec({

    "someLibraryMethod should return 'true'" {
        val classUnderTest = Library()

        classUnderTest.someLibraryMethod().shouldBeTrue()
    }
})
