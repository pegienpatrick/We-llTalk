export const routes = [
{
    path: '/',
    redirect : '/chat'

},
{
    path: '/',
    component: () => import('@/pages/auth.vue'),
    children: [
      {
        path: 'login',
        component: () => import('@/pages/Auths/login.vue'),
      },
      {
        path: 'register',
        component: () => import('@/pages/Auths/register.vue'),
      },
    ]
}





]