import request from './request'

// 登录
export function login(username, password, code, uuid) {
  const data = {
    username,
    password,
    code,
    uuid,
  }
  return request({
    url: '/login',
    method: 'post',
    data,
  })
}

// 获取用户详细信息
export function getInfo(token) {
  return request({
    url: '/getInfo',
    method: 'get',
    params: { token },
  })
}

// 退出登录
export function logout(token) {
  return request({
    url: '/logout',
    method: 'post',
    data: { token },
  })
}

