package org.example.dyds_proyecto2_ramones.presentation.common

import kotlin.test.Test
import kotlin.test.assertEquals

class HoursFormatterTest {
    @Test
    fun `formatHoursOneDecimal rounds and keeps one decimal`() {
        assertEquals("123.5", 123.456789.formatHoursOneDecimal())
        assertEquals("123.4", 123.44.formatHoursOneDecimal())
        assertEquals("0.0", 0.0.formatHoursOneDecimal())
        assertEquals("10.0", 10.0.formatHoursOneDecimal())
    }
}

