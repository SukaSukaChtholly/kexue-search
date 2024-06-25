<script setup lang="ts">

import { reqGetPlay } from "@/api/searchApi";
import router from "@/router";
import { useAnimeQto } from "@/store/animeList";
import { AnimeVo, PageInfo, Play} from "@/util/type";
import { ElMessage } from "element-plus";
import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";

const animeListCache = localStorage.getItem('animePage');

if (!animeListCache) {
  setTimeout(() => {
    ElMessage.error("数据异常！");
    router.back();
  }, 1000);
}

const animeList = JSON.parse(animeListCache ?? '') as PageInfo<AnimeVo>;

const id = useRoute().params.id as string;
const anime = animeList.records.find(item => item.id === parseInt(id)) as AnimeVo;

let playList = ref<Play[]>([]);

onMounted(async () => {
  let { data } = await reqGetPlay(+id);
  playList.value = data;
})

function toPlay(link?: string) {
  router.push({
    path: `/player`,
    query: {
      link
    }
  })
}


function handleSearch(type: string) {
  const animeQto = { type: type };
  const animeConditionStore = useAnimeQto();
  animeConditionStore.setCondition(animeQto);

  /* 跳转至查询展示页 */
  localStorage.removeItem("animePage");
  router.push(`/vod/list`);
}

</script>

<template>
  <div class="part-layout">
    <div>
      <a href="#">
        <img class="data-img img-error" :src="anime.image" :alt="anime.name"></a>
    </div>
    <div class="data-info">
      <h4><a href="javascript:void(0)">{{ anime.name }}</a></h4>
      <ul>
        <li><span>更新至：{{ anime.latest }}</span></li>
        <li class="item-type"> <span>类型：<a @click="handleSearch(t)" v-for="t of anime.type" >{{ t }}</a></span>
        </li>
        <li>
          <input type="checkbox" id="toggle-checkbox" class="toggle-checkbox" />
          <label for="toggle-checkbox" class="toggle-button"></label>
          <span class="brief">简介：{{ anime.info }}</span>
        </li>

      </ul>
    </div>
  </div>
  <div class="block part-layout">
    <div>播放列表</div>
    <ul class="flex play-list"><li v-for="p of playList" :key="p.id">
      <a @click="toPlay(p.link)">{{ p.episode }}</a></li>
    </ul>
  </div>
</template>

<style scoped>
@import '@/assets/styles/layout.css';
@import '@/assets/styles/reset.css';

.play-list a:hover {
  background-image: linear-gradient(45deg, skyblue, rgb(236, 51, 51), skyblue);
}

.play-list a {
  display: block;
  width: 5.3125rem;
  height: 1.875rem;
  color: black;
}

.play-list li:nth-child(n + 13) {
  margin-top: .625rem;
}

.play-list li {
  width: 5.3125rem;
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
  justify-content: start;
  flex-wrap: wrap;
}

.toggle-button {
  color: skyblue;
  border: none;
  cursor: pointer;
  background-color: transparent;
  position: absolute;
  right: 0;
  bottom: -1rem;
}

.toggle-checkbox {
  display: none;
}

.toggle-checkbox:checked ~ .brief {
  -webkit-line-clamp: unset;
}

.toggle-checkbox:checked + .toggle-button::after {
  content: '折叠';
}

.toggle-checkbox:not(:checked) + .toggle-button::after {
  content: '展开';
}

.brief {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  /* 显示的行数 */
  -webkit-line-clamp: 3;
  overflow: hidden;
}


.data-info li {
  padding-top: .625rem;
}

.data-info ul {
  list-style: none;
  margin: 0;
  padding-left: 1rem;
  position: relative;
}

.item-type a {
  color: blue;
}

.item-type a ~ a {
  margin-left: .625rem;
}

.data-info a:hover {
  color: #ff795e;
  cursor: pointer;
}

.data-info a {
  text-decoration: none;
}

.data-info h4 {
  margin: 0;
  font-size: 1.375rem;
  padding-left: 1rem;
}

.data-img {
  width: 9rem;
  height: 13rem;
  background-size: cover;
}

.img-error {
  min-width: 9rem;
  min-height: 13rem;
  background-image: url("@/assets/images/img-error.png");
  background-size: 9rem 13rem;
  content: attr(alt);
  position: relative;
  left: 0;
}

.part-layout {
  width: 75rem;
  min-height: 12.5rem;
  margin: 1.25rem auto;
  padding: 1.5rem;
  border-radius: .625rem;
  background-color: white;
  display: flex;
}

@media screen and (max-width: 768px) {
  .part-layout {
      width: 22rem;
      min-height: 11.25rem;
      padding: .3125rem;
      margin: .625rem auto;
    }
  
    .data-img {
      width: 7.5rem;
      height: 11.25rem;
    }
  
    .img-error {
      min-width: 7.5rem;
      min-height: 11.25rem;
      background-size: 7.5rem 11.25rem;
    }

    .data-info h4 {
      font-size: 1rem;
      padding-left: 0.8rem;
    }

    .play-list li:nth-child(n + 5) {
      margin-top: .625rem;
    }

    .play-list li {
      width: 4.75rem;
    }

    .play-list a {
      width: 4.75rem;
    }
}
</style>