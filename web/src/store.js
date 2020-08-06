import Vue from 'vue'
import Vuex from 'vuex'
import api from './services/login-api'
import createPersistedState from 'vuex-persistedstate'
Vue.use(Vuex);

export default new Vuex.Store({
    plugins: [createPersistedState({
        storage: window.sessionStorage,
    })],
    state: {
        loginSuccess: false,
        loginError: false,
        userName: null,
        accessToken: null
    },
    mutations: {
        login_success(state, payload) {

            state.loginSuccess = true;
            state.accessToken = payload.accessToken;
            state.userName = payload.userName;

        },
        login_error(state, payload) {
            state.loginError = true;
            state.userName = payload.userName;
            state.loginSuccess = false;

        }
    },
    actions: {
        login({ commit }, { user, password }) {

            return new Promise((resolve, reject) => {
                api.getSecured(user, password)
                    .then(response => {
                        if (response.status == 200) {
                            // place the loginSuccess state into our vuex store
                            Vue.$log.info("Placing token");
                            commit('login_success', {
                                accessToken: response.data.accessToken,
                                userName: user
                            });
                        }
                        resolve(response)
                    })
                    .catch(error => {
                        Vue.$log.error(error)
                        // place the loginError state into our vuex store
                        commit('login_error', {
                            userName: user
                        });
                        reject("Invalid credentials!")
                    })
            })
        },
        logout({ commit }) {

            commit('login_error', {
                accessToken: '',
                userName: ''
            });
            window.sessionStorage.clear();
        }
    },
    getters: {
        isLoggedIn: state => state.loginSuccess
        ,
        hasLoginErrored: state => state.loginError,
        getToken: state => state.accessToken,
        getLoggedInUser: state => state.userName

    }
})