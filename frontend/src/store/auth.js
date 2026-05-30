import { reactive } from 'vue'
import { getMe } from '../api/auth'

export const authState = reactive({
  user: null,
  loaded: false,
})

export async function fetchMe() {
  try {
    authState.user = await getMe()
  } catch {
    authState.user = null
  } finally {
    authState.loaded = true
  }
}
