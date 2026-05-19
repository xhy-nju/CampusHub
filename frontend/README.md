# Frontend

Vue 前端工程目录，基于 Vue 3 + Vite 初始化。

## 技术栈

- Vue 3
- Vite
- Node.js 24 LTS
- Element Plus
- Pinia
- Axios
- Vue Router
- TypeScript

## 本地启动

安装依赖：

```bash
npm install
```

启动开发服务器：

```bash
npm run dev
```

默认访问地址：

```text
http://localhost:5173
```

构建检查：

```bash
npm run build
```

## 环境变量

本地开发默认通过 Vite 代理访问后端：

```env
VITE_API_BASE_URL=/api
VITE_BACKEND_PROXY_TARGET=http://localhost:8080
```

后端需要先启动在 `http://localhost:8080`。如果部署到真实环境，可以把 `VITE_API_BASE_URL` 改成完整 API 地址。

## 当前结构

```text
src/
├── api/       # Axios 实例和接口请求
├── router/    # Vue Router 路由和守卫
├── stores/    # Pinia 状态
├── styles/    # 全局样式
├── types/     # 前端类型定义
└── views/     # 页面入口，登录页与后续业务页面
```

## 当前页面

- `/`：自动跳转到登录页
- `/login`：登录页，已接入后端登录接口和 Pinia 登录状态
- `/register`、`/me`：暂为占位页，后续按注册与个人中心流程继续开发
