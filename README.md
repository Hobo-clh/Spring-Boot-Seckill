### SpringBoot+MyBatis+Redis+RabbitMQ实现秒杀系统！



#### 启动和运行

执行sql脚本

修改为你的配置文件信息（例如redis端口号等）

#### 主要功能描述

##### 1、明文密码使用两次MD5进行处理 

由前端使用MD5与固定的salt将表单中的密码加密，再传入后端服务器

后端服务器接收到后，再将其和随机生成的salt进行二次加密

将最后生成的加密密码和salt存入数据库中

- 第一次作用：防止用户名明文密码在网络中进行传输，增加安全性

- 第二次作用：防止数据库信息被盗窃，防止通过MD5反推出密码，双重保险

##### 2、使用Redis实现分布式Session    

[分布式Session实现基本思路](http://www.hobosocool.top/2020/07/19/分布式session/)

众所周知，Session的出现就是为了记录用户状态的

使用redis模仿session的实现

> 当用户在客户端发送请求登陆时，后端进行校验通过后使用UUID生成随机的token作为用户的唯一标识符，将其当作Key，用户信息当作Value存入redis中。
>
> 再将其添加至Cookie中保存在用户的浏览器上，当下次用户进行请求时，会附带上Cookie中的token信息，我们拿到此token去Redis中进行查询，返回用户信息。完成会话操作

##### 3、使用JSR303自定义参数校验

使用JSR303自定义校验器，实现对用户账号、密码的验证，使得验证逻辑从业务代码中脱离出来

##### 4、封装Jedis、封装缓存Key

将Jedis方法进行封装，使用模板方法模式封装缓存key

达到复用的状态，增强代码的灵活性

##### 5、Redis缓存设计

- 页面缓存：使用ThymeleafViewResolver手动渲染html页面，并存入redis中，加快访问速度

- 对象缓存：将用户信息、商品信息、订单信息等存入redis中

- Redis预减库存减少对数据库的访问

- 内存标记减少对Redis的访问

##### 6、RabbitMQ实现异步下单

- RabbitMQ队列缓冲，异步下单，增强用户体验
- 客户端使用js轮询检查是否秒杀成功

##### 7、秒杀接口地址隐藏

> 地址提前暴露秒由什么问题出现？
>
> - 如果秒杀接口地址提前暴露，容易被人提前下单购买
> - 或者使用机器人轮询发送秒杀请求，机器人一般比正常人手速更快
>
> 如何解决？
>
> - 使用**动态url**，加密算法加密随机的字符串生成url，然后通过前端代码获取url后台校验才能通过。　　

##### 8、数学公式验证码

> 随机生成数学公式验证码，验证正确才可进行秒杀操作
>
> 好处
>
> - 防止恶意机器人和爬虫
> - 分散用户的请求

##### 9、接口防刷（限流）

自定义注解AccessLimit，定义拦截器实现对接口的访问控制

例如： 表示在10s内，接口超过10次以上则返回服务繁忙提示，访问该接口需要登录

```java
    @AccessLimit(seconds = 20, maxCount = 10, needLogin = false)
    @GetMapping(value = "/goods/detail/{goodsId}")
    @ResponseBody
    public ResultDTO goodsDetail(User user, @PathVariable("goodsId") long goodsId) {...}
```





### PQS记录

|                          | 5000 * 10s |
| ------------------------ | ---------- |
| do_seckill               | QPS：390/  |
| redis优化版本 do_seckill | QPS：850   |
| 异步下单版本  do_seckill | QPS：1390  |

---
### 学习自慕课网若雨老师课程
[https://coding.imooc.com/class/168.html](https://coding.imooc.com/class/168.html)



#### 参考

[windows下安装RabbitMQ](https://www.cnblogs.com/nongzihong/p/11578255.html)





#### 在linux下进行测压 

```bash
apache-jmeter-5.3/bin/jmeter.sh -n -t miaosha_test.jmx -l result.jtl
```



