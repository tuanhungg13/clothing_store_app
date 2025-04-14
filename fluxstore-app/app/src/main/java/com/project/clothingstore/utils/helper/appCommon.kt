package com.project.clothingstore.utils.helper

fun formatPrice(amount: Number): String {
    return "%,d đ".format(amount.toLong()).replace(',', '.')
}