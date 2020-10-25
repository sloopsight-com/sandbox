import HTTP from './common-client'

export default {
    getConfig(name) {
        return HTTP.get('/auth/config/' + name);
    },
    updateConfig(name, config) {
        return HTTP.put('/auth/config/' + name, config);
    }
}

