import CartItem from "../CartItem";
import "./index.css";

const CartListView = ({ cartItems, onDeleteItem, onClearCart, onOrderPlaced }) => {
  const getCartTotal = () =>
    cartItems.reduce(
      (acc, item) => acc + item.product.price * item.quantity,
      0
    );

  return (
    <div className="cart-list-view-container">
      <div>
        <ul className="cart-list">
          {cartItems.map((eachCartItem) => (
            <CartItem
              key={eachCartItem.id}
              cartItemDetails={eachCartItem}
              onDeleteItem={onDeleteItem}
            />
          ))}
        </ul>
      </div>
      <div className="cart-total-container">
        <h3>Total: ₹{getCartTotal()}</h3>
        <button className="checkout-btn" onClick={onOrderPlaced}>
          Buy Now
        </button>
      </div>
    </div>
  );
};

export default CartListView;
