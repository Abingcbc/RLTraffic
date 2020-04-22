import React from 'react';
import {HashRouter, Route, Switch} from 'react-router-dom';
import DataV from "./components/datav";
import LoadPage from "./components/datav/loading";


const Router = () => (
    <HashRouter>
        <Switch>
            <Route exact path="/" component={LoadPage}/>
            <Route exact path="/screen" component={DataV}/>
        </Switch>
    </HashRouter>
);

export default Router;
