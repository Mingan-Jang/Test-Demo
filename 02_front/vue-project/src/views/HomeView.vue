<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getAllUsers, getUserById, createUser, deleteUser } from '../api/userApi'
import type { User } from '../types/user'

const users = ref<User[]>([])
const newUser = ref<User>({ username: '', email: '', password: '' })
const searchId = ref<number | null>(null)
const searchResult = ref<User | null>(null)

// 取得所有使用者
const fetchUsers = async () => {
  users.value = await getAllUsers()
}

// 查詢單個使用者
const fetchUserById = async () => {
  if (!searchId.value) return
  try {
    alert(searchId.value)
    searchResult.value = await getUserById(searchId.value)
  } catch (error) {
    searchResult.value = null
    alert('查無此使用者')
  }
}

// 新增使用者
const addUser = async () => {
  if (!newUser.value.username) return
  await createUser(newUser.value)
  newUser.value = { username: '', email: '', password: '' }
  fetchUsers()
}

// 刪除使用者
const removeUser = async (id: number) => {
  await deleteUser(id)
  fetchUsers()
}

onMounted(() => {
  fetchUsers()
})
</script>

<template>
  <div class="p-4">
    <h1 class="text-2xl font-bold mb-4">使用者管理</h1>

    <!-- 新增使用者 -->
    <div class="mb-4">
      <input v-model="newUser.username" placeholder="Username" class="border p-1 mr-2" />
      <input v-model="newUser.email" placeholder="Email" class="border p-1 mr-2" />
      <input v-model="newUser.password" placeholder="Password" class="border p-1 mr-2" />
      <button @click="addUser" class="bg-blue-500 text-white px-3 py-1 rounded">新增</button>
    </div>

    <!-- 查詢單個使用者 -->
    <div class="mb-4">
      <input
        type="number"
        v-model.number="searchId"
        placeholder="輸入使用者 ID 查詢"
        class="border p-1 mr-2"
      />
      <button @click="fetchUserById" class="bg-yellow-500 text-white px-3 py-1 rounded">
        查詢
      </button>
      <div v-if="searchResult" class="mt-2">
        查詢結果：{{ searchResult.username }} - {{ searchResult.email }}
      </div>
    </div>

    <!-- 顯示所有使用者 -->
    <ul>
      <li v-for="user in users" :key="user.id" class="mb-2 flex justify-between">
        <span>{{ user.id }} - {{ user.username }} - {{ user.email }}</span>
        <button @click="removeUser(user.id!)" class="bg-red-500 text-white px-2 rounded">
          刪除
        </button>
      </li>
    </ul>
  </div>
</template>
