export const routes = [
  {
    path: '/',
    redirect: '/chat'
  },
  {
    path: '/auth/',
    component: () => import('@/pages/auth.vue'),
    children: [
      {
        path: 'login',
        component: () => import('@/pages/login.vue')
      },
      {
        path: 'register',
        component: () => import('@/pages/register.vue')
      },
    ],
  },
  {
    path:'/',
    component: () => import('@/pages/dashboard.vue'),
    children: [

    ]
  }
]
