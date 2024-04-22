<script setup lang="ts"></script>


<template>
  <div class="container">
    <div class="error-page">
      <div class="error-info">
        对不起，您正在寻找的页面不存在。尝试检查URL的错误，然后按浏览器上的刷新按钮或尝试在我们的网页中找到其他内容。
      </div>
      <router-link to="/" class="return-home"> 返回首页 </router-link>
    </div>
  </div>
</template>

<style scoped>
.container {
  width: 100vw;
  height: 100vh;
  display: flex;
  margin: 0 auto;
  background-image: url('@/assets/images/keduolizibao.jpg');
  background-size: cover;
  background-repeat: no-repeat;
}

.error-page {
  display: flex;
  flex: 1;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-bottom: 40vh;
}

.error-info {
  font-size: 30px;
  color: red;
}

.return-home {
  font-size: 30px;
  color: blue;
}
</style>
