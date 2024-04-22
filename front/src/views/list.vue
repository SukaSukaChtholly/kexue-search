<script setup lang="ts">
import { reqSearch } from "@/api/searchApi";
import { useAnimeQto } from "@/store/animeList";
import { AnimeQto, AnimeVo, PageInfo } from "@/util/type";
import { storeToRefs } from "pinia";
import { onMounted, ref } from "vue";

const webName = import.meta.env.VITE_API_WEB_NAME;

let isLoading = ref(false);
let count = ref(0);

let animeListStore = useAnimeQto();
let { animeQto } = storeToRefs(animeListStore);
let searchName = '';
let condition = ref('');

let animeList = ref<AnimeVo[]>([]);

onMounted(() => {
  const localAnimePage = localStorage.getItem("animePage");
  const localAnimeQto = localStorage.getItem("animeQto");

  animeQto.value = JSON.parse(localAnimeQto ?? '');
  setCondition(animeQto.value);
  document.title = `“${condition.value}”搜索结果 - ${webName}`;
  if (localAnimePage !== null) {
    let { total, records } = JSON.parse(localAnimePage);
    animeList.value = records;
    count = total;
    return;
  }

  search(animeQto.value);

})

function getWhenChange() {
  search(animeQto.value);
}

function handleSearch(name?: string, type?: string) {
  animeQto.value.name = name ?? '';
  animeQto.value.type = type ?? '';
  /* 将搜索框置空 */
  searchName = '';
  /* 重新定位到第一页 */
  animeQto.value.pageNum = 1;
  search(animeQto.value);
}

async function search(animeQto: AnimeQto) {
  isLoading.value = true;
  /* 回到最顶端 */
  window.scrollTo(0, 0);

  const { data } = await reqSearch(animeQto);
  setCondition(animeQto);
  animeQto.pageNum = data.pageNum;
  animeQto.pageSize = data.pageSize;

  animeList.value = data.records;
  count = ref(data.total);

  setLocalCache(data);
  /* 修改标题 */
  document.title = `“${condition.value}”搜索结果 - ${webName}`;
  isLoading.value = false;

}

function setCondition(animeQto: AnimeQto) {
  condition.value = animeQto.name || animeQto.type || '';
}

function setLocalCache(data: PageInfo<AnimeVo>) {
  clearLocalCache();
  localStorage.setItem("animeQto", JSON.stringify(animeQto.value));
  localStorage.setItem("animePage", JSON.stringify(data));
}

function clearLocalCache() {
  localStorage.removeItem("animeQto");
  localStorage.removeItem("animePage");
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
      <ul class="main">
        <VueSpinner class="loading" v-if="isLoading" />
        <div class="totalCount">搜索到与“{{ condition }}”相关的动漫有“{{ count ?? 0 }}”条</div>
        <li class="data-info" v-for="item of animeList" :key="item.id">
          <a :href="item.link">
            <img class="data-img img-error" referrerpolicy="no-referrer" :src="item.image" :alt="item.name"></a>
          <div class="data-content">
            <h4>
              <a :href="item.link" :title="item.name">{{ item.name }}</a>
            </h4>
            <ul>
              <li> <span>
                  <font color="red">{{ item.latest }}</font>
                </span></li>
              <li class="item-type"> <span>类型：<a href="#" @click.prevent="handleSearch('', t)"
                    v-for="t of item.type ">{{ t
                    }}</a></span>

              </li>
              <li>
                <span>简介：{{ item.info }}</span>
              </li>
              <li><a class="to-info" :href="item.link">查看详情</a></li>
            </ul>
          </div>
        </li>
        <el-pagination background prev-text="上一页" next-text="下一页" :total="count" :page-size="animeQto.pageSize"
          v-model:current-page="animeQto.pageNum" @current-change="getWhenChange" layout="prev, pager, next, jumper" />
      </ul>
    </div>
  </div>
  <div class="footer">
    <p>本网站提供的资源均系收集于各大视频网站，本网站只提供web页面服务，并不提供影片资源存储，也不参与录制、上传</p>
    <p>若本站收录的节目无意侵犯了贵司版权，请给网页底部邮箱地址来信，我会及时处理和回复，谢谢</p>
    <p>邮箱：123@qq.com</p>
  </div>

</template>

<style scoped>
:deep(li.is-active) {
  background-image: linear-gradient(45deg, skyblue, rgb(236, 51, 51), skyblue);
}

:deep(.el-pager li) {
  font-size: 1rem;
  font-family: 楷体;
  font-style: italic;
  padding-bottom: .125rem;
  margin: 0 .625rem !important;
}

:deep(.el-pagination) {
  font-size: 1rem;
  justify-content: center;
  margin: 2.5rem auto;
  flex-wrap: wrap;
}

.loading {
  width: 3.125rem;
  height: 3.125rem;
  margin-left: 40%;
  color: skyblue;
}

.item-type a {
  text-decoration: none;
  margin-left: .625rem;
}

.totalCount {
  font-size: 1.25rem;
  margin: 1.25rem 0;
}

.to-info {
  display: block;
  width: 6.25rem;
  height: 2.5rem;
  border: .0625rem solid transparent;
  border-radius: .3125rem;
  background-image: linear-gradient(45deg, skyblue, rgb(236, 51, 51), skyblue);
  text-decoration: none;
  text-align: center;
  line-height: 2.5rem;
  cursor: pointer;
  position: absolute;
  bottom: 0;
}


.data-content h4 a {
  text-decoration: none;
}

.data-content h4 {
  margin: 0;
  padding-left: .9375rem;
  font-size: 1.375rem;

}

.data-content ul {
  list-style: none;
  padding-left: .9375rem;
}

.data-content h4::before {
  content: "";
  display: block;
  border-top: .0625rem solid #e2e2e2;
  position: relative;
  bottom: .625rem;
}


.data-content li {
  padding-top: .625rem;
}

.data-content li span {
  display: -webkit-box;
  overflow: hidden;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  text-overflow: ellipsis;
}

.data-content {
  height: 88%;
  position: relative;
}

.data-info a:hover {
  color: #ff795e;
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

.data-info {
  width: 100%;
  height: 14rem;
  display: flex;
}

.main {
  width: 75rem;
  margin: 0 auto;
  margin-top: 1.25rem;
  padding-right: 2.5rem;
  background-color: white;
  border-radius: .625rem;
  overflow: hidden;
}

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
}

.footer {
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