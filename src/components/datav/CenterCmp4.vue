<template>
  <div class="center-cmp">
<!--    <div class="cc-header">-->
<!--      <div>机电设备总数</div>-->
<!--    </div>-->
    <div id="map4"></div>
  </div>
</template>
<script>
import { Scene, LineLayer } from '@antv/l7'
import { Mapbox } from '@antv/l7-maps'
export default {
  data () {
    return {}
  },
  mounted () {
    const scene = new Scene({
      id: 'map4',
      map: new Mapbox({
        center: [ 116.3956, 39.9392 ],
        pitch: 0,
        zoom: 10,
        rotation: 0,
        style: 'dark'
      })
    })

    fetch(
      'https://gw.alipayobjects.com/os/basement_prod/0d2f0113-f48b-4db9-8adc-a3937243d5a3.json'
    )
      .then(res => res.json())
      .then(data => {
        const layer = new LineLayer({})
          .source(data)
          .size(1.5)
          .shape('line')
          .color('标准名称', [ '#5B8FF9', '#5CCEA1', '#5D7092' ])
        layer.animate(true)
        scene.addLayer(layer)
      })
  }
}
</script>
<style lang="less">
::-webkit-scrollbar {
  display: none;
}

html,
body {
  overflow: hidden;
  margin: 0;
}

#map4 {
  position: absolute;
  top: 0;
  bottom: 0;
  width: 100%;
}

.center-cmp {
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;

  .cc-header {
    height: 70px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 30px;
    z-index: 9999;
    text-align: center;
  }

  .cc-details {
    height: 120px;
    display: flex;
    justify-content: center;
    font-size: 32px;
    align-items: center;

    .card {
      background-color: rgba(4, 49, 128, .6);
      color: #08e5ff;
      height: 70px;
      width: 70px;
      font-size: 45px;
      font-weight: bold;
      line-height: 70px;
      text-align: center;
      margin: 10px;
    }
  }
}
</style>
