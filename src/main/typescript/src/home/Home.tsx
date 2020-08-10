import React, {useState} from 'react';
import './Home.css';
import {api} from '../apiConfig';
import {SensorValue} from '../generated/api';


export function Home() {
    const [response, setResponse] = useState();

    const probes = () => {

        api.getReading("channel0").then(x => setResponse(x.data));

    }

    return (
        <div className="App">
            <header className="App-header">
                <img src={'https://www.loten.nl/app/uploads/2020/01/nederlandse-loterij.png'} className="App-logo"
                     alt="logo"/>
                <div onClick={probes}>click <div>{JSON.stringify(response, null, 2)}</div></div>
            </header>
        </div>
    )
}
