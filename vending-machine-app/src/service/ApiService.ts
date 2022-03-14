import authStateStore  from '../authentication/AuthenticationStateStore';
import axios from 'axios';
import { useHistory } from 'react-router-dom';

export type Product = {
    "id": string
    "amountAvailable": number
    "cost": number
    "productName": string
}

export type PurchaseResponse = {
  totalPrice: number,
  productName: string,
  change: number[],
}

export const fetchAllProducts = (onUnauthorized: VoidFunction): Promise<Product[]> =>
  axios.get(`/api/v1/products`, {
      headers: {
          Authorization: 'Bearer ' + authStateStore.authUser?.token
      }})
    .then(res => Promise.resolve(res.data))
    .catch(err => err.response.status === 401 ? onUnauthorized() : new Error("Something went wrong"))

export const depositCoins = (value: number, onUnauthorized: VoidFunction): Promise<number> =>
  axios.put(`/api/v1/users/deposit?amount=${value}`,undefined, {
    headers: {
      Authorization: 'Bearer ' + authStateStore.authUser?.token
    }})
    .then(res => res.status === 200 ? Promise.resolve(res.data.deposit) : Promise.resolve(0))
    .catch(err => err.response.status === 401 ? onUnauthorized() : new Error("Something went wrong"))

export const resetDeposit = (onUnauthorized: VoidFunction): Promise<void> =>
  axios.put(`/api/v1/users/resetdeposit`,undefined, {
    headers: {
      Authorization: 'Bearer ' + authStateStore.authUser?.token
    }})
    .then(() => Promise.resolve())
    .catch(err => {
      if (err.response.status === 401) {
        onUnauthorized()
      }
    })

export const purchaseProduct = (productId: string, amount: number, onUnauthorized: VoidFunction, onSuccess: (res: any) => void) =>
  axios.put(`/api/v1/products/${productId}/purchase?amount=${amount}`, undefined, {
    headers: {
      Authorization: 'Bearer ' + authStateStore.authUser?.token
    }
  })
    .then(res => onSuccess(res.data as PurchaseResponse))
    .catch(err => err.response.status === 401 ? onUnauthorized() : new Error("Something went wrong"))
