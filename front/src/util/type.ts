import { AxiosResponse } from "axios";

export interface AnimeVo {
  id?: number,
  /* 动漫名 */
  name?: string,
  /* 最新集数 */
  latest?: string,
  /* 首播时间 */
  showTime?: string,
  /* 类型 */
  type?: string[],
  /* 链接 */
  link?: string,
  /* 简介 */
  info?: string,
  /* 图片 */
  image?: string,
}

export interface Video {
  id?: number,
  /* 动漫名称 */
  name?: string,
  /* 动漫集数 */
  episode?: string,
  /* 视频资源序列 */
  number?: number,
  /* 播放链接 */
  link?: string,
  /* 动漫id */
  animeId?: number
}

/* 分页类型 */
export interface PageInfo<T> {
  records: T[],
  pageNum: string,
  pageSize: string,
  total: string
}

/* 动漫搜索请求类型 */
export interface AnimeQto extends PageParam {
  name?: string,
  type?: string
}

export interface PageParam {
  pageNum?: number,
  pageSize?: number
}

export interface RespOne<T> {
  code: number,
  data: T,
  msg: string
}

export interface RespList<T> {
  code: number,
  data: T[],
  msg: string
}

export interface RespPage<T> {
  code: number,
  data: T,
  msg: string
}


export interface AxiosRespList<T> extends AxiosResponse<RespList<T>> { }

