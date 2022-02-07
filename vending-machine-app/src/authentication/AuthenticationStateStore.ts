import { action, makeObservable, observable } from 'mobx';
import axios from 'axios';

export type AuthenticatedUser = {
  userId: string,
  username: string,
  token: string,
  roles: string
}

class AuthStateStore {
  authUser: AuthenticatedUser | undefined = undefined

  constructor() {
    makeObservable(this, {
      authUser: observable,
      login: action,
    })
  }

  async login(username: string, password: string): Promise<boolean> {
    return await axios.post(`/login?username=${username}&password=${password}`)
      .then(async res => {
        this.authUser = res.data
        return Promise.resolve(true);
      }).catch( err => {
        return Promise.resolve(false);
    })
  }
}

const authStateStore = new AuthStateStore()

export default authStateStore