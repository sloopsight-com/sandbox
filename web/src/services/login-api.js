import axios from "axios";

const AXIOS = axios.create({
  baseURL: `api`,
  timeout: 60000
});

export default {
  getSecured(user, password) {
    return AXIOS.post(`/auth/signin`, {
      username: user,
      password: password
    });
  },

  getDocument(projectId) {
    return AXIOS.get(`/v1/document/${projectId}`);
  }
};
