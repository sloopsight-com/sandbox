import Vue from 'vue'
import Router from 'vue-router'
import DashboardLayout from '@/layout/DashboardLayout'
import AuthLayout from '@/layout/AuthLayout'
import store from './store'
import guards from './services/guards'

Vue.use(Router)

const router = new Router({
  linkExactActiveClass: 'active',
  routes: [
    {
      path: '/',
      redirect: 'dashboard',
      component: DashboardLayout,
      children: [
        {
          path: '/dashboard',
          name: 'dashboard',
          // route level code-splitting
          // this generates a separate chunk (about.[hash].js) for this route
          // which is lazy-loaded when the route is visited.
          component: () => import(/* webpackChunkName: "demo" */ './views/Dashboard.vue'),
          meta: {
            requiresAuth: true
          }
        },
        {
          path: '/projects',
          name: 'projects',
          // route level code-splitting
          // this generates a separate chunk (about.[hash].js) for this route
          // which is lazy-loaded when the route is visited.
          component: () => import(/* webpackChunkName: "demo" */ './views/Projects.vue'),
          meta: {
            requiresAuth: true
          }
        }
        ,
        {
          path: '/project',
          name: 'Create Project',
          // route level code-splitting
          // this generates a separate chunk (about.[hash].js) for this route
          // which is lazy-loaded when the route is visited.
          component: () => import(/* webpackChunkName: "demo" */ './views/Project.vue'),
          meta: {
            requiresAuth: true
          }
        }
        , {
          path: '/project/:id',
          name: 'Edit Project',
          // route level code-splitting
          // this generates a separate chunk (about.[hash].js) for this route
          // which is lazy-loaded when the route is visited.
          component: () => import(/* webpackChunkName: "demo" */ './views/Project.vue'),
          meta: {
            requiresAuth: true
          }
        },
        {
          path: '/project/:id/endpoint',
          name: 'Endpoints',
          // route level code-splitting
          // this generates a separate chunk (about.[hash].js) for this route
          // which is lazy-loaded when the route is visited.
          component: () => import(/* webpackChunkName: "demo" */ './views/Endpoints.vue'),
          meta: {
            requiresAuth: true
          }
        },
        {
          path: '/project/:projectId/endpoint/:id',
          name: 'Edit Endpoint',
          // route level code-splitting
          // this generates a separate chunk (about.[hash].js) for this route
          // which is lazy-loaded when the route is visited.
          component: () => import(/* webpackChunkName: "demo" */ './views/Endpoint.vue'),
          meta: {
            requiresAuth: true
          }
        }
        ,
        {
          path: '/docs/:projectId',
          name: 'vIEW dOCUMENT',
          // route level code-splitting
          // this generates a separate chunk (about.[hash].js) for this route
          // which is lazy-loaded when the route is visited.
          component: () => import(/* webpackChunkName: "demo" */ './views/Docs.vue'),
          meta: {
            requiresAuth: true
          }

        }
      ]
    },
    {
      path: '/',
      redirect: 'login',
      component: AuthLayout,
      children: [
        {
          path: '/login',
          name: 'login',
          component: () => import(/* webpackChunkName: "demo" */ './views/Login.vue')
        }
      ]
    }

  ]
})

router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    // this route requires auth, check if logged in
    // if not, redirect to login page.
    Vue.$log.info(store.getters.isLoggedIn);
    Vue.$log.info(store.getters.getToken);
    Vue.$log.info(store.getters.getLoggedInUser);

    if (!store.getters.isLoggedIn) {
      next({
        path: '/login'
      })
    } else {

      guards.isLoggedId(store.getters.getToken).then(() => {
        next();
      }).catch(e => {
        Vue.$log.info(e);


        this.$store
          .dispatch("logout", {
            user: this.model.email,
            password: this.model.password
          })
          .then(() => {
            this.$router.push("/login");
          })
          .catch(error => {
            this.error = true;
            this.error_message = error;
          });

      })


    }
  } else {
    next(); // make sure to always call next()!
  }
});

export default router;