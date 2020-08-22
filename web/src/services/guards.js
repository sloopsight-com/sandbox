import axios from 'axios'

const HTTP = axios.create({
    baseURL: `/api`,
    timeout: 1000
});

export default {


    isLoggedId(token) {

        const headers = {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        }

        return HTTP.get(`/auth/signedIn`, {
            headers: headers
        });

    }



}

