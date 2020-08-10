import {Component} from 'react';
import React from 'react';

type Props = {
    identifier: string,
    mode: string
};
type State = {};

class TemperatureSensor extends Component<Props, State> {
    render() {
        return (
            <div>
                <div>Identifier: {this.props.identifier}</div>
                <div>Mode: {this.props.mode}</div>
            </div>
        )
    };
}