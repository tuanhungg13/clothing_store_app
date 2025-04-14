import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Variant(
    val color: String = "",
    val size: String = ""
):Parcelable
@Parcelize
data class CartItem(
    val productId: String = "",
    val productName: String = "",
    val variant: Variant = Variant(),
    val image: String = "",
    var quantity: Int = 0,
    val price: Int = 0,
    var isSelected: Boolean = false, // chỉ để UI chọn sp
    var stock: Int = 0 // chỉ để UI chọn sp
) : Parcelable

data class Cart(
    val cartId: String = "",
    val cartItems: List<CartItem> = emptyList()
)
