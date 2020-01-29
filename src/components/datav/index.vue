<template>
  <div id="data-view">
    <dv-full-screen-container>
      <div class="main-header">
        <div class="mh-left"></div>
        <div class="mh-middle">强化学习无人车调度平台</div>
        <div class="mh-right">
          <dv-border-box-2
            style="width: 120px; height: 50px; line-height: 50px; text-align:center;margin-left:200px;"
          >
            同济大学
          </dv-border-box-2>
        </div>
      </div>

      <dv-border-box-1 class="main-container">
        <dv-border-box-3 class="left-chart-container">

          <Left-Chart-1 />
          <Left-Chart-2 />
          <Left-Chart-3 />

        </dv-border-box-3>

        <div class="right-main-container">
          <div class="rmc-top-container">
            <dv-border-box-3 class="rmctc-left-container">
              <Center-Cmp1 />
            </dv-border-box-3>
<!--            <dv-border-box-3 class="rmctc-left-container">-->
<!--              <Center-Cmp2 />-->
<!--            </dv-border-box-3>-->
<!--            <dv-border-box-3 class="rmctc-left-container">-->
<!--              <Center-Cmp3 />-->
<!--            </dv-border-box-3>-->
<!--            <dv-border-box-3 class="rmctc-left-container">-->
<!--              <Center-Cmp4 />-->
<!--            </dv-border-box-3>-->

          </div>

        </div>
      </dv-border-box-1>

    </dv-full-screen-container>
  </div>
</template>

<script>
import LeftChart1 from './LeftChart1'
import LeftChart2 from './LeftChart2'
import LeftChart3 from './LeftChart3'

import CenterCmp1 from './CenterCmp1'
import CenterCmp2 from './CenterCmp2'
import CenterCmp3 from './CenterCmp3'
import CenterCmp4 from './CenterCmp4'

// import RightChart1 from './RightChart1'
// import RightChart2 from './RightChart2'
//
// import BottomCharts from './BottomCharts'

export default {
  name: 'DataView',
  components: {
    LeftChart1,
    LeftChart2,
    LeftChart3,
    CenterCmp1,
    CenterCmp2,
    CenterCmp3,
    CenterCmp4
    // RightChart1,
    // RightChart2,
    // BottomCharts
  },
  data () {
    return {}
  }
}
</script>

<style lang="less">
#data-view {
  width: 100%;
  height: 100%;
  background-color: #030409;
  color: #fff;

  #dv-full-screen-container {
    background-image: url('./img/bg.png');
    background-size: 100% 100%;
    box-shadow: 0 0 3px blue;
    display: flex;
    flex-direction: column;
  }

  .main-header {
    height: 80px;
    display: flex;
    justify-content: space-between;
    align-items: flex-end;

    .mh-left {
      font-size: 20px;
      color: rgb(1,134,187);

      a:visited {
        color: rgb(1,134,187);
      }
    }

    .mh-middle {
      font-size: 30px;
    }

    .mh-left, .mh-right {
      width: 450px;
    }
  }

  .main-container {
    height: calc(~"100% - 80px");

    .border-box-content {
      padding: 20px;
      box-sizing: border-box;
      display: flex;
    }
  }

  .left-chart-container {
    width: 22%;
    padding: 10px;
    box-sizing: border-box;

    .border-box-content {
      flex-direction: column;
    }
  }

  .right-main-container {
    width: 78%;
    padding-left: 5px;
    box-sizing: border-box;
  }

  .rmc-top-container {
    height: 90%;
    display: flex;
    flex-wrap: wrap;
  }

  .rmctc-left-container {
    width: 45%;
    height: 45%;
    padding: 3% 4% 3% 1%;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    display: flex;
  }

  .rmctc-right-container {
    width: 35%;
  }

  .rmc-bottom-container {
    height: 35%;
  }

  .rmctc-chart-1, .rmctc-chart-2 {
    height: 50%;
  }
}
</style>
