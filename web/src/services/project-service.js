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
        return HTTP.get('v1/project', {
            params: {
                page: _page,
                size: _limit
            }
        });
    },

    getOne(id) {
        return HTTP.get('v1/project/' + id);
    },

    getDocs(id) {
        return HTTP.get('v1/project/' + id + '/docs');
    },

    delete(id) {
        return HTTP.delete('v1/project/' + id);
    },

    update(id, name, openApiSpec, _description) {
        return HTTP.put('/v1/project/' + id, {
            name: name,
            openApiSpec: openApiSpec,
            description: _description
        });
    },

    save(name, openApiSpec, _description) {
        return HTTP.post(`/v1/project`, {
            name: name,
            openApiSpec: openApiSpec,
            description: _description

        });
    }
}

