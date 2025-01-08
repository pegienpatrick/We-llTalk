/**
 * main.js
 *
 * Bootstraps Vuetify and other plugins then mounts the App`
 */

// Plugins
import { registerPlugins } from '@/plugins'

// Components
import App from './App.vue'
import axios from 'axios'

// Composables
import { createApp } from 'vue'

let apiUrl="http://welltalk.us.to:8081";

axios.defaults.baseURL=apiUrl;

const app = createApp(App)

registerPlugins(app)

app.mount('#app')
