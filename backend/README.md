# Backend

Spring Boot 后端工程目录。

建议后续约定：

- Java 17
- Spring Boot 3.x
- MyBatis / MyBatis-Plus
- JWT 认证
- PostgreSQL 作为主数据库
- Redis 用于缓存、验证码、会话或限流等场景

后续创建 Spring Boot 项目时，数据库驱动建议使用 PostgreSQL JDBC Driver。

## 已实现接口

- `GET /api/health`
- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/users/me`
- `PUT /api/users/me/profile`

## 本地启动

先在仓库根目录启动 PostgreSQL 和 Redis：

```bash
docker compose up -d
```

再启动后端：

```bash
cd backend
mvn spring-boot:run
```

健康检查：

```bash
curl http://localhost:8080/api/health
```

运行测试：

```bash
mvn test
```

注册示例：

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d "{\"studentNo\":\"student001\",\"password\":\"password123\",\"nickname\":\"Student\"}"
```

登录后访问当前用户：

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"studentNo\":\"student001\",\"password\":\"password123\"}"

curl http://localhost:8080/api/users/me \
  -H "Authorization: Bearer <token>"
```

如果本地库提示 `relation "users" does not exist`，说明 PostgreSQL 命名卷创建时还没有执行初始化脚本。可在仓库根目录执行：

```bash
docker compose exec -T postgres psql -U campushub -d campushub -f /docker-entrypoint-initdb.d/001_schema.sql
docker compose exec -T postgres psql -U campushub -d campushub -f /docker-entrypoint-initdb.d/002_seed.sql
```
