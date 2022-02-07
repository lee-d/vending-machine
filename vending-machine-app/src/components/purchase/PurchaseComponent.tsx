import React, { useEffect, useState } from 'react';
import {
  depositCoins,
  fetchAllProducts,
  Product,
  purchaseProduct,
  PurchaseResponse,
  resetDeposit
} from '../../service/ApiService';
import styled from 'styled-components';
import { blue1, blue2, blue3, green, orange, blue } from '../colors';
import { DepositComponent } from './DepositComponent';
import authStateStore from '../../authentication/AuthenticationStateStore';
import { ProductPurchaseComponent } from './ProductPurchaseComponent';
import { useHistory } from 'react-router-dom';

const Container = styled.div`
  display: flex;
  justify-content: center;
  padding-top: 200px;
`;

const ProductContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 150px;
  width: 200px;
  border-style: solid;
  border-width: 1px;
  padding: 10px;
  margin-bottom: 4px;
  background: ${blue3};
  cursor: pointer;
`;

const ItemContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  border-style: solid;
  border-width: 1px;
  padding: 20px;
`;

const RightContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  border-style: solid;
  border-width: 1px;
  padding: 20px;
  margin: 20px;
  width: 190px;
`;

const ProductLabel = styled.span`
  font-weight: bold;
  color: ${blue1};
`;

export const PurchaseComponent: React.FC = props => {

  const [products, setProducts] = useState<Product[]>([])
  const [deposit, setDeposit] = useState(0)
  const [selectedProduct, setSelectedProduct] = useState<Product | undefined>(undefined)
  const [purchaseAmount, setPurchaseAmount] = useState<number>(1)
  const history = useHistory();

  useEffect(() => {
    fetchAllProducts(redirectToLogin).then(res => {
        setProducts(res)
    })
  }, [])

 return (
   <Container>
     <ItemContainer>
       {products?.map(product =>  {
         return <ProductContainer onClick={() => setProduct(product)} key={product.id}>
           <p><ProductLabel>{product.productName}</ProductLabel></p>
           <p><ProductLabel>Price: </ProductLabel>{product.cost / 100} Euro</p>
           <p><ProductLabel>Stock: </ProductLabel>{product.amountAvailable}</p>
         </ProductContainer>;
       })}
     </ItemContainer>
     <ItemContainer>
       <RightContainer>
         <DepositComponent deposit={deposit} onDeposit={onDeposit} onReset={onReset}/>
       </RightContainer>
       {selectedProduct && <RightContainer>
         <ProductPurchaseComponent
           product={selectedProduct}
           onPurchase={onPurchase}
           onSetPurchaseAmount={setPurchaseAmount}
           amount={purchaseAmount}
           deposit={deposit}
         />
       </RightContainer>}
     </ItemContainer>
   </Container>
  );

  function setProduct(product: Product) {
    setPurchaseAmount(1)
    setSelectedProduct(product)
  }

  function redirectToLogin() {
    history.push("/login")
  }
  function onDeposit(value: number) {
    depositCoins(authStateStore.authUser?.userId!!, value, redirectToLogin).then(depositValue => setDeposit(depositValue))
  }

  function onReset() {
    resetDeposit(authStateStore.authUser?.userId!!, redirectToLogin).then(res => setDeposit(0))
  }

  function onPurchaseSuccess(res: PurchaseResponse) {
    setSelectedProduct(undefined)
    setPurchaseAmount(1)
    setDeposit(0)
    window.alert(`Purchase of ${res.productName} successful!\nPlease take your item.\nTotal price of ${res.totalPrice / 100} Euros paid.\nFollowing change will be returned: ${res.change}`)
  }

  function onPurchase() {
    purchaseProduct(selectedProduct!!.id, purchaseAmount, redirectToLogin, onPurchaseSuccess)
  }
}