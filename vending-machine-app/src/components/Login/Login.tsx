import React, { useState, } from 'react';
import styled from 'styled-components';
import { errorTextColor, gray1, gray3, blue, gray2 } from '../colors';
import authStateStore from '../../authentication/AuthenticationStateStore';
import { useHistory } from 'react-router-dom';

const InputContainerHeading = styled.h2`
  font-weight: 600;
  font-size: 24px;
  margin-top: 0;
  margin-bottom: 20px;
`;

const LoginForm = styled.form`
  display: flex;
  display-direction: column;
  height: 100%;
`;

const LoginInputField = styled.input`
  width: 100%;
  padding: 0.5rem 0.5rem;
  background-color: ${gray1};
  border: 1px solid ${gray3};
  border-radius: 0.25rem;
  margin-bottom: 10px;
  filter: none;
`;

const LoginButton = styled.button`
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
  min-width: 300px;
`;

const InputContainer = styled.div`
  background-color: white;
  border: 1px solid ${gray2};
  border-radius: 0.25rem;
  width: 400px;
  padding: 30px;
  margin: 50px 0;
`;

type FlexBoxProps = {
  justifyContent?: string;
  alignItems?: string;
};

export const StyledColFlexBox = styled.div<FlexBoxProps>`
  display: flex;
  flex-direction: column;
  width: 100%;
  justify-content: ${props => props.justifyContent};
  margin: 10px;
  height: 100%;
  align-items: ${props => props.alignItems};
`;

type ErrorTextProps = {
  showError: boolean;
};
const ErrorText = styled.p<ErrorTextProps>`
  color: ${errorTextColor};
  font-size: 12px;
  visibility: ${props => (props.showError ? 'visible' : 'hidden')};
`;

export const LoginLabel = styled.label`
  margin-bottom: 5px;
  font-size: 14px;
  &:hover {
    background-color: white;
  }
  &:focus-within {
    background-color: white;
  }
`;

export const Login: React.FC = props => {
  const [username, setUsername] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [showError, setShowError] = useState<boolean>(false);
  const history = useHistory();

  function login() {
    authStateStore.login(username, password).then(res => {
      if (res) {
        history.push('/purchase')
      } else {
        setShowError(true)
      }
    })
  }

  return (
      <Container>
        <StyledColFlexBox justifyContent="center" alignItems="center" >
          <InputContainer>
            <InputContainerHeading>Login</InputContainerHeading>
            <LoginForm>
              <StyledColFlexBox>
                <LoginLabel htmlFor="username">Username</LoginLabel>
                <LoginInputField
                  type="username"
                  value={username}
                  onChange={event => {
                    setShowError(false);
                    setUsername(event.target.value);
                  }}
                  onFocus={() => setShowError(false)}
                  aria-label="username-input"
                />
                <LoginLabel htmlFor="password">Password</LoginLabel>
                <LoginInputField
                  type="password"
                  value={password}
                  onChange={event => {
                    setShowError(false);
                    setPassword(event.target.value);
                  }}
                  onFocus={() => setShowError(false)}
                  aria-label="password-input"
                />
                <ErrorText showError={showError}>Username or password incorrect</ErrorText>
                <LoginButton
                  type="button"
                  onClick={login}
                  aria-label="login-button"
                >
                  Login
                </LoginButton>
              </StyledColFlexBox>
            </LoginForm>
          </InputContainer>
        </StyledColFlexBox>
      </Container>
  );
};
