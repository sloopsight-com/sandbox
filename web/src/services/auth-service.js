import axios from 'axios'
import store from '../store'

const HTTP = axios.create({
    baseURL: `/api`,
    timeout: 60000,
    headers: {
        "Authorization": "Bearer " + store.getters.getToken
    }
});

export default {
    getConfig(name) {
        return HTTP.get('/auth/config/' + name);
    },
    updateConfig(name, config) {
        return HTTP.put('/auth/config/' + name, config);
    }
}

