import React from 'react';
import { blue2, green, orange } from '../colors';
import styled from 'styled-components';


interface DepositComponentProps {
  onDeposit: (value: number) => void;
  onReset: () => void;
  deposit: number;
}

interface DepositValueProp {
  color?: string;
}

const DepositContainer = styled.div`
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  width: 190px;
`;


const DepositValue = styled.div<DepositValueProp>`
  width: 55px;
  height: 55px;
  border-style: solid;
  border-width: 1px;
  border-radius: 50%;
  background: ${props => (props.color ? props.color : green)};
  margin-top: 10px;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const DepositHeader = styled.span`
  font-weight: bold;
  font-size: 21px;
  color: ${blue2};
`;

export const DepositComponent: React.FC<DepositComponentProps> = props => {

  return (
    <>
      <DepositHeader>Deposit: {props.deposit / 100} Euro</DepositHeader>
      <DepositContainer>
        <DepositValue onClick={() => props.onDeposit(5)}>5 Cents</DepositValue>
        <DepositValue onClick={() => props.onDeposit(10)}>10 Cents</DepositValue>
        <DepositValue onClick={() => props.onDeposit(20)}>20 Cents</DepositValue>
        <DepositValue onClick={() => props.onDeposit(50)}>50 Cents</DepositValue>
        <DepositValue onClick={() => props.onDeposit(100)}>100 Cents</DepositValue>
        <DepositValue onClick={() => props.onReset()} color={orange}>Reset</DepositValue>
      </DepositContainer>
    </>
  );

}