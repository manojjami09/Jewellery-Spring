import React, { useState, useEffect } from "react";
import { BrowserRouter, Routes, Route } from "react-router";
import axios from "axios";
import Cookies from "js-cookie";
import jwtDecode from "jwt-decode";

import Home from "./components/Home";
import Shop from "./components/Shop";
import ProductItem from "./components/ProductItem";
import NewArrivalItem from "./components/NewArrivalItem";
import LoginForm from "./components/LoginForm";
import SignupForm from "./components/SignupForm";
import Cart from "./components/Cart";
import ProtectedRoute from "./components/ProtectedRoute";
import CartContext from "./context/CartContext";

export default function App() {
  const [cartList, setCartList] = useState([]);
  const [userId, setUserId] = useState(null);
  const [user, setUser] = useState(null);

  // Axios instance
  const api = axios.create({
    baseURL: "http://localhost:8080/api/cart",
    withCredentials: true,
  });

  api.interceptors.request.use((config) => {
    const token = Cookies.get("jwt_token");
    if (token) config.headers.Authorization = `Bearer ${token}`;
    return config;
  });

  // Decode JWT on mount
  useEffect(() => {
      const token = Cookies.get("jwt_token");
      if (!token) return;

      try {
        const decoded = jwtDecode(token);

        // 👇 assuming your JWT payload looks like:
        // { userId: 1, username: "Manoj", email: "manoj@gmail.com", ... }
        if (decoded.userId) {
          setUserId(decoded.userId);
          setUser({ username: decoded.username, email: decoded.email }); // ✅ store full user
        }
      } catch (err) {
        console.error("Invalid JWT token:", err);
      }
    }, []);

  // Clear cart when userId changes
  useEffect(() => {
    setCartList([]); // remove previous user's cart immediately
  }, [userId]);

  // Add item to cart
  const addCartItem = (product) => {
    if (!userId) return;

    api.post(`/${userId}/add`, { productId: product.id, quantity: product.quantity || 1 })
      .then(res => setCartList(res.data.items || []))
      .catch(err => console.error("Error adding item:", err));
  };

  // Delete item from cart
  const deleteCartItem = async (productId) => {
    if (!userId) return;

    try {
      await api.delete(`/${userId}/remove/${productId}`);
      const res = await api.get(`/${userId}`);
      setCartList(res.data.items || []);
    } catch (err) {
      console.error("Error deleting item:", err);
    }
  };

  // Clear cart
  const clearCart = () => {
    if (!userId) return;

    api.delete(`/${userId}/clear`)
      .then(() => setCartList([]))
      .catch(err => console.error("Error clearing cart:", err));
  };

  //update cart
  const updateQuantity = (cartItemId, newQuantity) => {
    if (!userId) return;

    api.put(`/${userId}/update/${cartItemId}?quantity=${newQuantity}`)
      .then((res) => setCartList(res.data.items || [])) // ✅ update state
      .catch((err) => console.error("Error updating quantity:", err));
  };

  return (
    <CartContext.Provider value={{ cartList, setCartList, addCartItem, deleteCartItem, clearCart, userId, setUserId , updateQuantity}}>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<LoginForm />} />
          <Route path="/signup" element={<SignupForm />} />

          <Route
            path="/cart"
            element={
              <ProtectedRoute>
                <Cart />
              </ProtectedRoute>
            }
          />
          <Route
            path="/"
            element={
              <ProtectedRoute>
                <Home />
              </ProtectedRoute>
            }
          />
          <Route
            path="/products"
            element={
              <ProtectedRoute>
                <Shop />
              </ProtectedRoute>
            }
          />
          <Route
            path="/products/:id"
            element={
              <ProtectedRoute>
                <ProductItem />
              </ProtectedRoute>
            }
          />
          <Route
            path="/newArrivals/:id"
            element={
              <ProtectedRoute>
                <NewArrivalItem />
              </ProtectedRoute>
            }
          />
        </Routes>
      </BrowserRouter>
    </CartContext.Provider>
  );
}
