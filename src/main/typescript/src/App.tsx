import React, {useState} from 'react';
import { connect } from "react-redux";

import {HashRouter, Route, Switch,} from "react-router-dom";
import {Home} from './home/Home';

// Actions
var increaseAction = { type: "increase" };
var decreaseAction = { type: "decrease" };

// Map Redux state to component props
function mapStateToProps(state: any) {
    return {
        countValue: state.count
    }
}

// Map Redux actions to component props
function mapDispatchToProps(dispatch: any) {
    return {
        increaseCount: function() {
            return dispatch(increaseAction);
        },
        decreaseCount: function() {
            return dispatch(decreaseAction);
        }
    }
}

// The HOC
var App = connect(
    mapStateToProps,
    mapDispatchToProps
)(AppTemplate);

function AppTemplate() {
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
