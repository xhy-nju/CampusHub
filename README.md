# CampusHub

CampusHub 是一个校园互助服务平台，面向校内任务发布、任务接取、订单管理、评价信任等场景。

## 技术栈

- 前端：Vue、Element Plus、Pinia、Axios
- 后端：Spring Boot、MyBatis、JWT
- 数据库与中间件：PostgreSQL、Redis

## 仓库结构

```text
CampusHub/
├── backend/      # Spring Boot 后端
├── frontend/     # Vue 前端
├── database/     # 数据库脚本、迁移和种子数据
├── docs/         # 项目文档
└── screenshots/  # 运行截图、验收截图
```

## 初始化状态

已完成：

- Git 远程仓库已关联到 `https://github.com/xhy-nju/CampusHub.git`
- 已补充 `.gitignore`、`.gitattributes`、`.editorconfig`
- 已配置 GitHub Actions CI，用于后续自动检查前端和后端
- 已提供 PostgreSQL、Redis 的本地 Docker Compose 配置
- 已加入目录入口文件，避免空目录无法被 Git 追踪
- 已初始化 Spring Boot 后端工程
- 已完成后端健康检查、统一响应、全局异常处理、JWT 登录鉴权
- 已完成用户注册、登录、当前用户信息、个人资料修改接口
- 已初始化 Vue 3 + Vite 前端工程
- 已配置 Element Plus、Pinia、Axios、Vue Router、TypeScript

待完成：

- 实现登录、注册、个人中心页面
- 实现需求分类、需求发布与浏览
- 确定部署目标后再补充 CD 流程

更多检查项见 [docs/initialization-checklist.md](docs/initialization-checklist.md)。

## 开发入口

- [需求说明](docs/requirements.md)
- [开发流程清单](docs/development-roadmap.md)
- [数据库设计](docs/database-design.md)
- [API 草案](docs/api-contract.md)
- [ADR 目录](docs/adr/README.md)

建议从 [开发流程清单](docs/development-roadmap.md) 的“当前立即执行任务”开始推进。

## 本地开发

启动数据库和 Redis：

```bash
docker compose up -d
```

启动后端：

```bash
cd backend
mvn spring-boot:run
```

后端健康检查地址：

```text
http://localhost:8080/api/health
```

运行后端测试：

```bash
cd backend
mvn test
```

启动前端：

```bash
cd frontend
npm install
npm run dev
```

前端默认地址：

```text
http://localhost:5173
```

运行前端构建检查：

```bash
cd frontend
npm run build
```

如果 PostgreSQL 容器创建得早于 `database/init` 初始化脚本，可能出现 `relation "users" does not exist`。可以在仓库根目录补执行一次初始化脚本：

```bash
docker compose exec -T postgres psql -U campushub -d campushub -f /docker-entrypoint-initdb.d/001_schema.sql
docker compose exec -T postgres psql -U campushub -d campushub -f /docker-entrypoint-initdb.d/002_seed.sql
```
