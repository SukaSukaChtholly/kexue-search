<script setup lang="ts">

import router from "@/router";
import { ElMessage } from "element-plus";

const animeListCache = localStorage.getItem('animePage');

if (!animeListCache) {
  setTimeout(() => {
    ElMessage.error('数据异常！');
    router.back();
  }, 1000);
}

</script>

<template>
  <router-view />
  <PlayList />
</template>
