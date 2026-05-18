# CampusHub API 草案

所有接口默认以 `/api` 为前缀。

## 1. 通用约定

统一响应：

```json
{
  "code": 0,
  "message": "ok",
  "data": {}
}
```

认证方式：

```text
Authorization: Bearer <token>
```

## 2. 健康检查

- `GET /health`

## 3. 用户认证

- `POST /auth/register`：注册
- `POST /auth/login`：登录
- `GET /users/me`：获取当前用户
- `PUT /users/me/profile`：修改个人资料

### `POST /auth/register`

请求：

```json
{
  "studentNo": "student001",
  "password": "password123",
  "nickname": "Student",
  "college": "Software School",
  "contact": "student001@example.com"
}
```

响应：

```json
{
  "code": 0,
  "message": "ok",
  "data": {
    "tokenType": "Bearer",
    "accessToken": "<token>",
    "expiresInSeconds": 86400,
    "user": {
      "id": 1,
      "studentNo": "student001",
      "role": "user",
      "status": "active",
      "creditScore": 100,
      "profile": {
        "nickname": "Student",
        "avatarUrl": null,
        "college": "Software School",
        "contact": "student001@example.com",
        "bio": null
      }
    }
  }
}
```

### `POST /auth/login`

请求：

```json
{
  "studentNo": "student001",
  "password": "password123"
}
```

响应结构同注册接口。

### `GET /users/me`

请求头：

```text
Authorization: Bearer <token>
```

响应 `data` 为当前用户信息。

### `PUT /users/me/profile`

请求头：

```text
Authorization: Bearer <token>
```

请求：

```json
{
  "nickname": "New Name",
  "avatarUrl": "https://example.com/avatar.png",
  "college": "Software School",
  "contact": "student001@example.com",
  "bio": "Hello CampusHub"
}
```

## 4. 分类

- `GET /categories`：分类列表

## 5. 需求

- `POST /demands`：发布需求
- `GET /demands`：需求列表
- `GET /demands/{id}`：需求详情
- `PUT /demands/{id}`：修改需求
- `POST /demands/{id}/cancel`：取消需求

列表查询参数：

- `keyword`
- `categoryId`
- `status`
- `page`
- `size`

## 6. 订单

- `POST /demands/{id}/orders`：申请接单
- `POST /orders/{id}/confirm`：需求方确认服务方
- `POST /orders/{id}/provider-done`：服务方标记完成
- `POST /orders/{id}/complete`：需求方确认完成
- `POST /orders/{id}/cancel`：取消订单
- `GET /orders/requested`：我发布需求产生的订单
- `GET /orders/provided`：我接取的订单
- `GET /orders/{id}`：订单详情

## 7. 评价

- `POST /orders/{id}/reviews`：提交评价
- `GET /users/{id}/reviews`：查看用户评价

## 8. 通知

- `GET /notifications`：通知列表
- `GET /notifications/unread-count`：未读数量
- `POST /notifications/{id}/read`：标记已读

## 9. 管理后台

- `GET /admin/users`：用户列表
- `POST /admin/users/{id}/disable`：禁用用户
- `POST /admin/users/{id}/enable`：启用用户
- `GET /admin/demands`：需求管理列表
- `POST /admin/demands/{id}/approve`：审核通过
- `POST /admin/demands/{id}/reject`：审核拒绝
- `GET /admin/statistics`：统计数据
