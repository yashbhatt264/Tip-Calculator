package com.yb.tipcalculator.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat

fun commaFilter(text: AnnotatedString): TransformedText {
    val hasDecimal = (text.text.contains("."))
    val formatString = if (hasDecimal) text.text.split(".")[0] else text.text
    val length = formatString.length

    val annotatedString = AnnotatedString.Builder().run {
        for (i in formatString.indices) {
            if (length > 3 && (i % 3) == (length % 3) && i > 0) {
                append(",")
            }
            append(text.text[i])
        }
        if (hasDecimal) {
            append(".${text.text.split(".")[1]}")
        }
        toAnnotatedString()
    }

    val translator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return if (offset <= 3) {
                offset
            } else {
                (offset) + ((length - 1) / 3)
            }
        }

        override fun transformedToOriginal(offset: Int): Int {
            return if (offset <= 3) {
                offset
            } else {
                offset - ((length - 1) / 3)
            }
        }
    }

    return TransformedText(annotatedString, translator)
}

class EndSuffixTransformation(val suffix: String) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val result = text + AnnotatedString(suffix)

        val textWithSuffixMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return offset
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (text.isEmpty()) return 0
                if (offset >= text.length) return text.length
                return offset
            }
        }

        return TransformedText(result, textWithSuffixMapping)
    }
}