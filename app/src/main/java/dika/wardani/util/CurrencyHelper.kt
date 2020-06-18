package dika.wardani.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

@Suppress("MemberVisibilityCanBePrivate")
object CurrencyHelper {

    @JvmStatic
    fun toCurrency(currencySymbol: String, value: Double, groupingSeparator: Char, decimalSeparator: Char): String {
        val pattern = "$currencySymbol #,###.00"
        val numberFormat = DecimalFormat(pattern).apply {
            decimalFormatSymbols = DecimalFormatSymbols().apply {
                this.groupingSeparator = groupingSeparator
                this.decimalSeparator = decimalSeparator
            }
        }

        return  numberFormat.format(value)
    }

    @JvmStatic
    fun toIndonesiaCurrency(value: Double): String {
        return toCurrency(
            currencySymbol = "Rp",
            value = value,
            groupingSeparator = '.',
            decimalSeparator = ','
        )
    }
}