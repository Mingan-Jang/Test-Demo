import { createApp } from 'vue'
import App from './App.vue'
import router from './router' // ← 引入 router
import VueECharts from 'vue-echarts'
import { use } from 'echarts/core'

// 按需載入 ECharts 模組
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart, LineChart, PieChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
} from 'echarts/components'

// 註冊 ECharts 所需模組
use([
  CanvasRenderer,
  BarChart,
  LineChart,
  PieChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
])

const app = createApp(App)

// 全域註冊 <v-chart> 元件
app.component('v-chart', VueECharts)

// ← 這行一定要加
app.use(router)

app.mount('#app')
