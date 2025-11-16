<template>
  <div class="p-4 mt-6 border rounded">
    <h2 class="text-xl font-semibold mb-2">jieba-wasm 分詞（繁體）示範</h2>

    <label class="block mb-2">原始文字：</label>
    <textarea v-model="text" rows="6" class="w-full border p-2 mb-2"></textarea>

    <div class="mb-2 flex gap-2">
      <button @click="parse" class="bg-green-600 text-white px-3 py-1 rounded">解析</button>
      <button @click="reset" class="bg-gray-400 text-white px-3 py-1 rounded">重置</button>
    </div>

    <div v-if="loading" class="mb-2 text-blue-600">jieba-wasm 初始化中…</div>
    <div v-if="error" class="text-red-600 mb-2">❌ {{ error }}</div>

    <div v-if="tokens && tokens.length > 0">
      <h3 class="font-medium mb-2">✅ 解析結果（{{ tokens.length }} 個詞）：</h3>
      <div class="flex flex-wrap gap-2 mb-4">
        <span v-for="(t, i) in tokens" :key="i" class="px-2 py-1 bg-blue-50 border border-blue-200 rounded">
          {{ t }}
        </span>
      </div>

      <h3 class="font-medium mt-3 mb-1">原始文字：</h3>
      <div class="whitespace-pre-wrap p-2 border rounded bg-gray-50 text-sm">{{ text }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

const defaultText = `SQL，全名為 Structured Query Language，是一種專門用來管理與操作關聯式資料庫（Relational Database）的語言。自從 1970 年代由 IBM 研究員提出關聯式模型後，SQL 迅速成為資料庫的標準語言，並被廣泛使用於企業系統、金融、醫療、政府資料平台、Web 應用與資料分析領域。幾乎所有常見的資料庫系統，例如 MySQL、PostgreSQL、Oracle、SQL Server、DB2，都支援 SQL 語法，並依需求進行延伸。

SQL 的核心精神是讓使用者能夠以接近自然語言的方式描述資料的查詢需求，並由資料庫系統負責最佳化執行過程。也就是說，使用 SQL 時，開發者只需描述「要什麼資料」，而不需描述「如何取得資料」。`

const text = ref<string>(defaultText)
const tokens = ref<string[] | null>(null)
const loading = ref(false)
const error = ref<string | null>(null)
let cutFn: ((text: string, precise?: boolean) => string[]) | null = null

onMounted(async () => {
  loading.value = true
  try {
    console.log('開始載入 jieba-wasm (web 版本)...')
    
    // 明確導入 web 版本
    const { cut } = await import('jieba-wasm/web')
    cutFn = cut
    console.log('✅ jieba-wasm 載入成功')
  } catch (e) {
    console.error('❌ jieba-wasm 載入失敗', e)
    error.value = `jieba-wasm 載入失敗: ${(e as Error).message}`
  } finally {
    loading.value = false
  }
})

function parse() {
  tokens.value = null
  error.value = null
  if (!text.value) {
    error.value = '請輸入文字後再解析'
    return
  }

  if (!cutFn) {
    error.value = 'jieba-wasm 尚未載入，無法分詞'
    return
  }

  try {
    console.log('開始分詞...')
    // 使用 cut() 進行分詞，第二個參數 true 表示精確分詞
    const result = cutFn(text.value, true)
    console.log('📝 分詞結果:', result)
    tokens.value = result
  } catch (e) {
    console.error('分詞錯誤:', e)
    error.value = `分詞失敗: ${(e as Error).message}`
  }
}

function reset() {
  text.value = defaultText
  tokens.value = null
  error.value = null
}
</script>
</script>
</script>

<style scoped>
textarea {
  font-family: 'Courier New', monospace;
  resize: vertical;
}
</style>
