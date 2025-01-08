
/**
 * router/index.ts
 *
 * Automatic routes for `./src/pages/*.vue`
 */

// Composables
import { createRouter, createWebHistory } from 'vue-router/auto'
import { routes } from './routes'

console.log(routes)
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

console.log(router.getRoutes())

export default router
