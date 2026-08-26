import api from './api'

export default{
    login(username, password){
        return api.post('/auth/login',{username, password})
    },

    logout(){
        return api.post('/auth/logout')
    },

    whoami(){
        return api.get('/auth/whoami')
    }
}