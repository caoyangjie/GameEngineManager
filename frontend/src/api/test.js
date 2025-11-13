import request from './request'

// 测试接口
export function testHello() {
  return request({
    url: '/test/hello',
    method: 'get',
  })
}

