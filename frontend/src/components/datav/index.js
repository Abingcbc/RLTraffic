import React, {Component} from 'react'

import {
    BorderBox2,
    BorderBox1,
    BorderBox3,
} from '@jiaminghi/data-view-react'

import LeftChart1 from './LeftChart1'
import LeftChart2 from './LeftChart2'
import LeftChart3 from './LeftChart3'

import CenterCmp from './CenterCmp'

import './index.less'
import render from "less/lib/less/render";

const style = {
    width: '120px',
    height: '50px',
    lineHeight: '50px',
    textAlign: 'center',
    marginLeft: '200px',
};

export default class DataV extends Component {
    render() {
        let algorithm = [
            {
                aName: '算法1'
            }
        ];
        return (
            <div id="data-view">
                <div className="main-header">
                    <div className="mh-left"></div>
                    <div className="mh-middle">车载人工智能调度决策支持系统</div>
                    <div className="mh-right">
                        <BorderBox2 style={style}>同济大学</BorderBox2>
                    </div>
                </div>
                <BorderBox1 className="main-container">
                    <BorderBox3 className="left-chart-container">
                        <LeftChart1/>
                        <LeftChart2/>
                        <LeftChart3/>
                    </BorderBox3>
                    <div className="right-main-container">
                        <div className="rmc-top-container">
                            <BorderBox3 className="rmctc-left-container">
                                <CenterCmp/>
                            </BorderBox3>)
                        </div>
                    </div>
                </BorderBox1>
            </div>
        )
    }
}
