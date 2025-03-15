import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import FlightDetail from '@/components/FlightDetail.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/flight/:id',
      name: 'SFV',
      component: FlightDetail,
      props: true,
    },
  ],
})

export default router
