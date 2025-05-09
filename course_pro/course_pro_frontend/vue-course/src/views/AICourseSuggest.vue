<template>
  <div class="main_box">
    <div class="courses">
      <h2>Selected Courses Plan List</h2>
      <div class="course-grid">
        <div 
          class="course-item" 
          v-for="course in selectedCourses" 
          :key="course.courseId"
          @click="handleCourseClick(course)"
          :class="{'selected': selectedCourseId === course.courseId}"
        >
          <el-card class="box-card">
            <div class="course_lis">
              <span class="course-name"> {{ course.courseName }}</span>
              <div>
                <span class="course-code">Code: {{ course.courseId }}</span>
              </div>
              <p>{{ course.course.description }}</p>
            </div>
          </el-card>
        </div>
      </div>
    </div>

    <!-- 显示生成学习计划的 Dialog -->
    <el-dialog
      v-model="dialogVisible"
      title="Generated Study Plan"
      width="600px">
      <div v-html="generatedPlanContent"></div>
      <template #footer>
        <el-button @click="dialogVisible = false">Close</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { baseURL } from '../api/api';
import { marked } from 'marked';
import { ElLoading } from 'element-plus'; // 引入 ElLoading

// 定义变量
const selectedCourses = ref([]);
const dialogVisible = ref(false);
const generatedPlanContent = ref('');
const selectedCourseId = ref(null); // 用于存储被选中的课程ID

// 获取选中的课程
const fetchSelectedCourses = async () => {
  try {
    const userId = localStorage.getItem('Uid');  
    const token = localStorage.getItem('userToken'); 

    const url = `${baseURL}/api/users/${userId}/courses`;

    const response = await axios.get(url, {
      headers: {
        'Authorization': `Bearer ${token}`,  
      }
    });

    selectedCourses.value = response.data;

  } catch (error) {
    console.error('Failed to fetch selected courses:', error);
  }
};

// 点击课程时触发的事件
const handleCourseClick = async (course) => {
  selectedCourseId.value = course.courseId; // 设置当前选中的课程ID

  // 显示加载动画
  const loading = ElLoading.service({
    lock: true,
    text: 'Loading...',
    background: 'rgba(0, 0, 0, 0.7)',
  });

  const apiUrl = 'http://localhost:8080/api/chat/generate-study-plan';
  const body = {
    CourseId: course.courseId,
    Course_name: course.courseName,
    Course_description: course.course.description,
  };

  try {
    const response = await axios.post(apiUrl, body, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('userToken')}`, // 发送 JWT Token
      },
    });

  
    const markdownContent = response.data.choices[0].message.content;
    const htmlContent = marked(markdownContent);
    generatedPlanContent.value = htmlContent;  
    dialogVisible.value = true;

  } catch (error) {
    console.error('Failed to generate study plan:', error);
  } finally {
   
    loading.close();
  }
};

onMounted(() => {
  fetchSelectedCourses();
});
</script>

<style scoped>
.main_show {
  padding-left: 20px;
}

.main_box {
  margin-top: 20px;
  margin-bottom: 20px;
}

h2 {
  margin-left: 15px;
}

.course-grid {
  display: flex;
  flex-direction: column;
  gap: 20px;  /* 设置每个课程间的间距 */
}

.course-item {
  margin-left: 5px;
  margin-right: 5px;
  transition: background-color 0.3s, transform 0.2s; /* 添加过渡效果 */
  cursor: pointer; /* 鼠标悬停时显示指针 */
}

.course-item:hover {
  background-color: #f0f8ff; 
  transform: translateY(-5px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
}


.selected {
  background-color: #d3e8f8; /* 被点击时的颜色 */
}

.course-name {
  font-size: 1.5em; 
  font-weight: bold;
}

.course-code {
  font-size: 0.6em;
  color: #666; 
}
</style>