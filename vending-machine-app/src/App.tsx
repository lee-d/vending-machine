import React from 'react';
import './App.css';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import { Login } from './components/Login/Login';
import authStateStore from './authentication/AuthenticationStateStore';
import { PurchaseComponent } from './components/purchase/PurchaseComponent';

export const StoreContext = React.createContext(authStateStore);

function App() {
  return (
    <StoreContext.Provider value={authStateStore}>
      <Router>
        <Switch>
          <Route path="/purchase">
            <PurchaseComponent />
          </Route>
          <Route path="/">
            <Login/>
          </Route>
        </Switch>
      </Router>
    </StoreContext.Provider>
  );
}

export default App;
