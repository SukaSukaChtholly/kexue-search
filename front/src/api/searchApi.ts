import { AnimeQto, AnimeVo, PageInfo, Video, RespList, RespPage, RespOne } from '@/util/type';
import axios from '../util/request';

enum API {
  SEARCH_URL = '/search',
  GETPLAYLIST_URL = '/getPlayList',
  GETVideo_URL = '/getVideo'
}

export function reqSearch(anime: AnimeQto): Promise<RespPage<PageInfo<AnimeVo>>> {
  return axios.post(API.SEARCH_URL, anime, {
    headers: {
      'Content-Type': 'application/json'
    }
  });
}

export function reqGetPlayList(animeId: string): Promise<RespList<Video>> {
  return axios.get(API.GETPLAYLIST_URL, {
    params: {
      animeId
    }
  })
}

export function reqGetVideo(animeId: string, number: string): Promise<RespOne<Video>> {
  return axios.get(API.GETVideo_URL, {
    params: {
      animeId,
      number
    }
  })
}