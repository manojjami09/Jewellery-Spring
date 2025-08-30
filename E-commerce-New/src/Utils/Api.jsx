import axios from 'axios';
import Cookies from 'js-cookie';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true, // ✅ needed if sending cookies cross-origin
});

// Adding token from cookie
api.interceptors.request.use((config) => {
  const token = Cookies.get('token');  // ✅ cookie instead of localStorage
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
