<script setup lang="ts">
import { reqGetVideo } from '@/api/searchApi';
import { setTitle } from '@/util/commonUtils';
import { Video } from '@/util/type';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

const animeId = useRoute().params.id as string;
const number = useRoute().params.number as string;
let link = ref('');
let isLoading = ref(true);
let title = ref('');




onMounted(async () => {
  isLoading.value = true;

  const localPlayList = localStorage.getItem('playList');
  if (localPlayList) {
    const videos = JSON.parse(localPlayList) as Video[];
    const currentVideo: any = videos.filter((v) => v.number === +number)[0];
    title.value = currentVideo.name + '-' + currentVideo.episode;
    setTitle(title.value);
  }

  const { data } = await reqGetVideo(animeId, number);
  link.value = data.link || '';

  isLoading.value = false;
})

</script>

<template>
  <!-- <iframe id="fc_playfram"
    src="/tpsf/player/dpx2/?hls=1&amp;url=https%3A%2F%2Fvip.ffzy-play6.com%2F20221212%2F17101_4b3a19be%2Findex.m3u8"
    height="100%" width="960px" scrolling="no" frameborder="0" allowfullscreen="true"></iframe> -->
  <div class="container flex">
    <div class="player">
      <VueSpinner class="loading" v-show="isLoading"/>
      <iframe id="iframe" :src="link" height="100%" width="100%" scrolling="no" allowfullscreen="true" frameborder="no"
        allowtransparency="true" referrerpolicy="no-referrer"></iframe>
    </div>

    <div class="title"><h3>{{ title }}</h3></div>
  </div>
</template>

<style scoped>
@import '@/assets/styles/layout.css';

.title h3 {
  position: absolute;
  background: linear-gradient(45deg, skyblue, rgb(236, 51, 51), skyblue);
  -webkit-background-clip: text;
  color: transparent;
  left: 0;
  bottom: 0;
  margin: 0;
}

.title {
  width: 100%;
  height: 2.5rem;
  position: relative;
}

.loading {
  width: 3.125rem;
  height: 3.125rem;
  color: skyblue;
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  margin: auto;
}

.player {
  width: 73.125rem;
  height: 41.25rem;
  background-color: black;
  position: relative;
}

.container {
  justify-content: center;
  width: 75rem;
  min-height: 12.5rem;
  margin: 1.25rem auto;
  padding: 1.5rem;
  border-radius: .625rem;
  background-color: white;
  flex-wrap: wrap;
}

@media screen and (max-width: 768px) {
  .title h3 {
      font-size: 1rem;
    }

  .title {
    height: 40px;
  }

  .player {
    width: 50rem;
    height: 12.5rem;
  }

  .container {
    width: 22rem;
    min-height: 11.25rem;
    padding: .3125rem;
    margin: .625rem auto;
  }
}
</style>
