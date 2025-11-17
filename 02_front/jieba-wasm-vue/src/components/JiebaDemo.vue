<template>
  <div class="container">
    <h1>jieba-wasm 分詞示例</h1>

    <div v-if="!ready" class="loading">Loading Jieba...</div>

    <div v-else>
      <!-- 自訂字典輸入 -->
      <label for="dictInput">自訂字典（每行一個詞，可帶詞頻及詞性）:</label>
      <textarea
        id="dictInput"
        v-model="dictText"
        rows="3"
        placeholder="例如：滷肉飯 100 n"
        class="dict-input"
      ></textarea>
      <button @click="applyDict" class="btn">套用字典</button>

      <!-- 分詞文字輸入 -->
      <label for="textInput">要分詞的文字:</label>
      <textarea
        id="textInput"
        v-model="targetText"
        rows="3"
        placeholder="輸入文字..."
        class="dict-input"
      ></textarea>
      <button @click="doCut" class="btn">分詞</button>

      <!-- 分詞結果 -->
      <p class="label">分詞結果：</p>
      <div class="chip-container">
        <div v-for="(word, index) in results" :key="word + index" class="chip">
          {{ word }}
          <button class="chip-close" @click="removeWord(index)">×</button>
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
const targetText = ref("我愛吃滷肉飯"); // 預設文字
const dictText = ref("朱貝樂 100 n"); // 自訂字典輸入

let jiebaInstance = null;

onMounted(async () => {
  try {
    jiebaInstance = await init("/wasm/jieba_rs_wasm_bg.wasm");
    ready.value = true;

    // 可以先套用預設字典
    with_dict("滷肉飯 100 n\n");
    results.value = cut(targetText.value, false);
  } catch (error) {
    console.error("Jieba 初始化失敗:", error);
  }
});

// 套用自訂字典
function applyDict() {
  try {
    with_dict(dictText.value);
  } catch (error) {
    console.error("套用字典失敗:", error);
  }
}

// 分詞
function doCut() {
  results.value = cut(targetText.value, false);
}

// 刪除 chip
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

.dict-input {
  width: 100%;
  padding: 0.5rem;
  font-size: 0.85rem;
  margin-bottom: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-family: inherit;
}

.btn {
  margin-bottom: 1rem;
  padding: 0.3rem 0.8rem;
  font-size: 0.85rem;
  border: none;
  background-color: #3b82f6;
  color: white;
  border-radius: 4px;
  cursor: pointer;
}

.btn:hover {
  background-color: #2563eb;
}

.chip-container {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-top: 0.5rem;
}

.chip {
  display: flex;
  align-items: center;
  background-color: #dbeafe;
  color: #1e3a8a;
  font-size: 0.85rem;
  font-weight: 500;
  padding: 0.3rem 0.8rem;
  border-radius: 9999px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
  cursor: default;
  transition: background-color 0.2s;
}

.chip:hover {
  background-color: #bfdbfe;
}

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
