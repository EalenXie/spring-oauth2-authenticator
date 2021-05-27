SpringBoot整合spring-security-oauth2完整实现例子
========================



技术栈 : springboot + spring-security + spring-oauth2 + mybatis-plus 

完整的项目地址 : 

[OAuth2.0](https://oauth.net/2/)是当下最主流的授权机制，如若不清楚什么是OAuth2.0，请移步[Oauth2详解-介绍(一)](https://www.jianshu.com/p/84a4b4a1e833)，[OAuth 2.0 的四种方式 - 阮一峰的网络日志](http://www.ruanyifeng.com/blog/2019/04/oauth-grant-types.html)等文章进行学习。

此例子基本完整实现了OAuth2.0四种授权模式。


### 1. 客户端凭证式(此模式不支持刷新令牌)

![](https://img2020.cnblogs.com/blog/994599/202102/994599-20210203140609030-750274907.png)


请求示例 : 
```
POST /oauth/token HTTP/1.1
Host: localhost:8080
Authorization: Basic QUJDOjEyMzQ1Ng==
Content-Type: application/x-www-form-urlencoded
Content-Length: 29

grant_type=client_credentials
```

此模式获取令牌接口 `grant_type`固定传值 <font color='red'>client_credentials</font>，客户端认证信息通过basic认证方式。


### 2. 用户密码模式

请求示例 : 

![](https://img2020.cnblogs.com/blog/994599/202102/994599-20210203140849090-463914185.png)


```
POST /oauth/token HTTP/1.1
Host: localhost:8080
Authorization: Basic QUJDOjEyMzQ1Ng==
Content-Type: application/x-www-form-urlencoded
Content-Length: 52

grant_type=password&username=ealenxie&password=admin
```
此模式获取令牌接口 `grant_type`固定传值 <font color='red'>password</font>并且携带用户名密码进行认证。~~~~


### 3. 授权码模式

此模式过程相对要复杂一些，首先需要认证过的用户先进行授权，获取到授权码code(通过回调url传递回来)之后，再向认证授权中心通过code去获取令牌。

#### 3.1 用户认证(登录)

请求示例  :
(本例子中笔者对此模式的第一步登录做了改造，用户登录授权服务器需要也进行basic认证，目的是在一个认证授权中心里面，为了确认客户端和用户均有效且能够建立信任关系)

![](https://img2020.cnblogs.com/blog/994599/202102/994599-20210203141150447-1796077322.png)

```
POST /login HTTP/1.1
Host: localhost:8080
Authorization: Basic QUJDOjEyMzQ1Ng==
Content-Type: application/x-www-form-urlencoded
Content-Length: 32

username=ealenxie&password=admin
```
认证成功后，会在浏览器写入cookie内容。


#### 3.2 获取授权码

请求示例 : 

```
GET /oauth/authorize?client_id=ABC&response_type=code&grant_type=authorization_code HTTP/1.1
Host: localhost:8080
Cookie: JSESSIONID=D329015F6B61C701BD69AE21CA5112C4
```

浏览器此接口调用成功后，会302到对应的redirect_uri,并且携带上code值。


#### 3.3 授权码模式获取令牌

获取到code之后，再次调用获取令牌接口

![](https://img2020.cnblogs.com/blog/994599/202102/994599-20210203141532941-1192533333.png)

```
POST /oauth/token HTTP/1.1
Host: localhost:8080
Authorization: Basic QUJDOjEyMzQ1Ng==
Content-Type: application/x-www-form-urlencoded
Content-Length: 90

grant_type=authorization_code&redirect_uri=http://localhost:9528/code/redirect&code=3EZOug
```

### 4. 简化模式

此模式首先需要认证过的用户(见3.1 用户认证)直接进行授权，浏览器此接口调用授权接口成功后，会直接302到对应的redirect_uri,并且携带上token值，此时token以<font color='red'>锚点</font>的形式返回。
本例子中我在后台配置 redirect_uri 假设为 www.baidu.com 如下 : 
![](https://img2020.cnblogs.com/blog/994599/202102/994599-20210203152616881-566304748.png)



![](https://img2020.cnblogs.com/blog/994599/202102/994599-20210203145138530-258931100.png)

### 5. 刷新令牌

本例中，设置的令牌有效期`access_token_validity`为7199秒，即两个小时。
刷新令牌的有效期`refresh_token_validity`为2592000秒，即30天。
当`access_token`过期且`refresh_token`未过期时，可以通过`refresh_token`进行刷新令牌，获取新的`access_token`和`refresh_token`

![](https://img2020.cnblogs.com/blog/994599/202102/994599-20210203151933958-120036858.png)

```
POST /oauth/token HTTP/1.1
Host: localhost:8080
Authorization: Basic QUJDOjEyMzQ1Ng==
Content-Type: application/x-www-form-urlencoded
Cookie: JSESSIONID=BC4B6A26370829BB3CAD6BED398F72C8
Content-Length: 391

grant_type=refresh_token&refresh_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9xxxx.....

```

此模式获取令牌接口 `grant_type`固定传值 <font color='red'>refresh_token</font>

### 6. 检查令牌是否有效

当需要进行确定令牌是否有效时，可以进行check_token
![](https://img2020.cnblogs.com/blog/994599/202102/994599-20210203152744359-1285977795.png)

```
POST /oauth/check_token?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiY2xvdWQtYXBpLXBsYXRmb3JtIl0sImV4cCI6MTYxMjM3OTkxMSwidXNlcl9uYW1lIjoiZWFsZW54aWUiLCJqdGkiOiJhZWVmMDhkZS02YTExLTQ3NDAtYTQzNS0wNTMyMThkYTMyYzkiLCJjbGllbnRfaWQiOiJBQkMiLCJzY29wZSI6WyJyZWFkIiwid3JpdGUiXX0.NPTkpwwdnaKSiPzUgILnnhjawgAuw-ZZWk_4HbkfYzM HTTP/1.1
Host: localhost:8080
Authorization: Basic QUJDOjEyMzQ1Ng==
Cookie: JSESSIONID=4838A3CFD6327A1644D1DAB0B095CC58

```

### 本例运行先决条件
    
1. 因为本例子中使用的数据库方式存储令牌，用户等等。需要准备spring_oauth2的相关数据表，执行本项目下的db脚本(里面配置了oauth2的基础表和客户端及用户账号信息)。
2. 运行项目



