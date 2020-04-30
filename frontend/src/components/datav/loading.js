import React from 'react'
import ReactLoading from 'react-loading'
import $ from "jquery";
import {hostname} from '../../config'

export const DATA_URL = {
    ORDERS: hostname + '/order.json',
    WAITTIME: hostname + '/waittime.json',
    DISTANCE: hostname + '/distance.json',
};

const divStyle = {
    justifyContent: 'center',
    verticalAlign: 'middle',
    display: 'flex',
};

export default class LoadPage extends React.Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {
        this.fetchData();
    }

    fetchData() {
        $.ajaxSettings.async = false;
        // Order data
        if (!localStorage.getItem("orders")) {
            $.get({
                url: DATA_URL.ORDERS,
                datatype: 'json',
                success: function (data) {
                    localStorage.setItem("orders", JSON.stringify(data));
                }
            });
        }
        // wait time data
        if (!localStorage.getItem("waitTime")) {
            $.get({
                url: DATA_URL.WAITTIME,
                datatype: 'json',
                success: function (data) {
                    localStorage.setItem("waitTime", JSON.stringify(data));
                }
            });
        }
        // distance data
        if (!localStorage.getItem("distance")) {
            $.get({
                url: DATA_URL.DISTANCE,
                datatype: 'json',
                success: function (data) {
                    localStorage.setItem("distance", JSON.stringify(data));
                }
            });
        }
        console.log("data fetch success");
        $.ajaxSettings.async = true;
        this.props.history.push('screen');
    }

    render() {
        return (
            <div style={divStyle}>
                <ReactLoading type="cubes" color="rgb(53, 126, 221)"/>
            </div>
        )
    }
}

