import axios from "axios";
import store from "../store";
let axiosConnection = axios.create({
  baseURL: `api`,
  timeout: 60000,
  headers: {
    Authorization: "Bearer " + store.getters.getToken
  }
});

axiosConnection.interceptors.request.use(config => {
  // We are importing store before it is populated.
  // We intercept the request and use the current apiKey
  config.headers = { Authorization: "Bearer " + store.getters.getToken };
  return config;
});

export default axiosConnection;
