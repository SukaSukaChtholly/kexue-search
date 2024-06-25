import { AnimeQto, AnimeVo, PageInfo, Play, RespList, RespPage } from '@/util/type';
import axios from '../util/request';

enum API {
  SEARCH_URL = '/search',
  GETPLAY_URL = '/getPlay'
}

export function reqSearch(anime: AnimeQto): Promise<RespPage<PageInfo<AnimeVo>>> {
  return axios.post(API.SEARCH_URL, anime, {
    headers: {
      'Content-Type': 'application/json'
    }
  });
}

export function reqGetPlay(animeId: number): Promise<RespList<Play>> {
  return axios.get(API.GETPLAY_URL, {
    params: {
      animeId
    }
  })
}