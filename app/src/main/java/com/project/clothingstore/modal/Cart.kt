data class Variant(
    val color: String = "",
    val size: String = ""
)

data class CartItem(
    val productId: String = "",
    val productName: String = "",
    val variant: Variant = Variant(),
    var quantity: Int = 0,
    val price: Int = 0,
    var isSelected: Boolean = false // chỉ để UI chọn sp
)

data class Cart(
    val cartId: String = "",
    val cartItems: List<CartItem> = emptyList()
)
