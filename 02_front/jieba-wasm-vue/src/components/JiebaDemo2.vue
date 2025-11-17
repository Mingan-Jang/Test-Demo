<template>
  <div class="container">
    <h1>jieba-wasm 分詞示例</h1>

    <div v-if="!ready" class="loading">Loading Jieba...</div>

    <div v-else>
      <p class="label">分詞結果（固定文本示例）</p>
      {{ targetText }}
      <div class="chip-container">
        <div v-for="(word, index) in results" :key="word + index" class="chip">
          {{ word }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import init, { cut, with_dict } from "jieba-wasm";

const ready = ref(false);
const results = ref([]);
// const targetText =
//   "我可以幫你整理一個 打包後 Vue + jieba-wasm 可直接運行的範例專案結構";
const targetText =
  "我可以幫你整理一個 打包後 Vue + jieba-wasm 可直接運行的範例專案結構";

const targetText2 = "我愛吃滷肉飯";
onMounted(async () => {
  try {
    await init("/wasm/jieba_rs_wasm_bg.wasm");

    // 自訂字典，把滷肉飯設成一個詞
    with_dict("滷肉飯 100 n\n");

    // 精確模式切詞
    results.value = cut(targetText, false);
    ready.value = true;
  } catch (error) {
    console.error("Jieba 初始化失敗:", error);
  }
});

function removeWord(index) {
  results.value.splice(index, 1);
}
</script>

<style>
.container {
  max-width: 600px;
  margin: 2rem auto;
  padding: 1rem;
  font-family: sans-serif;
}

h1 {
  font-size: 2rem;
  font-weight: bold;
  margin-bottom: 1rem;
}

.loading {
  color: #666;
  margin-top: 1rem;
}

.label {
  font-size: 0.9rem;
  font-weight: 500;
  margin-top: 1rem;
}

.chip-container {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-top: 1rem;
}

.chip {
  display: flex;
  align-items: center;
  background-color: #dbeafe; /* 淺藍色 */
  color: #1e3a8a; /* 深藍色 */
  font-size: 0.85rem;
  font-weight: 500;
  padding: 0.3rem 0.8rem;
  border-radius: 9999px; /* 完全圓角 */
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
  cursor: default;
  transition: background-color 0.2s;
}

.chip:hover {
  background-color: #bfdbfe; /* hover 淺藍色 */
}

/* 可選：刪除按鈕 */
.chip-close {
  margin-left: 0.4rem;
  background: none;
  border: none;
  color: #1e3a8a;
  font-weight: bold;
  cursor: pointer;
  font-size: 0.8rem;
}

.chip-close:hover {
  color: #7c3aed;
}
</style>
