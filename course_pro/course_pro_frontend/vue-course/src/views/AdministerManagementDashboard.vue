<template>
  <el-container class="mainbox">
    <!-- 左侧导航栏 -->
    <el-aside width="280px" class="sidebar">
      <el-menu @select="handleMenuSelect" class="menu_el" :default-active="defaultActive">
        <el-menu-item index="users_list">Users List</el-menu-item>
        <el-menu-item index="course_management">Course Management</el-menu-item>
        <el-menu-item index="LoginRecords">LoginRecords</el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 中心内容区域 -->
    <el-main>
      <router-view /> <!-- 用于渲染嵌套路由 -->
    </el-main>
  </el-container>
</template>

<script>


export default {
  name: 'AdministerManagementDashboard',
  data() {
    return {
      defaultActive: 'users_list' // 默认选中的菜单项索引
    };
  },
  methods: {
    handleMenuSelect(index) {
      switch (index) {
        case 'users_list':
          this.$router.push({ name: 'usersList' }); // 跳转到用户列表页面
          break;
        case 'course_management':
          this.$router.push({ name: 'courseManage' }); // 跳转到课程管理页面
          break;
        case 'LoginRecords':
          this.$router.push({ name: 'LoginRecords' }); // 跳转到更改历史页面
          break;
      }
    }
  },
  mounted() {
    // 在页面加载时手动调用一次 handleMenuSelect 方法
    this.handleMenuSelect(this.defaultActive);
  }
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap');

body {
  font-family: 'Roboto', sans-serif;
}

/* 左侧导航栏样式 */
.sidebar {
  margin-top: 16px;
  background: #f5f5f5;
  padding-top: 8px;
}

.el-menu-item {
  font-size: 1.2em;
  color: #333;
}

.el-menu-item:hover {
  background-color: #e6f7ff;
  color: #409eff;
}
</style>
