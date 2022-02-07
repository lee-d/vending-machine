import authStateStore  from '../authentication/AuthenticationStateStore';
import axios from 'axios';

export type Product = {
    "id": string
    "amountAvailable": number
    "cost": number
    "productName": string
}

export type ProductPurchaseDto = {
  userId: string,
  amount: number,
}

export type PurchaseResponse = {
  totalPrice: number,
  productName: string,
  change: number[],
}

export const fetchAllProducts = (): Promise<Product[]> =>
  axios.get(`/api/v1/products`, {
      headers: {
          Authorization: 'Bearer ' + authStateStore.authUser?.token
      }})
    .then(res => Promise.resolve(res.data));

export const depositCoins = (userId: string, value: number): Promise<number> =>
  axios.put(`/api/v1/users/${userId}/deposit?amount=${value}`,undefined, {
    headers: {
      Authorization: 'Bearer ' + authStateStore.authUser?.token
    }})
    .then(res => res.status === 200 ? Promise.resolve(res.data.deposit) : Promise.resolve(0));

export const resetDeposit = (userId: string): Promise<number> =>
  axios.put(`/api/v1/users/${userId}/resetdeposit`, undefined,{
    headers: {
      Authorization: 'Bearer ' + authStateStore.authUser?.token
    }})
    .then(res => Promise.resolve(0));

export const purchaseProduct = (productId: string, amount: number): Promise<PurchaseResponse> =>
  axios.put(`/api/v1/products/${productId}/purchase`, { userId: authStateStore.authUser?.userId!!, amount }, {
    headers: {
      Authorization: 'Bearer ' + authStateStore.authUser?.token
    }
  })
    .then(res => Promise.resolve(res.data as PurchaseResponse));