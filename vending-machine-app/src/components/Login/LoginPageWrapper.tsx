import React from 'react';
import styled from 'styled-components';
import { gray1, gray2 } from '../colors';
import { ColFlexBox } from '../FlexBox';

const Container = styled.main`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100vx;
  height: 100vh;
  min-height: 700px;
  background-color: ${gray1};
  overflow-y: auto;
  min-width: 400px;
`;

const InputContainer = styled.div`
  background-color: white;
  border: 1px solid ${gray2};
  border-radius: 0.25rem;
  width: 400px;
  padding: 30px;
  margin: 50px 0;
`;

export const LoginPageWrapper: React.FC = ({ children }) => {
  return (
    <Container>
      <ColFlexBox justifyContent="center" alignItems="center" height="100%">
        <InputContainer>{children}</InputContainer>
      </ColFlexBox>
    </Container>
  );
};
