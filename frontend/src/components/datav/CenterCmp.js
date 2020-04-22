import React, {Component} from 'react';
import {StaticMap} from 'react-map-gl';
import {AmbientLight, PointLight, LightingEffect} from '@deck.gl/core';
import DeckGL from '@deck.gl/react';
import {TripsLayer} from '@deck.gl/geo-layers';
import {ScatterplotLayer} from '@deck.gl/layers';
import MapboxLanguage from '@mapbox/mapbox-gl-language';

import './CenterCmp.less'

// Set your mapbox token here
const MAPBOX_TOKEN = 'pk.eyJ1IjoiYWJpbmdjYmMiLCJhIjoiY2s1aHV5YWNvMDJhNjNmcDRqNjQ1aTBvbSJ9.Zvyu6jrndnVlkXsiytMW-w';
const hostname = 'http://localhost:3000';

// Source data CSV
const DATA_URL = {
    TRIPS: hostname+'/trips.json',
    ORDERS: hostname+'/order.json'
    // 'https://raw.githubusercontent.com/uber-common/deck.gl-data/master/examples/trips/trips-v7.json' // eslint-disable-line
};

const ambientLight = new AmbientLight({
    color: [255, 255, 255],
    intensity: 1.0
});

const pointLight = new PointLight({
    color: [255, 255, 255],
    intensity: 2.0,
    position: [-74.05, 40.7, 8000]
});

const lightingEffect = new LightingEffect({ambientLight, pointLight});

const material = {
    ambient: 0.1,
    diffuse: 0.6,
    shininess: 32,
    specularColor: [60, 64, 70]
};

const DEFAULT_THEME = {
    buildingColor: [74, 80, 87],
    trailColor0: [253, 128, 93],
    trailColor1: [23, 184, 190],
    material,
    // effects: [lightingEffect]
};

const INITIAL_VIEW_STATE = {
    longitude: 121.258354,
    latitude: 31.343472,
    zoom: 12,
    pitch: 45,
    bearing: 0
};

let orders = [];
let curOrderIndex = 0;

export default class CenterCmp extends Component {
    constructor(props) {
        super(props);
        this.state = {
            time: 0,
        };
    }

    componentDidMount() {
        const context = this;
        const timestamps = new Date().getTime();
        fetch(DATA_URL.ORDERS)
            .then(response => {
                return response.json();
            })
            .then(data => {
                orders = data;
            });
        const socket = new WebSocket("ws://abingcbc.cn:6627/dispatch/"+timestamps);
        socket.onopen = function () {
            console.log("WS connect success")
        };
        socket.onclose = function () {
            console.log("WS connect close")
        };
        socket.onmessage = function (msg) {
            context.setState ({
                time: parseInt(msg.data)
            })
        };
        this._animate();
    }

    componentWillUnmount() {
        if (this._animationFrame) {
            window.cancelAnimationFrame(this._animationFrame);
        }
    }

    mapLanguageHandler(event) {
        const map = event.target;
        map.addControl(new MapboxLanguage({
            defaultLanguage: 'zh',
        }));
        map.setLayoutProperty('country-label-lg', 'text-field', ['get', 'name_zh']);
    }

    _animate() {
        const {
            loopLength = 7600, // unit corresponds to the timestamp in source data
            animationSpeed = 10 // unit time per second
        } = this.props;
        const nextTime = (this.state.time+1)%loopLength;
        if (nextTime === 0) {
            curOrderIndex = 0;
        }
        if (nextTime % 500 === 0) {
            console.log('current time ' + nextTime);
        }

        this.setState({
            // time: ((timestamp % loopTime) / loopTime) * loopLength
            time: nextTime
        });
        this._animationFrame = window.requestAnimationFrame(this._animate.bind(this));
    }

    _renderLayers() {
        const {
            trips = DATA_URL.TRIPS,
            trailLength = 100,
            theme = DEFAULT_THEME,
            timestamp = this.state.time
        } = this.props;
        while (curOrderIndex < orders.length &&
        orders[curOrderIndex].timestamp < timestamp-100) {
            curOrderIndex += 1;
        }
        let endIndex = curOrderIndex;
        while (endIndex < orders.length &&
            orders[endIndex].timestamp < timestamp+100) {
            endIndex += 1;
        }
        const curOrders = orders.slice(curOrderIndex, endIndex);
        console.log(curOrders.length);
        return [
            new ScatterplotLayer({
                id: 'scatter-plot',
                data: curOrders,
                radiusScale: 1,
                radiusMinPixels: 0.25,
                lineWidthScale: 0,
                getPosition: d => d.coordinates,
                getFillColor: [0, 128, 255],
                getRadius: (order) => {
                    if (timestamp < order.timestamp) {
                        return 100-order.timestamp+timestamp;
                    } else {
                        return timestamp-order.timestamp;
                    }
                },
            }),
            new TripsLayer({
                id: 'trips',
                data: trips,
                getPath: d => d.path,
                getTimestamps: d => d.timestamps,
                opacity: 0.3,
                widthMinPixels: 2,
                rounded: true,
                trailLength,
                currentTime: this.state.time,
                shadowEnabled: false,
                getColor: [253, 128, 93]
            })
        ];
    }

    render() {
        const {
            viewState,
            mapStyle = 'mapbox://styles/mapbox/dark-v9',
            theme = DEFAULT_THEME,
            aName
        } = this.props;

        return (
            <div className="center-cmp">
                <div className="cc-header">
                    {/*<div>{aName}</div>*/}
                </div>
                <DeckGL
                    layers={this._renderLayers()}
                    effects={theme.effects}
                    initialViewState={INITIAL_VIEW_STATE}
                    viewState={viewState}
                    controller={true}
                >
                    <StaticMap
                        reuseMaps
                        mapStyle={mapStyle}
                        preventStyleDiffing={true}
                        mapboxApiAccessToken={MAPBOX_TOKEN}
                        onLoad={this.mapLanguageHandler}
                    />
                </DeckGL>
            </div>
        );
    }
}
