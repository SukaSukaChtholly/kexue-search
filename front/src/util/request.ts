import axios from "axios";
import router from "../router";


const _axios = axios.create({
  baseURL: /* import.meta.env.VITE_BACKEND_API_BASE_URL + */ '/api',
  // timeout: 10000
})

// 请求拦截器
_axios.interceptors.request.use(
  (config) => {
    return config
  },
  (error) => {
    console.log(error);
    return Promise.reject(error);
  }
)

// 响应拦截器
export default _axios;
_axios.interceptors.response.use(
  (response) => {
    const result = response.data;
    /* 服务器错误，返回首页 */
    if (result.code === 500) {
      console.log(result.msg);
      router.push(`/`);
      return Promise.resolve();
    }
    return result;
  },
  (error) => {
    console.log(error);
    return Promise.reject(error);
  }
)