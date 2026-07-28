import axios from "axios";

const api = axios.create({
    baseUrl: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/freightanddelivery/api',
    headers: {'Content-Type': 'application/json'},
    timeout: 1000
})

export default api