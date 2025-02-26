<template>
  <el-container class="ai-chat-home">
    <!-- 顶部导航栏 -->
    <!-- <el-header class="nav-header">
      <div class="nav-bar">
        <img src="@/imgs/logo.png" alt="PickUpCourse Logo" class="nav-logo" />
        <nav class="nav-links">
          <a href="#" @click.prevent="navigateTo('homepage')">Homepage</a>
          <a href="#" @click.prevent="navigateTo('courses')">Courses</a>
          <a href="#" @click.prevent="navigateTo('ai-chats')">AI chats</a>
        </nav>
        <div class="user-info">
          <span class="username">UserA</span>
          <el-avatar size="small" icon="el-icon-user-solid"></el-avatar>
        </div>
      </div>
    </el-header> -->

    <!-- 主内容区域 -->
    <el-container class="main-layout">
      <!-- 侧边栏 -->
      <el-aside width="60px" class="sidebar">
        <el-menu default-active="1" class="el-menu-vertical">
          <el-menu-item index="1">
            <img src="@/imgs/message.png" alt="Message Icon" class="menu-icon" />
          </el-menu-item>
          <el-menu-item index="2">
            <img src="@/imgs/setting.png" alt="Setting Icon" class="menu-icon" />
          </el-menu-item>
          <el-menu-item index="3" class="bottom-item" @click="handleExit">
            <img src="@/imgs/exit.png" alt="Exit Icon" class="menu-icon" />
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 主内容 -->
      <el-container class="main-container">
        <!-- <el-header class="header-title">
          <h2>Start with AI</h2>
        </el-header> -->

        <el-main class="main-content">
          <!-- 对话内容容器 -->
          <div class="chat-container">
            <div v-for="(message, index) in messages" :key="index" class="chat-message">
              <span class="chat-user">{{ message.user }}:</span>
              <span class="chat-text">{{ message.text }}</span>
            </div>
          </div>
          <div class="card-container">
            <!-- 保留接口以便后续功能完善 -->
          </div>
        </el-main>

        <el-footer class="chat-footer">
          <div class="message-input-container">
            <el-input
              v-model="userMessage"
              placeholder="Send a message..."
              class="message-input"
              @keyup.enter="sendMessage"
              clearable
            >
              <template #append>
                <el-button type="primary" class="send-button" @click="sendMessage">
                  <img src="@/imgs/send.png" alt="Send Icon" class="send-icon" />
                </el-button>
              </template>
            </el-input>
          </div>
        </el-footer>
      </el-container>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import axios from 'axios'; // 安装 axios
import { onMounted } from 'vue';


// 使用 useRoute 来获取传递的参数
const route = useRoute();
const router = useRouter();

const userMessage = ref(route.query.userMessage || ''); // 初始化为传递的内容，如果没有则为空
const messages = ref([]);
// 退出按钮的点击处理函数
const handleExit = () => {
  ElMessage({
    message: 'Exiting to Home...',
    type: 'info',
  });
  router.push('/'); // 跳转到主页
};


// 处理发送消息的逻辑
const sendMessage = async () => {
  if (!userMessage.value.trim()) {
    ElMessage({
      message: 'Message cannot be empty!',
      type: 'warning',
    });
    return;
  }

  // 在消息数组中加入用户消息
  messages.value.push({ user: 'You', text: userMessage.value });
  
  // 获取用户输入的消息以发送到后端
  const messageToSend = userMessage.value;

  // 清空输入框
  userMessage.value = ''; 

  try {
    const response = await axios.post('http://localhost:8080/api/chat', {
      message: messageToSend
    });
    messages.value.push({ user: 'AI', text: response.data.choices[0].message.content });
  } catch (error) {
    console.error('Error sending message:', error);
    messages.value.push({ user: 'AI', text: 'Failed to connect to AI service, please try again later.' });
  }
};

onMounted(() => {
  if (userMessage.value.trim()) {
    sendMessage();
  }
});

</script>

<style scoped>
.ai-chat-home {
  margin-top: 11px;
  height: 90vh; /* 占满整个视口高度 */
  display: flex;
  flex-direction: column;
  overflow: hidden; /* 防止页面溢出导致滚动条 */
}

.nav-header {
  background: #fff;
  padding: 0 20px; /* 减少左右内边距，使内容更紧凑 */
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  height: 70px; /* 固定导航栏的高度 */
}

.nav-logo {
  height: 40px; /* 根据需要调整高度 */
  width: auto; /* 自动调整宽度保持比例 */
  margin-right: 20px; /* 根据需要调整与链接的间距 */
}

.nav-bar {
  display: flex;
  align-items: center; /* 垂直方向居中，使标题和链接在同一高度 */
  justify-content: flex-start; /* 将标题和链接靠左对齐 */
  gap: 20px; /* 增加标题和链接之间的间距 */
}

.nav-title {
  margin-right: 40px; /* 增加标题和导航链接之间的间距 */
}

.nav-links {
  display: flex;
  gap: 20px;
  align-items: center; /* 链接垂直居中 */
}

