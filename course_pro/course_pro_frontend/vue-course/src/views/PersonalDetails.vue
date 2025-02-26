<template>
  <div>
    <h2 style="text-align: center; margin-top: 20px;">Personal Information</h2>
    <el-form label-position="top" label-width="100px" style="width: 400px; margin: 20px auto 0;">
      <el-form-item>
        <!-- Adding an empty line before Original Email -->
        <div style="height: 20px;"></div>
      </el-form-item>
      <el-form-item label="Original Email">
        <div>{{ userToEdit.email }}</div>
      </el-form-item>
      <el-form-item label="Original Password">
        <div>{{ userToEdit.pwd }}</div>
      </el-form-item>
      <el-form-item label="New Email">
        <el-input v-model="newEmail" style="width: 100%;"></el-input>
      </el-form-item>
      <el-form-item label="New Password">
        <el-input v-model="newPwd" type="password" style="width: 100%;"></el-input>
      </el-form-item>
    </el-form>
    <div style="display: flex; justify-content: center; margin-top: 20px;">
      <el-button type="primary" @click="doEdit">Update Personal Information</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { baseURL, get_all_users } from '@/api/api';  
import { useRouter } from 'vue-router'; // 引入 useRouter

// 获取路由实例
const router = useRouter();

interface User {
  userId: number;
  email: string;
  username: string;
  admin: boolean;
  pwd: string;
}

const userToEdit = ref<User>({
  userId: 0,
  email: '',
  username: '',
  admin: false,
  pwd: ''
});

const newEmail = ref<string>('');
const newPwd = ref<string>('');

// 加载用户数据
const loadUserData = async () => {
  const token = localStorage.getItem('userToken');
  if (!token) {
    ElMessage.error('Missing authentication token');
    return;
  }

  const userId = localStorage.getItem('userId');
  if (!userId) {
    ElMessage.error('User ID not found, please log in again');
    return;
  }

  const url = baseURL + get_all_users;
  try {
    const response = await fetch(url, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    const dataList: User[] = await response.json();

    const currentUser = dataList.find(user => user.email === userId);
    
    if (currentUser) {
      userToEdit.value = currentUser;
    } else {
      ElMessage.error('No user data found');
    }
  } catch (err) {
    ElMessage.error('Failed to fetch user data');
  }
};

// 更新用户信息
const doEdit = async () => {
  if (!userToEdit.value) return;
  const token = localStorage.getItem('userToken');
  if (!token) {
    ElMessage.error('Missing authentication token');
    return;
  }

  const url = `http://localhost:8080/api/users/edit/${userToEdit.value.userId}`;
  
  const updatedUser = {
    ...userToEdit.value,
    email: newEmail.value || userToEdit.value.email,
    pwd: newPwd.value || userToEdit.value.pwd
  };

  try {
    const response = await fetch(url, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(updatedUser)
    });

    if (response.ok) {
      ElMessage.success('User information updated successfully');
      
      // 清空 localStorage
      localStorage.clear();

      // 使用 Vue Router 跳转到登录页面
      router.push('/login'); // 假设 '/login' 是你的登录路由

    } else {
      throw new Error('Failed to update user information');
    }

  } catch (err) {
    ElMessage.error('Failed to update user information');
  }
};

// 页面加载时获取用户数据
onMounted(() => {
  loadUserData();
});
</script>