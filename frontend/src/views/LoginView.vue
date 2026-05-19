<script setup lang="ts">
import { Lock, View, Hide, User, Star, Lightning } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { computed, reactive, ref } from 'vue'
import { RouterLink, useRoute, useRouter } from 'vue-router'

import campusIllustration from '@/assets/login-campus.svg'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const form = reactive({
  studentNo: '',
  password: '',
  remember: false
})

const loading = ref(false)
const showPassword = ref(false)

const passwordType = computed(() => (showPassword.value ? 'text' : 'password'))

async function handleLogin() {
  if (!form.studentNo.trim() || !form.password) {
    ElMessage.warning('请输入学号和密码')
    return
  }

  loading.value = true

  try {
    await authStore.login({
      studentNo: form.studentNo.trim(),
      password: form.password
    })

    ElMessage.success('登录成功')
    const redirect = typeof route.query.redirect === 'string' ? route.query.redirect : '/me'
    await router.push(redirect)
  } catch {
    ElMessage.error('学号或密码错误')
  } finally {
    loading.value = false
  }
}

function handleSsoLogin() {
  ElMessage.info('统一身份认证后续接入')
}

const featureItems = [
  {
    title: '安全可靠',
    description: '多重安全防护，保障账号安全',
    icon: Lock,
    tone: 'blue'
  },
  {
    title: '互助友爱',
    description: '连接校园互助，温暖彼此',
    icon: Star,
    tone: 'purple'
  },
  {
    title: '高效便捷',
    description: '快速发布需求，及时获得帮助',
    icon: Lightning,
    tone: 'green'
  }
]
</script>

<template>
  <main class="login-page">
    <section class="login-shell">
      <div class="brand-panel">
        <header class="brand-header">
          <div class="brand-mark" aria-hidden="true">
            <svg viewBox="0 0 48 48" role="img">
              <path d="M24 4 41.3 14v20L24 44 6.7 34V14L24 4Z" fill="currentColor" />
              <path
                d="M13.2 19.3 24 13.8l10.8 5.5L24 24.7 13.2 19.3Z"
                fill="none"
                stroke="white"
                stroke-linejoin="round"
                stroke-width="3"
              />
              <path
                d="M15.6 21.2v8.3c5.6 4 11.2 4 16.8 0v-8.3"
                fill="none"
                stroke="white"
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="3"
              />
            </svg>
          </div>
          <div class="brand-name">
            <strong>CampusHub</strong>
            <span>校园互助平台</span>
          </div>
        </header>

        <div class="welcome-copy">
          <h1>欢迎回来</h1>
          <p>校园互助，让学习更轻松，让生活更美好</p>
        </div>

        <img class="campus-illustration" :src="campusIllustration" alt="" />

        <div class="feature-list" aria-label="平台特点">
          <article
            v-for="item in featureItems"
            :key="item.title"
            class="feature-item"
            :class="`feature-item--${item.tone}`"
          >
            <span class="feature-icon">
              <component :is="item.icon" />
            </span>
            <span>
              <strong>{{ item.title }}</strong>
              <small>{{ item.description }}</small>
            </span>
          </article>
        </div>
      </div>

      <section class="login-card" aria-labelledby="login-title">
        <div class="login-card__header">
          <h2 id="login-title">登录</h2>
          <p>登录你的 CampusHub 账号</p>
        </div>

        <form class="login-form" @submit.prevent="handleLogin">
          <label class="field">
            <User class="field__icon" />
            <input v-model="form.studentNo" autocomplete="username" placeholder="学号" type="text" />
          </label>

          <label class="field">
            <Lock class="field__icon" />
            <input
              v-model="form.password"
              autocomplete="current-password"
              placeholder="密码"
              :type="passwordType"
            />
            <button
              class="field__suffix"
              type="button"
              :aria-label="showPassword ? '隐藏密码' : '显示密码'"
              @click="showPassword = !showPassword"
            >
              <Hide v-if="showPassword" />
              <View v-else />
            </button>
          </label>

          <div class="form-row">
            <label class="remember-option">
              <input v-model="form.remember" type="checkbox" />
              <span>记住我</span>
            </label>
            <button class="text-button" type="button">忘记密码?</button>
          </div>

          <button class="primary-button" :disabled="loading" type="submit">
            {{ loading ? '登录中...' : '登录' }}
          </button>
        </form>

        <div class="divider">
          <span></span>
          <em>或</em>
          <span></span>
        </div>

        <button class="sso-button" type="button" @click="handleSsoLogin">
          <span class="sso-badge">S</span>
          使用统一身份认证登录
        </button>

        <p class="register-entry">
          还没有账号？
          <RouterLink to="/register">立即注册</RouterLink>
        </p>
      </section>
    </section>

    <footer class="login-footer">
      <span>© 2024 CampusHub 校园互助平台</span>
      <i></i>
      <a href="#">帮助中心</a>
      <i></i>
      <a href="#">隐私政策</a>
      <i></i>
      <a href="#">服务条款</a>
    </footer>
  </main>
