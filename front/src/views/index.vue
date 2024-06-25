<script setup lang="ts">
import { AnimeQto } from "@/util/type";
import { useAnimeQto } from "@/store/animeList";
import router from "@/router";

const anime: AnimeQto = { name: '' }

function handleSearch() {
  const name = anime.name;
  if (!name) {
    alert("请输入动漫名称！");
    return;
  }
  const animeConditionStore = useAnimeQto();
  animeConditionStore.setCondition(anime);

  /* 跳转至查询展示页 */
  localStorage.removeItem("animePage");
  router.push(`/vod/list`);
}

</script>

<template>
  <div class="container">
    <div class="sform">
      <input id="search" type="text" placeholder="请输入动漫名称" v-model="anime.name" @keyup.enter="handleSearch" />
      <input id="submit" type="button" @click="handleSearch" value="搜索" />
    </div>
  </div>
</template>

<style scoped>
.container {
  width: auto;
  height: 100vh;
  display: flex;
  background-image: url('@/assets/images/keduoli.jpg');
  background-size: cover;
  background-repeat: no-repeat;
  overflow-x: hidden;
}

.sform {
  display: flex;
  /* 自动伸缩 */
  flex: 1;
  /* 主轴对齐 */
  justify-content: center;
  /* 侧轴对齐 */
  align-items: center;
  margin-bottom: 40vh;
}

#search {
  width: 36.25rem;
  height: 3.125rem;
  border-radius: .625rem;
  font-size: 1.5625rem;
  background-color: transparent;
  border-color: #e0e0e0;
  margin: 1.25rem;
}

#submit {
  width: 9.375rem;
  height: 3.125rem;
  font-size: 2.1875rem;
  background-color: #ff795e;
  border: 0;
  border-radius: .9375rem;
  margin-right: 1.25rem;
  cursor: pointer;
}

/* 在小屏幕上调整按钮样式 */
@media screen and (max-width: 768px) {
  #submit {
    width: 5rem;
    height: 3rem;
    font-size: 2rem;
  }

  #search {
    width: 15rem;
    height: 3rem;
    font-size: 2rem;
    margin-right: .625rem;
  }

  .container {
    background-image: url('@/assets/images/xiaokeduoli.jpg');
    background-position: center;
  }
}
</style>