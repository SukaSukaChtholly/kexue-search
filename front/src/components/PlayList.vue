<script setup lang="ts">
import { reqGetPlayList } from '@/api/searchApi';
import router from '@/router';
import { Video } from '@/util/type';
import { ElMessage } from 'element-plus';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

const animeListCache = localStorage.getItem('animePage');

if (!animeListCache) {
  setTimeout(() => {
    ElMessage.error("数据异常！");
    router.back();
  }, 1000);
}

const id = useRoute().params.id as string;
const number = useRoute().params.number;

let videoInfos = ref < Video[] > ([]);


onMounted(async () => {
  const localPlayList = localStorage.getItem('playList');
  if (localPlayList) {
    videoInfos.value = JSON.parse(localPlayList);
    return;
  }

  const { data } = await reqGetPlayList(id);
  videoInfos.value = data;
  localStorage.setItem('playList', JSON.stringify(videoInfos.value));
})

function toPlay(v: Video) {
  const animeId = v.animeId;
  const number = v.number;
  router.push({
    path: `/vod/detail/${animeId}/player/${number}`,
  })
} 


</script>

<template>
  <div class="block part-layout">
    <div>播放列表</div>
    <ul class="flex play-list">
      <li v-for="v of videoInfos" :key="v.id" :class="{'selected': v.number === +number}">
        <a @click="toPlay(v)">{{ v.episode }}</a>
      </li>
    </ul>
  </div>
</template>

<style scoped>
@import '@/assets/styles/layout.css';
@import '@/assets/styles/reset.css';

.selected {
  background-image: linear-gradient(45deg, skyblue, rgb(236, 51, 51), skyblue);
}

.play-list a:hover {
  background-image: linear-gradient(45deg, skyblue, rgb(236, 51, 51), skyblue);
}

.play-list a {
  display: block;
  width: 5.4375rem;
  height: 1.875rem;
  color: black;
}

.play-list li:nth-child(n + 13) {
  margin-top: .625rem;
}

.play-list li:nth-child(12n) {
    margin-right: 0;
}

.play-list li:nth-child(12n + 1) {
    margin-left: .625rem;
}

.play-list li {
  width: 5.4375rem;
  height: 1.875rem;
  border: .0625rem solid #e2e2e2;
  border-radius: .1875rem;
  margin-right: .625rem;
  text-align: center;
  line-height: 1.875rem;
}

.play-list {
  padding-left: 0;
  list-style: none;
  flex-wrap: wrap;
}

.part-layout {
  width: 75rem;
  margin: 1.25rem auto;
  padding: 1.5rem;
  border-radius: .625rem;
  background-color: white;
  display: flex;
}

@media screen and (max-width: 768px) {
  .part-layout {
    width: 22rem;
    padding: .3125rem;
    margin: .625rem auto;
  }

  .play-list li:nth-child(12n + 1) {
        margin-left: 0;
  }

  .play-list li:nth-child(n + 5) {
    margin-top: .625rem;
  }

  .play-list li:nth-child(4n) {
    margin-right: 0;
  }

  .play-list li:nth-child(4n + 1) {
    margin-left: .25rem;
  }

  .play-list li {
    width: 4.75rem;
  }

  .play-list a {
    width: 4.75rem;
  }
}
</style>