</template>

<style scoped>
.login-page {
  display: grid;
  min-height: 100vh;
  padding: clamp(24px, 4vh, 46px) 70px 22px;
  color: #0f1d35;
  background:
    linear-gradient(135deg, rgba(232, 238, 255, 0.92) 0%, rgba(249, 251, 255, 0.98) 48%, #ffffff 100%),
    #f7f9ff;
  border: 1px solid #dfe7f5;
  border-radius: 28px;
}

.login-shell {
  display: grid;
  grid-template-columns: minmax(0, 1.2fr) minmax(420px, 0.76fr);
  gap: 50px;
  align-self: center;
  width: min(100%, 1568px);
  min-height: min(742px, calc(100vh - 112px));
  margin: 0 auto;
  padding: clamp(42px, 5vh, 56px) 78px clamp(34px, 4vh, 46px);
  background: rgba(255, 255, 255, 0.78);
  border: 1px solid rgba(222, 230, 246, 0.86);
  border-radius: 28px;
  box-shadow: 0 28px 80px rgba(101, 126, 172, 0.18);
  backdrop-filter: blur(18px);
}

.brand-panel {
  position: relative;
  display: grid;
  grid-template-rows: auto auto minmax(0, 1fr) auto;
  min-width: 0;
}

.brand-header {
  display: flex;
  align-items: center;
  gap: 20px;
}

.brand-mark {
  display: grid;
  width: 68px;
  height: 68px;
  color: #1f6fff;
  filter: drop-shadow(0 12px 18px rgba(34, 103, 255, 0.24));
  place-items: center;
}

.brand-mark svg {
  width: 100%;
  height: 100%;
}

.brand-name {
  display: flex;
  align-items: baseline;
  gap: 12px;
  font-size: 24px;
  line-height: 1;
}

.brand-name strong {
  color: #1e68f6;
  font-size: 30px;
  font-weight: 800;
}

.brand-name span {
  color: #12213a;
  font-weight: 700;
}

.welcome-copy {
  margin-top: 34px;
}

.welcome-copy h1 {
  margin: 0;
  color: #091731;
  font-size: 54px;
  font-weight: 900;
  line-height: 1.12;
}

.welcome-copy p {
  margin: 14px 0 0;
  color: #607088;
  font-size: 22px;
  font-weight: 600;
}

.campus-illustration {
  align-self: end;
  width: min(100%, 830px);
  margin: 4px 0 0 -72px;
  user-select: none;
  pointer-events: none;
}

.feature-list {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 28px;
  margin-top: 10px;
  padding: 0 12px;
}

.feature-item {
  display: grid;
  grid-template-columns: 46px minmax(0, 1fr);
  gap: 14px;
  align-items: center;
}

.feature-icon {
  display: grid;
  width: 46px;
  height: 46px;
  border-radius: 50%;
  place-items: center;
}

.feature-icon svg {
  width: 24px;
  height: 24px;
}

.feature-item strong,
.feature-item small {
  display: block;
}

.feature-item strong {
  color: #28364e;
  font-size: 17px;
  font-weight: 800;
}

.feature-item small {
  margin-top: 5px;
  color: #75849b;
  font-size: 13px;
  line-height: 1.45;
}

.feature-item--blue .feature-icon {
  color: #2e73ff;
  background: #e7efff;
}

.feature-item--purple .feature-icon {
  color: #884fff;
  background: #efe8ff;
}

.feature-item--green .feature-icon {
  color: #24b47e;
  background: #def5ea;
}

.login-card {
  align-self: center;
  width: 100%;
  max-width: 614px;
  padding: 38px 46px 36px;
  background: rgba(255, 255, 255, 0.96);
  border: 1px solid #e5ebf5;
  border-radius: 20px;
  box-shadow: 0 24px 48px rgba(38, 55, 88, 0.12);
}

.login-card__header h2 {
  margin: 0;
  color: #10203a;
  font-size: 36px;
  font-weight: 900;
  line-height: 1.2;
}

.login-card__header p {
  margin: 12px 0 0;
  color: #7a879d;
  font-size: 19px;
  font-weight: 600;
}

.login-form {
  display: grid;
  gap: 18px;
  margin-top: 26px;
}

.field {
  display: grid;
  grid-template-columns: 28px minmax(0, 1fr) 28px;
  align-items: center;
  height: 56px;
  padding: 0 20px;
  background: #ffffff;
  border: 1px solid #d9e1ee;
  border-radius: 7px;
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.72);
  transition:
    border-color 0.18s ease,
    box-shadow 0.18s ease;
}

