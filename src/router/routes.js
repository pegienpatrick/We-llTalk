export const routes = [
  {
    path: 'auth',
    component: () => import('@/pages/Auth/auth.vue'),
    children: [
      {
        path: 'login',
        component: () => import('@/pages/Auth/login.vue')
      },
      {
        path: 'register',
        component: () => import('@/pages/Auth/register.vue')
      },
    ],
  },
  {
    path:'/',
    component: () => import('@/pages/dashboard.vue'),
    children: [
      {
        path: '/chats',
        component: () => import('@/pages/Chat/Chats.vue')
      }

    ]
  }
]
