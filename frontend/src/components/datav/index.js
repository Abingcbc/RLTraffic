import React, {Component} from 'react'
import {
    BorderBox2,
    BorderBox1,
    BorderBox3, Decoration2,
} from '@jiaminghi/data-view-react'

import './index.less'
import {StaticMap} from "react-map-gl";

import DeckGL from '@deck.gl/react';
import {TripsLayer} from '@deck.gl/geo-layers';
import {ScatterplotLayer} from '@deck.gl/layers';
import MapboxLanguage from '@mapbox/mapbox-gl-language';

import CanvasJSReact from '../../assets/canvasjs.react';

import './CenterCmp.less'
import './LeftChart1.less'
import './LeftChart2.less'

import {hostname} from "../../config";

export const DATA_URL = {
    TRIPS: hostname + '/trips.json',
};

// Set your mapbox token here
const MAPBOX_TOKEN = 'pk.eyJ1IjoiYWJpbmdjYmMiLCJhIjoiY2s1aHV5YWNvMDJhNjNmcDRqNjQ1aTBvbSJ9.Zvyu6jrndnVlkXsiytMW-w';

const style = {
    width: '120px',
    height: '50px',
    lineHeight: '50px',
    textAlign: 'center',
    marginLeft: '200px',
};

const style2 = {
    height: '50px'
};

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
    zoom: 14,
    pitch: 45,
    bearing: 0
};

var CanvasJS = CanvasJSReact.CanvasJS;
let waitChart;
let distanceChart;

let orders;
let waitTime;
let distance;
let curOrderIndex = 0;
let noBalanceWT = [];
let simpleBalanceWT = [];
let bestBalanceWT = [];
let dqnBalanceWT = [];
let simpleBalanceD = [];
let bestBalanceD = [];
let dqnBalanceD = [];


export default class DataV extends Component {

    constructor(props) {
        super(props);
        this.state = {
            time: 0,
        };
    }

    fetchData() {
        if (localStorage.getItem("orders") ||
            localStorage.getItem("waitTime") ||
            localStorage.getItem("distance")
        ) {
            orders = JSON.parse(localStorage.getItem("orders"));
            waitTime = JSON.parse(localStorage.getItem("waitTime"));
            distance = JSON.parse(localStorage.getItem("distance"));
        } else {
            this.props.history.goBack('/');
        }
    }

    componentWillMount() {
        this.fetchData()
    }

    componentDidMount() {
        const context = this;
        const timestamps = new Date().getTime();
        // WebSocket
        const socket = new WebSocket("ws://abingcbc.cn:6627/dispatch/" + timestamps);
        socket.onopen = function () {
            console.log("WS connect success")
        };
        socket.onclose = function () {
            console.log("WS connect close")
        };
        socket.onmessage = function (msg) {
            context.setState({
                time: parseInt(msg.data)
            })
        };
        waitChart = new CanvasJS.Chart("waitTimeChart", {
            theme: "dark1",
            height: 250,
            backgroundColor: "transparent",
            axisX:{
                title: "时间(s)",
            },
            axisY:{
                title: "平均等待时间(s)",
            },
            legend: {
                cursor:"pointer",
                verticalAlign: "top",
            },
            title: {
                text: "等待时间"
            },
            toolTip: {
                shared: true
            },
            data: [{
                type: "line",
                dataPoints: noBalanceWT,
                name: "无再平衡",
                color: "lawngreen",
                showInLegend: true
            }, {
                type: "line",
                dataPoints: simpleBalanceWT,
                name: "简单再平衡",
                color: "darkorange",
                showInLegend: true
            }, {
                type: "line",
                dataPoints: bestBalanceWT,
                name: "最优再平衡",
                color: "dodgerblue",
                showInLegend: true
            }, {
                type: "line",
                dataPoints: dqnBalanceWT,
                name: "dqn再平衡",
                color: "darkorchid",
                showInLegend: true
            },
            ]
        });
        distanceChart = new CanvasJS.Chart("distanceChart", {
            theme: "dark1",
            height: 250,
            backgroundColor: "transparent",
            axisX:{
                title: "时间(s)",
            },
            axisY:{
                title: "距离(km)",
            },
            legend: {
                cursor:"pointer",
                verticalAlign: "top",
            },
            title: {
                text: "再平衡距离"
            },
            toolTip: {
                shared: true
            },
            data: [{
                type: "line",
                dataPoints: simpleBalanceD,
                name: "简单再平衡",
                color: "darkorange",
                showInLegend: true
            }, {
                type: "line",
                dataPoints: bestBalanceD,
                name: "最优再平衡",
                color: "dodgerblue",
                showInLegend: true
            }, {
                type: "line",
                dataPoints: dqnBalanceD,
                name: "dqn再平衡",
                color: "darkorchid",
                showInLegend: true
            },
            ]
        });

        this._animate();
    }

