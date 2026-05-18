# CampusHub 数据库设计

数据库使用 PostgreSQL。初版 schema 位于 `database/init/001_schema.sql`。

## 1. 核心实体

```text
users 1--1 user_profiles
users 1--n demands
categories 1--n demands
demands 1--1 orders
orders 1--n reviews
users 1--n credit_records
users 1--n notifications
```

## 2. 表说明

### users

用户账号表，保存登录和状态信息。

核心字段：

- `id`
- `student_no`
- `password_hash`
- `role`
- `status`
- `credit_score`

### user_profiles

用户资料表。

核心字段：

- `user_id`
- `nickname`
- `avatar_url`
- `college`
- `contact`
- `bio`

### categories

需求分类表。

初始分类：

- 快递代取
- 学习辅导
- 二手交易
- 活动组队
- 其他

### demands

需求表。

核心字段：

- `publisher_id`
- `category_id`
- `title`
- `description`
- `location_text`
- `expected_time`
- `reward`
- `status`

### orders

订单表。

核心字段：

- `demand_id`
- `requester_id`
- `provider_id`
- `status`
- `confirmed_at`
- `provider_finished_at`
- `completed_at`

### reviews

评价表。

核心字段：

- `order_id`
- `reviewer_id`
- `reviewee_id`
- `rating`
- `content`

约束：

- 同一订单中，同一个用户只能评价一次
- 评分范围为 1-5

### credit_records

信用分变更记录。

核心字段：

- `user_id`
- `order_id`
- `delta`
- `reason`

### notifications

站内通知表。

核心字段：

- `user_id`
- `type`
- `title`
- `content`
- `read_at`

## 3. 状态枚举

### demand_status

- `open`：可接单
- `pending_confirm`：等待需求方确认服务方
- `in_progress`：进行中
- `provider_done`：服务方已标记完成
- `completed`：已完成
- `cancelled`：已取消
- `rejected`：审核拒绝

### order_status

- `pending_confirm`
- `in_progress`
- `provider_done`
- `completed`
- `cancelled`

### user_status

- `active`
- `disabled`

### user_role

- `user`
- `admin`

## 4. 后续演进

后续可增加：

- `messages`：即时通讯
- `favorites`：收藏
- `demand_audit_records`：审核记录
- `user_behavior_logs`：推荐行为记录

