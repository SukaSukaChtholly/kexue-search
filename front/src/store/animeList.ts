import { defineStore } from "pinia";
import { AnimeQto } from "@/util/type";

export const useAnimeQto = defineStore('animeConditionStore', {
  state: () => {
    return {
      animeQto: <AnimeQto>{}
    };
  },
  actions: {
    setCondition(anime: AnimeQto) {
      this.animeQto = anime;
      localStorage.setItem("animeQto", JSON.stringify(this.animeQto));
    }
  }
})