import {ref} from 'vue'
import authService from '../services/authService'

const isAdmin=ref(false)
const checked=ref(false)
const roles=ref([])

async function checkAuth(){
    try{
        const res = await authService.whoami()
        roles.value = (res.data.roles || []).map(r=>r.authority)
        isAdmin.value = roles.value.includes('ROLE_ADMIN')
    }catch{
        roles.value = []
        isAdmin.value=false
    }finally{
        checked.value=true
    }
}

function hasRole(name){
    return roles.value.includes('ROLE_' + name)
}


function clearAuth(){
    roles.value = []
    isAdmin.value = false
    checked.value = false
}

export function useAuth(){
    return{isAdmin, checked, roles, checkAuth, hasRole, clearAuth}
}