    updateChart(curTime) {
        // pushing the new values
        let curIndex = noBalanceWT.length;
        while (curIndex*30 < curTime && curIndex < waitTime.length) {
            noBalanceWT.push({
                x: curIndex*30,
                y: waitTime[curIndex].no
            });
            simpleBalanceWT.push({
                x: curIndex*30,
                y: waitTime[curIndex].simple
            });
            bestBalanceWT.push({
                x: curIndex*30,
                y: waitTime[curIndex].best
            });
            dqnBalanceWT.push({
                x: curIndex*30,
                y: waitTime[curIndex].dqn
            });
            simpleBalanceD.push({
                x: curIndex*30,
                y: distance[curIndex].simple
            });
            bestBalanceD.push({
                x: curIndex*30,
                y: distance[curIndex].best
            });
            dqnBalanceD.push({
                x: curIndex*30,
                y: distance[curIndex].dqn
            });
            curIndex += 1;
        }
        waitChart.render();
        distanceChart.render();
    }

    clearChart() {
        noBalanceWT.length = 0;
        simpleBalanceWT.length = 0;
        bestBalanceWT.length = 0;
        dqnBalanceWT.length = 0;
        simpleBalanceD.length = 0;
        bestBalanceD.length = 0;
        dqnBalanceD.length = 0;
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
        const nextTime = (this.state.time + 1) % loopLength;
        if (nextTime === 0) {
            curOrderIndex = 0;
            this.clearChart();
        }
        if (nextTime % 500 === 0) {
            console.log('current time ' + nextTime);
        }
        this.updateChart(nextTime);

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
        orders[curOrderIndex].timestamp < timestamp - 100) {
            curOrderIndex += 1;
        }
        let endIndex = curOrderIndex;
        while (endIndex < orders.length &&
        orders[endIndex].timestamp < timestamp + 100) {
            endIndex += 1;
        }
        const curOrders = orders.slice(curOrderIndex, endIndex);
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
                        return 100 - order.timestamp + timestamp;
                    } else {
                        return 100 - timestamp + order.timestamp;
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
        } = this.props;

        return (
            <div id="data-view">
                <div className="main-header">
                    <div className="mh-left"/>
                    <div className="mh-middle">车载人工智能调度决策支持系统</div>
                    <div className="mh-right">
                        <BorderBox2 style={style}>同济大学</BorderBox2>
                    </div>
                </div>
                <BorderBox1 className="main-container">
                    <BorderBox3 id="chartContainer" className="left-chart-container">
                        <div className="left-chart-1">
                            <div id="waitTimeChart"/>
                        </div>
                        <Decoration2 style={style2}/>
                        <div className="left-chart-2">
                            <div id="distanceChart"/>
                        </div>
                    </BorderBox3>
                    <div className="right-main-container">
                        <div className="rmc-top-container">
                            <BorderBox3 className="rmctc-left-container">
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
                            </BorderBox3>
                        </div>
                    </div>
                </BorderBox1>
            </div>
        )
    }
}