.nav-links a {
  text-decoration: none; /* 默认没有下划线 */
  color: #333; /* 默认字体颜色 */
  font-weight: bold;
  font-size: 14px; /* 缩小字体 */
  position: relative; /* 为了使用伪元素 */
}

.nav-links a:hover {
  color: #409eff; /* 将字体颜色改为蓝色 */
}

.nav-links a:hover::after {
  content: ""; 
  position: absolute;
  left: 0;
  right: 0;
  bottom: -2px; /* 控制下划线与文字之间的距离 */
  height: 2px; /* 控制下划线的粗细 */
  background-color: #409eff; /* 设置下划线的颜色为蓝色 */
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-left: auto; /* 将 .user-info 推到导航栏的右边 */
}

.main-layout {
  display: flex;
  flex: 1;
  height: calc(100vh - 70px); /* 主内容区域高度为视口高度减去导航栏的高度 */
}

.sidebar {
  background: #409eff; /* 使用纯色背景 */
  padding: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 减小阴影的尺寸和不透明度，使边框看起来更轻巧 */
  width: 60px; /* 调整侧边栏宽度，使其更纤细 */
  height: 100%; /* 确保侧边栏占据主内容区域的全高 */
}

.el-menu-vertical {
  border: none;
  background: #409eff; /* 与侧边栏一致的背景颜色 */
  display: flex;
  flex-direction: column; /* 让菜单项垂直排列 */
  flex: 1; /* 占据侧边栏的最大空间 */
  width: 100%; /* 确保菜单项容器宽度占据侧边栏的全部 */
  padding: 20px 0; /* 确保没有多余的内边距导致超出 */
}

.el-menu-item {
  display: flex;
  justify-content: center;
  align-items: center;
  background: #409eff; /* 默认纯色背景 */
  color: #ffffff; /* 文本为白色 */
  transition: background 0.3s, box-shadow 0.3s; /* 平滑过渡 */
  border: none; /* 去掉边框 */
  padding: 15px;
  cursor: pointer; /* 鼠标悬停时显示手形光标 */
  box-shadow: none; /* 去掉阴影，减少边框感 */
}

/* 鼠标悬停时的效果 */
.el-menu-item:hover {
  background: #87cefa; /* 悬停时淡蓝色 */
  box-shadow: none; /* 不增加悬停时的阴影 */
}

/* 图标样式，使用滤镜将图标变为白色 */
.menu-icon {
  width: 30px; /* 调整图标宽度，使其更小、更适合 */
  height: 30px; /* 调整图标高度 */
  margin: 5px 0; /* 增加垂直方向的间距，确保图标与边框的距离更远 */
  filter: invert(100%) brightness(100%); /* 使用滤镜将图标变为白色 */
}

.bottom-item {
  margin-top: auto; /* 将最后一个菜单项推到菜单底部 */
  margin-bottom: 20px; /* 向上移动一些距离 */
}

.main-container {
  display: flex;
  flex-direction: column;
  flex: 1;
  background: #f0f0f0; /* 将主内容区域的背景颜色设置为淡灰色 */
}

.header-title {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  color: #9e9e9e;
  height: 100px;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 20px;
}

.chat-container {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: #f9f9f9;
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  margin-bottom: 20px;
}

.chat-message {
  margin-bottom: 10px;
}

.chat-user {
  font-weight: bold;
  margin-right: 5px;
}

.message-input-container {
width: 90%;
border-radius: 30px;
overflow: hidden;
border: 5px solid #409eff;
padding: 10px;
position: fixed;
bottom: 6px; /* 调整底部间距，向上移动输入框 */
left: 52%;
transform: translateX(-50%);
background: #fff;
box-shadow: 0 -2px 5px rgba(0, 0, 0, 0.1);
}

.message-input :deep(.el-input__inner) {
  border: none !important; /* 移除输入框边框 */
  box-shadow: none !important; /* 移除阴影 */
  outline: none !important; /* 移除聚焦时的样式 */
  height: 50px; /* 增加输入框的高度 */
  padding: 15px; /* 增加内边距 */
  font-size: 16px; /* 增大字体 */
  border-radius: 30px; /* 增加圆角 */
  background-color: #fff !important; /* 保持输入框的背景色一致 */
}

.send-button {
  width: 60px; /* 设置按钮的宽度，使其合适地占据右侧 */
  height: 60px; /* 设置按钮高度与输入框一致 */
  background-color: #409eff !important; /* 强制设置按钮背景颜色为蓝色 */

  cursor: pointer; /* 鼠标悬停显示手形光标 */
}

.send-button:hover {
  background-color: #1e8fdb !important; /* 鼠标悬停时变为深蓝色 */
}

.send-button:focus {
  outline: none; /* 移除点击后的聚焦样式 */
  box-shadow: none; /* 去掉可能的聚焦阴影 */
}

.send-icon {
  width: 30px; /* 增大图标的宽度 */
  height: 30px; /* 增大图标的高度 */
  filter: invert(100%) brightness(100%); /* 将图标颜色变为白色 */
}
</style>
