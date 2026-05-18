# 初始化检查清单

## 当前结论

本仓库已经完成 Git 远程关联，但在本次补充前只提交了 `README.md` 和空 `.gitignore`。由于 Git 不追踪空目录，`backend`、`frontend`、`database`、`docs`、`screenshots` 原本不会真正同步到 GitHub。

## 已补充配置

- `.gitignore`：覆盖 Vue、Node、Spring Boot、Maven、Gradle、数据库备份、日志、环境变量和 IDE 临时文件
- `.gitattributes`：统一换行规则，减少 Windows/macOS/Linux 协作时的无意义 diff
- `.editorconfig`：统一缩进、编码、换行和 Markdown 空格规则
- `.github/workflows/ci.yml`：配置 GitHub Actions CI
- `backend/.env.example`、`frontend/.env.example`：提供本地环境变量模板
- `docker-compose.yml`：提供 PostgreSQL、Redis 本地开发环境
- 目录说明文件：确保关键目录能被 Git 追踪

## CI 说明

当前 CI 会在 `main` 分支 push 和 pull request 时运行。

- 如果 `backend` 目录中存在 `pom.xml`、`mvnw`、`build.gradle`、`build.gradle.kts` 或 `gradlew`，CI 会使用 Java 17 运行后端测试
- 如果 `frontend` 目录中存在 `package.json`，CI 会使用 Node.js 24 安装依赖，并依次尝试运行 `lint`、`test`、`build`
- 在前后端项目尚未脚手架化之前，对应检查会自动跳过，避免空项目阶段误报失败

## 仍建议尽快完成

1. 初始化 Vue 项目，并提交 `package.json` 和锁文件
2. 初始化 Spring Boot 项目，并提交 Maven Wrapper 或 Gradle Wrapper
3. 补充数据库建表脚本和本地开发种子数据
4. 根据后续后端配置完善 PostgreSQL、Redis 的连接参数和初始化脚本
5. 确定 CD 目标环境，例如云服务器、Docker 镜像仓库、GitHub Pages 或学校服务器
6. 配置 GitHub 分支保护，要求 PR 合并前通过 CI

## CD 需要的信息

真正的 CD 需要先确定部署方式。建议至少明确：

- 前端部署到哪里：静态服务器、Nginx、对象存储、GitHub Pages 或其他平台
- 后端部署到哪里：云服务器、容器平台或学校服务器
- 是否使用 Docker 镜像
- GitHub Secrets 中保存哪些密钥，例如服务器地址、SSH 私钥、镜像仓库账号

在这些信息确定前，不建议提交只会打印日志的假 CD。
