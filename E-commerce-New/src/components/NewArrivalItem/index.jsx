// src/pages/NewArrivalItem/index.jsx
import { useState, useEffect, useContext } from "react";
import { useParams } from "react-router";
import Navbar from "../../components/Navbar";
import CartContext from "../../context/CartContext";
import { BsPlusSquare, BsDashSquare } from "react-icons/bs";
import Cookies from "js-cookie";   // ✅ import Cookies
import "./index.css";

const NewArrivalItem = () => {
  const [product, setProduct] = useState(null);
  const { id } = useParams();
  const [quantity, setQuantity] = useState(1);
  const [isAdded, setIsAdded] = useState(false);
  const { addCartItem } = useContext(CartContext);

  const onClickAddToCart = () => {
    addCartItem({ ...product, quantity });
    setIsAdded(true);
  };

  useEffect(() => {
    const fetchProduct = async () => {
      try {
        const token = Cookies.get("jwt_token"); // ✅ read JWT from cookies

        const response = await fetch(`http://localhost:8080/api/newArrivals/${id}`, {
          headers: {
            Authorization: `Bearer ${token}`,  // ✅ attach token
            "Content-Type": "application/json"
          },
        });

        if (!response.ok) throw new Error("Failed to fetch new arrival");
        const data = await response.json();
        setProduct(data);
      } catch (error) {
        console.error("Error fetching new arrival:", error);
      }
    };

    fetchProduct();
  }, [id]);

  if (!product) {
    return (
      <div className="loading-container">
        <p>Loading new arrival...</p>
      </div>
    );
  }

  const onDecrementQuantity = () => {
    setQuantity((prev) => (prev > 1 ? prev - 1 : prev));
  };

  const onIncrementQuantity = () => {
    setQuantity((prev) => prev + 1);
  };

  const { name, price, image } = product;

  return (
    <>
      <Navbar />
      <div className="product-item-container">
        <div className="product-item">
          <img src={image} alt={name} className="product-image" />
          <div className="product-details">
            <h2 className="product-name">{name}</h2>
            <p className="price-tag">₹ {price}</p>
            <p className="product-tax">Inclusive of all taxes</p>

            <div className="quantity-container">
              <button
                type="button"
                className="quantity-controller-button"
                onClick={onDecrementQuantity}
              >
                <BsDashSquare className="quantity-controller-icon" />
              </button>
              <p className="quantity">{quantity}</p>
              <button
                type="button"
                className="quantity-controller-button"
                onClick={onIncrementQuantity}
              >
                <BsPlusSquare className="quantity-controller-icon" />
              </button>
            </div>

            <hr />
            <p className="product-offer">
              Buy 3 at 3003 Use Code : MID3003 at checkout. <br />
              Buy 1 Get 1 Free Use Code : B1G1 at checkout.
            </p>
            <hr />
            <p className="product-stock">In stock - ready to ship</p>
            <button className="add-to-cart-btn" onClick={onClickAddToCart}>
              {isAdded ? "Added to Cart" : "Add to Cart"}
            </button>
          </div>
        </div>
      </div>
    </>
  );
};

export default NewArrivalItem;
