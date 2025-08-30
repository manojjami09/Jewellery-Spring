import { BsPlusSquare, BsDashSquare } from 'react-icons/bs';
import { AiFillCloseCircle } from 'react-icons/ai';
import { useContext } from 'react';
import CartContext from '../../context/CartContext';

import './index.css';

const CartItem = (props) => {
  const { cartItemDetails } = props;
  const { id, product, quantity } = cartItemDetails; // id = cartItem id
  const { id: productId, image, name, price } = product; // product fields

  const { deleteCartItem, updateQuantity } = useContext(CartContext);

  const handleDecrease = () => {
    if (quantity > 1) {
      updateQuantity(id, quantity - 1);  // decrement
    } else {
      deleteCartItem(id);  // remove if only 1 left
    }
  };

  const handleIncrease = () => {
    updateQuantity(id, quantity + 1);  // increment
  };

  return (
    <li className="cart-item">
      <img className="cart-product-image" src={image} alt={name} />
      <div className="cart-item-details-container">
        <div className="cart-product-title-brand-container">
          <p className="cart-product-title">{name}</p>
          {/* Remove brand if not available */}
        </div>

        {/* Quantity Controls */}
        <div className="cart-quantity-container">
          <button
            type="button"
            className="quantity-controller-button"
            onClick={handleDecrease}
          >
            <BsDashSquare color="#52606D" size={12} />
          </button>
          <p className="cart-quantity">{quantity}</p>
          <button
            type="button"
            className="quantity-controller-button"
            onClick={handleIncrease}
          >
            <BsPlusSquare color="#52606D" size={12} />
          </button>
        </div>

        {/* Price + Remove Button */}
        <div className="total-price-delete-container">
          <p className="cart-total-price">Rs {price * quantity}/-</p>
          <button
            className="remove-button"
            type="button"
            onClick={() => deleteCartItem(id)} // pass cartItem id
          >
            Remove
          </button>
        </div>
      </div>

      {/* Icon remove */}
      <button
        className="delete-button"
        type="button"
        onClick={() => deleteCartItem(id)}
      >
        <AiFillCloseCircle color="#616E7C" size={20} />
      </button>
    </li>
  );
};

export default CartItem;