.field:focus-within {
  border-color: #2d73ff;
  box-shadow: 0 0 0 3px rgba(45, 115, 255, 0.12);
}

.field__icon,
.field__suffix svg {
  width: 24px;
  height: 24px;
  color: #68778e;
}

.field input {
  width: 100%;
  min-width: 0;
  border: 0;
  outline: 0;
  padding: 0 12px;
  color: #12213a;
  background: transparent;
  font-size: 18px;
  font-weight: 600;
}

.field input::placeholder {
  color: #8c98ab;
}

.field__suffix {
  display: grid;
  width: 30px;
  height: 30px;
  padding: 0;
  border: 0;
  color: inherit;
  background: transparent;
  cursor: pointer;
  place-items: center;
}

.form-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.remember-option {
  display: inline-flex;
  align-items: center;
  gap: 12px;
  color: #24334a;
  font-size: 16px;
  font-weight: 700;
}

.remember-option input {
  width: 21px;
  height: 21px;
  margin: 0;
  accent-color: #246dff;
}

.text-button {
  padding: 0;
  border: 0;
  color: #1f6fff;
  background: transparent;
  font-size: 16px;
  font-weight: 800;
  cursor: pointer;
}

.primary-button,
.sso-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 56px;
  border-radius: 7px;
  font-size: 21px;
  font-weight: 800;
  cursor: pointer;
}

.primary-button {
  margin-top: 4px;
  border: 0;
  color: #ffffff;
  background: linear-gradient(100deg, #1268ff 0%, #246dff 45%, #7442ef 100%);
  box-shadow: 0 12px 22px rgba(47, 98, 238, 0.22);
}

.primary-button:disabled {
  cursor: wait;
  opacity: 0.78;
}

.divider {
  display: grid;
  grid-template-columns: 1fr auto 1fr;
  gap: 22px;
  align-items: center;
  margin: 20px 0 18px;
  color: #66758c;
}

.divider span {
  height: 1px;
  background: #e0e5ee;
}

.divider em {
  font-size: 16px;
  font-style: normal;
  font-weight: 700;
}

.sso-button {
  gap: 12px;
  border: 1px solid #2170ff;
  color: #1268ff;
  background: #ffffff;
}

.sso-badge {
  display: grid;
  width: 26px;
  height: 26px;
  color: #ffffff;
  background: #2d73ff;
  border-radius: 7px;
  font-size: 15px;
  place-items: center;
}

.register-entry {
  margin: 20px 0 0;
  color: #5f6d83;
  text-align: center;
  font-size: 17px;
  font-weight: 700;
}

.register-entry a {
  color: #1268ff;
  font-weight: 800;
}

.login-footer {
  display: flex;
  flex-wrap: wrap;
  gap: 18px;
  align-items: center;
  justify-content: center;
  margin-top: 22px;
  color: #8793a6;
  font-size: 14px;
  font-weight: 600;
}

.login-footer i {
  width: 1px;
  height: 14px;
  background: #c9d1df;
}

@media (max-width: 1180px) {
  .login-page {
    padding: 32px 20px;
  }

  .login-shell {
    grid-template-columns: 1fr;
    gap: 32px;
    padding: 38px 28px;
  }

  .brand-panel {
    min-height: 540px;
  }

  .login-card {
    justify-self: center;
  }
}

@media (max-width: 720px) {
  .login-page {
    padding: 0;
    border: 0;
    border-radius: 0;
  }

  .login-shell {
    min-height: 100vh;
    border-radius: 0;
  }

  .brand-panel {
    min-height: auto;
  }

  .brand-name {
    display: grid;
    gap: 6px;
  }

  .welcome-copy h1 {
    font-size: 40px;
  }

  .welcome-copy p {
    font-size: 17px;
  }

  .campus-illustration {
    margin-left: -28px;
  }

  .feature-list {
    grid-template-columns: 1fr;
  }

  .login-card {
    padding: 32px 24px;
  }

  .login-card__header h2 {
    font-size: 34px;
  }
}
</style>
