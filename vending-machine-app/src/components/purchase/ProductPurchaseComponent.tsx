import React from 'react';
import styled from 'styled-components';
import { Product } from '../../service/ApiService';
import { blue1, blue, yellow } from '../colors';

interface ProductPurchaseComponent {
  onPurchase: VoidFunction;
  product: Product;
  onSetPurchaseAmount: (amount: number) => void;
  amount: number;
  deposit: number;
}

const PurchaseButton = styled.button`
  font-size: 16px;
  font-weight: 500;
  background-color: ${blue};
  padding: 0.5rem 1.5rem;
  color: white;
  border-radius: 0.25rem;
  cursor: pointer;
  flex-shrink: 0;
  &:disabled {
    background-color: #e2e8f0;
    cursor: not-allowed;
  }
`;

const Container = styled.div`
  padding: 10px;
  background: ${yellow}
`;

const ProductLabel = styled.span`
  font-weight: bold;
  color: ${blue1};
`;

export const ProductPurchaseComponent: React.FC<ProductPurchaseComponent> = props => {

  return (
    <Container>
      <p><ProductLabel>{props.product.productName}</ProductLabel></p>
      <p><ProductLabel>Price: </ProductLabel>{props.product.cost / 100} Euro</p>
      <p>Amount: <input type="number" min="1" max={props.product.amountAvailable} value={props.amount} onChange={e => props.onSetPurchaseAmount(+e.target.value)}/> </p>
      <p>Total price: {props.amount * props.product.cost / 100} Euro</p>
      <PurchaseButton disabled={props.amount * props.product.cost > props.deposit} onClick={() => props.onPurchase()}>Purchase</PurchaseButton>
    </Container>
  );

}