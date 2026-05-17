# Database

数据库脚本目录，默认面向 PostgreSQL。

建议后续放置：

- `schema.sql`：PostgreSQL 表结构
- `seed.sql`：本地开发种子数据
- `init/`：本地 PostgreSQL 容器首次启动时自动执行的初始化脚本
- `migrations/`：版本化迁移脚本

不要提交真实生产数据、数据库备份或包含密码的连接配置。
