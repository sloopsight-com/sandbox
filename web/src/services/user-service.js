import axios from 'axios'
import store from '../store'

const HTTP = axios.create({
    baseURL: `/api`,
    timeout: 1000,
    headers: {
        "Authorization": "Bearer " + store.getters.getToken
    }
});

export default {
    get(_page, _limit) {
        return HTTP.get('v1/user', {
            params: {
                page: _page,
                size: _limit
            }
        });
    },
    getOne(id) {
        return HTTP.get('v1/user/' + id);
    },
    getAvailableRole(id) {
        return HTTP.get('v1/user/' + id + '/roles/available');
    },
    update(id, user) {
        return HTTP.put('/v1/user/' + id, user);
    },
    save(user) {
        return HTTP.post(`/v1/user`, user);
    }

}

