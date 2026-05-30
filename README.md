# 实验三：基于 Vue3 + Servlet 的房产销售系统

本仓库是前后端分离单仓库项目：
- `frontend/`：Vue3 + ElementPlus + axios
- `backend/`：Jakarta Servlet（Tomcat 10.x）+ JDBC + Druid + MySQL 8.5
- `sql/`：建表与初始化数据

## 一、环境要求
- MySQL 8.5
- JDK 21
- Tomcat 10.x
- Node.js 18+
- Maven 3.9+

## 二、数据库初始化
1. 在 MySQL 中执行：
   - `/tmp/workspace/Carrd-cpu/shiyan3/sql/schema.sql`
   - `/tmp/workspace/Carrd-cpu/shiyan3/sql/data.sql`
2. 修改后端数据库配置：
   - `/tmp/workspace/Carrd-cpu/shiyan3/backend/src/main/resources/db.properties`
   - 填写 `username/password`

默认账号：
- 管理员：admin / admin123
- 普通用户：user1 / user123

## 三、后端启动（IDEA + Tomcat）
1. IDEA 打开 `backend/` 为 Maven 项目。
2. 确认 JDK 使用 21。
3. Maven 打包（可选）：`mvn clean package`
4. 配置 Tomcat 10.x，部署 `backend:war`。
5. 启动后端，接口基础路径：
   - `http://localhost:8080/backend/api`

## 四、前端启动
```bash
cd /tmp/workspace/Carrd-cpu/shiyan3/frontend
npm install
npm run dev
```
默认访问：`http://localhost:5173`

## 五、已实现功能
### 1. 用户端（user）
- 登录/退出（Session）
- 房源列表（区域、价格区间、户型、关键字筛选 + 分页）
- 房源详情
- 收藏/取消收藏
- 收藏列表
- 个人信息查看与修改

### 2. 管理端（admin）
- 房源管理（CRUD + 分页）
- 审核列表（待审核）
- 审核操作（通过/驳回，驳回原因记录）
- 用户列表与启停管理

### 3. 安全与架构
- 严格 MVC：
  - View：Vue 页面
  - Controller：Servlet
  - Model：Entity + DAO + Service + MySQL + Druid
- 登录失败次数限制：
  - 同一账号连续失败达到 5 次锁定 10 分钟（信息落库）
  - 成功登录后清零
- 统一 JSON 返回：`{ code, message, data }`
- 统一分页数据：`{ total, list }`
- 审核状态：`PENDING / APPROVED / REJECTED / OFFLINE`
- 用户侧仅可见 `APPROVED`

## 六、前后端接口说明（核心）
- 认证：`/auth/login` `/auth/logout` `/auth/me`
- 用户房源：`GET /houses` `GET /houses/{id}`
- 收藏：`GET /favorites` `POST /favorites/{houseId}` `DELETE /favorites/{houseId}`
- 个人信息：`GET/PUT /users/profile`
- 管理：
  - 房源管理：`GET/POST /admin/houses` `PUT/DELETE /admin/houses/{id}`
  - 审核：`GET /admin/reviews` `POST /admin/reviews/{id}`
  - 用户：`GET /admin/users` `PUT /admin/users/{id}/status`
