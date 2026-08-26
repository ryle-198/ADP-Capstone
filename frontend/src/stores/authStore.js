import {ref} from 'vue'
import authService from '../services/authService'

const isAdmin=ref(false)
const checked=ref(false)

async function checkAuth(){
    try{
        const res = await authService.whoami()
        isAdmin.value = res.data.roles?.some(r=>r.authority==='ROLE_ADMIN')
    }catch{
        isAdmin.value=false
    }finally{
        checked.value=true
    }
}

export function useAuth(){
    return{isAdmin, checked, checkAuth}
}