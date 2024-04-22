import { AnimeQto, AnimeVo, PageInfo, RespPage } from '@/util/type';
import axios from '../util/request';

enum API {
  SEARCH_URL = "/search"
}

export function reqSearch(anime: AnimeQto): Promise<RespPage<PageInfo<AnimeVo>>> {
  return axios.post(API.SEARCH_URL, anime, {
    headers: {
      'Content-Type': 'application/json'
    }
  });
}
