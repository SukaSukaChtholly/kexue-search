<script setup lang="ts">
import router from '@/router';
import { useAnimeQto } from '@/store/animeList';
import { setTitle } from '@/util/commonUtils';
import { AnimeVo, PageInfo } from '@/util/type';
import { useRoute } from 'vue-router';

const animeListCache = localStorage.getItem('animePage');

const animeList = JSON.parse(animeListCache ?? '') as PageInfo<AnimeVo>;

const id = useRoute().params.id as string;
const anime = animeList.records.find(item => item.id == parseInt(id)) as AnimeVo;

setTitle(anime.name);

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
        <li>首播时间：{{ anime.showTime }}</li>
        <li><span>更新至：{{ anime.latest }}</span></li>
        <li class="item-type"> <span>类型：<a @click="handleSearch(t)" v-for="t of anime.type">{{ t }}</a></span>
        </li>
        <li>
          <input type="checkbox" id="toggle-checkbox" class="toggle-checkbox" />
          <label for="toggle-checkbox" class="toggle-button"></label>
          <span class="brief">简介：{{ anime.info }}</span>
        </li>
      </ul>
    </div>
  </div>
</template>

<style scoped>
@import '@/assets/styles/layout.css';
@import '@/assets/styles/reset.css';

.item-type a {
  color: blue;
}

.item-type a ~ a {
  margin-left: .625rem;
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

.brief {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  /* 显示的行数 */
  -webkit-line-clamp: 3;
  overflow: hidden;
}

.toggle-checkbox:checked + .toggle-button::after {
  content: '折叠';
}

.toggle-checkbox:not(:checked) + .toggle-button::after {
  content: '展开';
}

.toggle-checkbox:checked + .toggle-button::after {
  content: '折叠';
}

.toggle-checkbox:not(:checked) + .toggle-button::after {
  content: '展开';
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


.data-info li {
  padding-top: .625rem;
}

.data-info ul {
  list-style: none;
  margin: 0;
  padding-left: 1rem;
  position: relative;
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
}
</style>