import React, {useState} from 'react';

import {HashRouter, Route, Switch,} from "react-router-dom";
import {Home} from './home/Home';

function App() {
    return (
        <div>
            <HashRouter>
                <div>
                    <Switch>
                        <Route path="/"><Home/></Route>
                    </Switch>
                </div>
            </HashRouter>
        </div>
    );
}

export default App;
