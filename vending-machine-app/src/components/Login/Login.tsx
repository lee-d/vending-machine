import React, { useState, } from 'react';
import styled from 'styled-components';
import { errorTextColor, gray1, gray3, blue } from '../colors';
import { Spacing } from '../Spacing';
import { ColFlexBox } from '../FlexBox';
import { LoginPageWrapper } from './LoginPageWrapper';
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
}
  &:hover, &:focus, &:placeholder-shown {
    background-color: #fff !important;
  }
  &:-webkit-autofill {
    -webkit-transition: background-color 9999s ease-out !important;
    -webkit-transition-delay: 9999s !important;
    -webkit-box-shadow: 0 0 0 1000px ${gray1} inset !important;
    -moz-box-shadow: 0 0 0 100px ${gray1} inset !important;
    box-shadow: 0 0 0 100px ${gray1} inset !important;
    font-family: Source Sans Pro important!;
    font-size: 14px !important;
    font-weight: 500 !important;
  }
  &:-webkit-autofill:hover,
  &:-webkit-autofill:active,
  &:-webkit-autofill:focus {
    -webkit-transition: background-color 9999s ease-out !important;
    -webkit-transition-delay: 9999s !important;
    background-color: green !important;
    -webkit-box-shadow: 0 0 0 100px #fff inset !important;
    -moz-box-shadow: 0 0 0 100px #fff inset !important;
    box-shadow: 0 0 0 100px #fff inset !important;
    font-family: Source Sans Pro important!;
    font-size: 14px !important;
    font-weight: 500 !important;
  }
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

  return (
    <LoginPageWrapper>
      <InputContainerHeading>Login</InputContainerHeading>
      <LoginForm>
        <ColFlexBox>
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
          <LoginLabel htmlFor="password">Passwort</LoginLabel>
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
          <ErrorText showError={showError}>Username or passwort incorrect</ErrorText>
          <Spacing height="10px" />
          <LoginButton
            type="button"
            onClick={() => {
              authStateStore.login(username, password).then(res => {
                history.push('/purchase')
              })
            }}
            aria-label="login-button"
          >
            Login
          </LoginButton>
        </ColFlexBox>
      </LoginForm>
    </LoginPageWrapper>
  );
};
