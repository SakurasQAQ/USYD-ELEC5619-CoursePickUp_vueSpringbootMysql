<template>
    <div>

      <el-container class="">
        <!-- 左侧导航栏 -->
        <el-aside width="280px" class="sidebar" style="height: 224px;">
          <el-menu @select="handleMenuSelect">
            <el-menu-item index="personal_details">Personal Details</el-menu-item>
            <el-menu-item index="my_courses">My Courses</el-menu-item>

            <el-menu-item index="my_comments">My Comments</el-menu-item>
          </el-menu>
        </el-aside>
  
        <!-- 中心内容区域 -->
        <el-main>
          <router-view /> <!-- 用于渲染嵌套路由 -->
        </el-main>
      </el-container>
    </div>
  </template>
  
  <script>
  export default {
    methods: {
      goToLogin() {
        this.$router.push({ name: 'login' }); // 确保在路由中定义了名为 'login' 的路由
      },
      handleMenuSelect(index) {
        switch (index) {
          case 'personal_details':
            this.$router.push({ name: 'personalDetails' }); // 跳转到个人详情页面
            break;
          case 'my_courses':
            this.$router.push({ name: 'MyCourses' }); // 跳转到我的课程页面
            break;

          case 'my_comments':
            this.$router.push({ name: 'MyComments' }); // 跳转到我的评论页面
            break;
        }
      }
    }
  };
  </script>
  
  <style>
  @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap');
  
  body {
    
    font-family: 'Roboto', sans-serif;
  }
  
  /* 左侧导航栏样式 */
  .sidebar {
  
    margin-top:10px ;
  }
  
  /* 菜单项样式 */
  .el-menu-item {
    font-size: 1.2em;
    color: #333;
  }
  
  .el-menu-item:hover {
    background-color: #e6f7ff;
    color: #409eff;
  }
  </style>
  