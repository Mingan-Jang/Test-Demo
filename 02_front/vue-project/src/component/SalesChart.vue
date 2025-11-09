<template>
  <v-chart class="chart" :option="option" autoresize />
</template>

<script setup lang="ts">
import { ref } from 'vue'
import type { EChartsOption } from 'echarts'

// 圖表配置
const option = ref<EChartsOption>({
  title: {
    text: '每月銷售統計圖',
  },
  tooltip: {},
  legend: {
    data: ['銷售額'],
  },
  xAxis: {
    data: ['一月', '二月', '三月', '四月', '五月', '六月'],
  },
  yAxis: {},
  series: [
    {
      name: '銷售額',
      type: 'bar',
      data: [12, 25, 18, 30, 22, 40],
    },
  ],
})
</script>

<style scoped>
.chart {
  width: 600px;
  height: 400px;
}
</style>
