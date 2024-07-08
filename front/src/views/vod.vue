<script setup lang="ts">
import router from "@/router";
import { useAnimeQto } from "@/store/animeList";
import { ElMessage } from "element-plus";
import { storeToRefs } from "pinia";

const webName = import.meta.env.VITE_API_WEB_NAME;

let animeListStore = useAnimeQto();
let { animeQto } = storeToRefs(animeListStore);
let searchName = '';

function handleSearch(name?: string) {
  if (!name) {
    ElMessage.error("请输入查询条件！");
    return;
  }

  animeQto.value.name = name ?? '';
  /* 将搜索框置空 */
  searchName = '';
  /* 重新定位到第一页 */
  animeQto.value.pageNum = 1;
  clearLocalCache();

  router.push({path: `/vod/list`, query: animeQto.value});
}

function clearLocalCache() {
  localStorage.getItem("animeQto") && localStorage.removeItem("animeQto");
  localStorage.getItem("animePage") && localStorage.removeItem("animePage");
}

</script>


<template>
  <div class="container min-height-vh">
    <div class="header">
      <div class="title">
        <a href="/" :title="webName">{{ webName }}</a>
      </div>
      <div class="sform">
        <input id="search" type="text" v-model="searchName" @keyup.enter="handleSearch(searchName)" />
        <input id="submit" type="submit" value="搜索" @click="handleSearch(searchName)" />
      </div>
    </div>
    <div class="content">
      <router-view :key="$route.fullPath"/>
    </div>
    <div class="footer">
      <p>本网站提供的资源均系收集于各大视频网站，本网站只提供web页面服务，并不提供影片资源存储，也不参与录制、上传</p>
      <p>若本站收录的节目无意侵犯了贵司版权，请给网页底部邮箱地址来信，我会及时处理和回复，谢谢</p>
      <p>邮箱：123@qq.com</p>
    </div>
  </div>


</template>

<style scoped>
.content {
  width: 100%;
  margin: 0 auto;
  padding-top: 3.75rem;
  overflow: hidden;
}

.title {
  width: 8rem;
  font-size: 1.875rem;
  margin-top: .6rem;
  white-space: nowrap;
  justify-content: center;
}

.title a {
  color: skyblue;
  text-decoration: none;
}

#submit {
  width: 5rem;
  height: 2.1875rem;
  font-size: 1.25rem;
  border-width: .0625rem;
  border-radius: .5rem;
  cursor: pointer;
  background-color: #ff795e;
  margin-right: 6.25rem;
}

#search {
  width: 18.125rem;
  height: 1.875rem;
  font-size: 1rem;
  border: .125rem grid #e0e0e0;
  background-color: transparent;
  border-radius: .625rem;
  margin-right: .5rem;
}

.sform {
  height: 100%;
  display: flex;
  flex: 0, 1, 18.75rem;
  justify-content: center;
  align-items: center;
}

.header {
  width: 100%;
  height: 3.75rem;
  display: flex;
  /* flex: 1; */
  justify-content: center;
  position: fixed;
  z-index: 999;
  background-color: white;
}


.container {
  display: flex;
  flex-direction: column;
  background-image: url('@/assets/images/keduoli-kaochuang.jpg');
  background-size: cover;
  background-repeat: no-repeat;
  background-attachment: fixed;
  position: relative;
}

.footer {
  width: 100%;
  font-family: 宋体;
  border-radius: .3125rem;
  text-align: center;
  font-size: .9375rem;
  background-color: skyblue;
}

.footer p {
  line-height: 2em;
  margin: 0;
}

.min-height-vh {
  min-height: 100vh;
}

@media screen and (max-width: 768px) {
  .item-type a {
    margin-left: .25rem;
  }

  .title {
    display: none;
  }

  #search {
    width: 15rem;
    margin-left: 6.25rem;
  }

  .totalCount {
    font-size: 1rem;
  }

  .main {
    font-size: .75rem;
    width: 21rem;
    padding: 0 .5rem;
  }

  .data-content h4 a {
    font-size: 1rem;
  }

  .data-content li {
    padding-top: .25rem;
  }

  .data-img {
    width: 4.5rem;
    height: 6.5rem;
  }

  :deep(.el-pager) {
    margin: .25rem 0;
  }

  :deep(.el-pager li) {
    font-size: .75rem;
    padding-bottom: .125rem;
    margin: 0 .1875rem !important;
  }

  :deep(.el-pagination) {
    flex-wrap: wrap;
  }

}
</style>