import HTTP from './common-client'


export default {
    get(id, _page, _limit) {
        return HTTP.get('v1/project/' + id + "/endpoint", {
            params: {
                page: _page,
                size: _limit
            }
        });
    },

    delete(id) {
        return HTTP.delete('v1/endpoint/' + id);
    },

    getOne(id) {
        return HTTP.get('v1/endpoint/' + id);
    },

    getHint() {
        return HTTP.get('v1/hint');
    },


    update(id, _logic) {
        return HTTP.put('/v1/endpoint/' + id + "/logic", { logic: _logic });
    }

}