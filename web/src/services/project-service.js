import axios from 'axios'
import store from '../store'
import Vue from 'vue'

const HTTP = axios.create({
    baseURL: `/api`,
    timeout: 60000,
    headers: {
        "Authorization": "Bearer " + store.getters.getToken
    }
});

export default {

    get(_page, _limit) {

        Vue.$log.info("Calling with client with 60 sec timeout");
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

    update(id, name, openApiSpec, _description, _members) {
        return HTTP.put('/v1/project/' + id, {
            name: name,
            openApiSpec: openApiSpec,
            description: _description,
            members: _members
        });
    },


    getExistingMember(id) {
        return HTTP.get('/v1/project/' + id + '/existing/members');
    },


    getAvailableMember(id) {
        return HTTP.get('/v1/project/' + id + '/available/members');
    },


    save(name, openApiSpec, _description, _members) {
        return HTTP.post(`/v1/project`, {
            name: name,
            openApiSpec: openApiSpec,
            description: _description,
            members: _members
        });
    }
}

