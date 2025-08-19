package com.splitwise.easy.util

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Calculates per-person shares for a bill.
 *
 * Inputs:
 * - subtotal, tax, tip as BigDecimal (>= 0)
 * - people: Int (> 0)
 * - weights: optional list of per-person multipliers (size == people). If null, equal split.
 *
 * Output:
 * - List of amounts (scale=2) that sums exactly to rounded total.
 */
fun calculateSplit(
    subtotal: BigDecimal,
    tax: BigDecimal = BigDecimal.ZERO,
    tip: BigDecimal = BigDecimal.ZERO,
    people: Int,
    weights: List<BigDecimal>? = null
): List<BigDecimal> {
    require(people > 0) { "people must be > 0" }
    require(subtotal >= BigDecimal.ZERO && tax >= BigDecimal.ZERO && tip >= BigDecimal.ZERO) {
        "amounts must be >= 0"
    }

    val total = subtotal.add(tax).add(tip)
    val w = weights?.also { require(it.size == people) { "weights size must equal people" } }
        ?: List(people) { BigDecimal.ONE }
    val wSum = w.fold(BigDecimal.ZERO, BigDecimal::add)
    require(wSum > BigDecimal.ZERO) { "sum of weights must be > 0" }

    // Exact shares at higher precision
    val exact = w.map { weight ->
        if (wSum.compareTo(BigDecimal.ZERO) == 0) BigDecimal.ZERO
        else total.multiply(weight).divide(wSum, 6, RoundingMode.HALF_UP)
    }

    // Round to cents
    val rounded = exact.map { it.setScale(2, RoundingMode.HALF_UP) }.toMutableList()

    // Fix rounding drift to match the rounded total exactly
    val target = total.setScale(2, RoundingMode.HALF_UP)
    val diff = target.subtract(rounded.fold(BigDecimal.ZERO, BigDecimal::add))
    if (diff.compareTo(BigDecimal.ZERO) != 0) {
        val cent = if (diff.signum() > 0) BigDecimal("0.01") else BigDecimal("-0.01")
        val steps = diff.abs().divide(BigDecimal("0.01")).toInt()
        val fracParts = exact.mapIndexed { i, v ->
            val floor = v.setScale(0, RoundingMode.DOWN)
            i to v.subtract(floor) // fractional part
        }
        val order = if (diff.signum() > 0)
            fracParts.sortedByDescending { it.second }
        else
            fracParts.sortedBy { it.second }
        repeat(steps) {
            val idx = order[it % people].first
            rounded[idx] = rounded[idx].add(cent)
        }
    }
    return rounded
}
