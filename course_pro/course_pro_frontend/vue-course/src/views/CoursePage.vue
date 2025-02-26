<template>
  <div class="conmain">
    <div class="search-bar">
      <el-input
        v-model="searchQuery"
        placeholder="Search for courses..."
        clearable
        class="search-input">
        <template #append>
          <el-button @click="clearSearch">Clear</el-button>
        </template>
      </el-input>
      <el-select v-model="selectedMajor" placeholder="Select Major" class="major-select">
        <el-option
          v-for="major in uniqueMajors"
          :key="major"
          :label="major"
          :value="major"
        />
      </el-select>
    </div>
    <div class="course-title">Courses</div>
    <el-main class="course-content">
      <el-row :gutter="20">
        <el-col :span="8" v-for="(course, index) in filteredCourses" :key="course.courseId">
          <el-card
            class="course-card"
            @click="goToCourseDetail(course.courseId, index)"
            shadow="hover"
          >
            <img
                :src="getImageForCourse(index)"
                alt="Course Image"
                class="course-image"
              />
            <div class="course-info">
              <h2 class="course-name">{{ course.courseName }}</h2>
              <p class="course-code">Code: {{ course.courseId }}</p>
              <p class="course-description">{{ course.description }}</p>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import { baseURL } from '../api/api';
import backgroundImage from '@/imgs/background.jpg';

import { createClient } from 'pexels'
import { ElMessage } from 'element-plus';


const client = createClient('CgPGGt6tb2LUqz3nnCu7dRhopPd14HgwwY9c8kZ8VVWPXjqm4IbC9pV6');

const query = 'Nature';


const photos = ref([]);
const router = useRouter();


const searchQuery = ref("");
const selectedMajor = ref("");
const courses = ref([]); 
const majors = ref([]); 
const errorMessage = ref("");




const searchPhotos = async () =>{
  try{
    const response = await client.photos.search({
      query: query,
      page: 1,
      per_page: 60,
      size:'medium'
    })

    photos.value = response.photos

    console.log("test",photos);

  }catch{

    console.error('get error:', error);
    ElMessage("error");
  
  }
}


const getImageForCourse = (courseIndex) => {
  if (photos.value.length > 0) {
    return photos.value[courseIndex % photos.value.length]?.src?.small || backgroundImage;
  } else {
    return backgroundImage; // 如果没有照片，使用默认背景图
  }
};



const fetchCourses = async () => {
  const token = localStorage.getItem("userToken");
  console.log('token', token);
  try {
    const response = await axios.get(`${baseURL}/api/courses`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
    console.log('Response data:', response.data); 
    courses.value = response.data; 
    extractUniqueMajors();
  } catch (error) {
    console.error('Failed to fetch courses:', error);
    errorMessage.value = 'Failed to load courses.';
  }
};

const extractUniqueMajors = () => {
  const majorSet = new Set();
  courses.value.forEach(course => {
    if (course.major) {
      majorSet.add(course.major);
    }
  });
  majors.value = Array.from(majorSet);
};



const goToCourseDetail = (courseId, courseIndex) => {
  const courseImage = getImageForCourse(courseIndex);
  router.push({ 
    name: "CourseDetailPage", 
    state: { 
      courseId, 
      courseImage 
    } 
  });
};



onMounted(() => {



  fetchCourses(); 
  if (photos.value.length === 0) {
    searchPhotos();
  }

  
  // fetchCourses(); 
  // searchPhotos();
});





// Get unique majors for the dropdown
const uniqueMajors = computed(() => {
  return [...new Set(courses.value.map(course => course.major))];
});

// Filter courses based on search query and selected major
const filteredCourses = computed(() => {
  return courses.value.filter(course => {
    const matchesQuery = course.courseName.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      course.description.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      course.courseId.toLowerCase().includes(searchQuery.value.toLowerCase());
    const matchesMajor = selectedMajor.value ? course.major === selectedMajor.value : true;
    return matchesQuery && matchesMajor;
  });
});

// Clear search input and major selection
const clearSearch = () => {
  searchQuery.value = '';
  selectedMajor.value = '';
};










</script>

<style>


body {
  font-family: 'Roboto', sans-serif;
}


.conmain{
  min-width: 800px;
}

/* 搜索栏样式 */
.search-bar {
  display: flex;
  justify-content: center;
  margin-top: 40px;
  gap: 20px;
}

.search-input {
  width: 70%;
}

.major-select {
  width: 20%;
}

/* 课程标题样式 */
.course-title {
  margin-top: 5px;
  font-weight: bold;
  display: block;
  margin-left: 40px;
  
  font-size: 2em;

  color: #333;
}

/* 课程网格和课程卡片样式 */
.course-content {
  padding: 20px;
}

.course-card {
  cursor: pointer;
  transition: transform 0.3s;
}

.course-card:hover {
  transform: scale(1.05); /* 鼠标悬停时的缩放效果 */
}

.course-image {
  width: 380px !important;
  height: 270px !important;
  object-fit: cover !important;
  border-radius: 10px !important;
  margin-bottom: 15px !important;
}
.course-info {
  text-align: left;
}

.course-name {
  font-size: 1.5em;
  font-weight: bold ;
  margin-bottom: 10px;
  color: #000000;
}

.course-code {
  font-size: 1.1em;
  color: #555;
  margin-bottom: 10px;
}

.course-description {
  font-size: 1em;
  color: #333;
}
</style>