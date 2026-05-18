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
