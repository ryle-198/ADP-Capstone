<script setup>
import {computed} from 'vue'
import {useRoute} from 'vue-router'
import SidePanel from './components/SidePanel.vue'

const route = useRoute()

const showSidebar = computed(()=>{
    return route.meta.showSideBar === true
})
</script>

<template>

<div class="app-layout">
    <SidePanel v-if="showSidebar"/>

    <main class="app-content">
        <RouterView />
    </main>
</div>    
<!-- <SidePanel/> -->
<!-- <Home/> -->
<!-- <Login/> -->
<!-- <RouterView /> -->
</template>

<style scoped>
    .app-layout {
        display: flex;
        min-height: 100vh;
    }

    .app-content{
        flex:2;
        min-width:0;
        min-height: 100vh;   
    }
</style>