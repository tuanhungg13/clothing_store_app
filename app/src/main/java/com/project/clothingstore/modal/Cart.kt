data class Cart(
    val cartId: String,  // ID của giỏ hàng, sử dụng String thay vì ObjectId
    val cartItems: List<CartItem>  // Danh sách các sản phẩm trong giỏ hàng
)

data class CartItem(
    val productId: String,  // ID của sản phẩm
    val productName: String,  // Tên sản phẩm
    val variant: Variant,  // Mô tả biến thể của sản phẩm (màu sắc, kích thước)
    var quantity: Int,  // Số lượng sản phẩm
    val price: Int  // Giá của sản phẩm
)

data class Variant(
    val color: String,  // Màu sắc của sản phẩm
    val size: String  // Kích thước của sản phẩm
)
