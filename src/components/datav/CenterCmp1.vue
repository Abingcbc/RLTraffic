<template>
  <div class="center-cmp">
    <!--    <div class="cc-header">-->
    <!--      <div>机电设备总数</div>-->
    <!--    </div>-->
    <canvas id="map1"></canvas>
    <div id="background"></div>
  </div>
</template>
<script>
import { Deck, AmbientLight, PointLight, LightingEffect } from '@deck.gl/core'
import { PolygonLayer, ScatterplotLayer } from '@deck.gl/layers'
import { TripsLayer } from '@deck.gl/geo-layers'
import { MapboxLayer } from '@deck.gl/mapbox'
import mapboxgl from 'mapbox-gl'

const DATA_URL = {
  BUILDINGS:
    '/source/buildings.json', // eslint-disable-line
  TRIPS:
    '/source/trips-v7.json' // eslint-disable-line
}
const ambientLight = new AmbientLight({
  color: [255, 255, 255],
  intensity: 1.0
})

const pointLight = new PointLight({
  color: [255, 255, 255],
  intensity: 2.0,
  position: [-74.05, 40.7, 8000]
})

const lightingEffect = new LightingEffect({ ambientLight, pointLight })

const material = {
  ambient: 0.1,
  diffuse: 0.6,
  shininess: 32,
  specularColor: [60, 64, 70]
}

const DEFAULT_THEME = {
  buildingColor: [74, 80, 87],
  trailColor0: [253, 128, 93],
  trailColor1: [23, 184, 190],
  material,
  effects: [lightingEffect]
}

const landCover = [[[-74.0, 40.7], [-74.02, 40.7], [-74.02, 40.72], [-74.0, 40.72]]]

export default {
  name: 'map1',
  data () {
    return {
      deck: '',
      buildings: DATA_URL.BUILDINGS,
      trips: DATA_URL.TRIPS,
      trailLength: 10,
      theme: DEFAULT_THEME,
      time: 0,
      mapObject: { }
    }
  },
  methods: {
    deckInitMap () {
      const INITIAL_VIEW_STATE = {
        longitude: -74,
        latitude: 40.72,
        zoom: 13,
        pitch: 45,
        bearing: 0
      }
      const myDeck = new Deck({
        canvas: 'map1',
        initialViewState: INITIAL_VIEW_STATE,
        controller: true,
        layers: [
          new PolygonLayer({
            id: 'ground',
            data: landCover,
            getPolygon: f => f,
            stroked: false,
            getFillColor: [0, 0, 0, 0]
          }),
          new TripsLayer({
            id: 'trips',
            data: this.trips,
            getPath: d => d.path,
            getTimestamps: d => d.timestamps,
            getColor: d => (d.vendor === 0 ? this.theme.trailColor0 : this.theme.trailColor1),
            opacity: 0.3,
            widthMinPixels: 2,
            rounded: true,
            trailLength: this.trailLength,
            currentTime: this.time,
            shadowEnabled: false
          }),
          new PolygonLayer({
            id: 'buildings',
            data: this.buildings,
            extruded: true,
            wireframe: false,
            opacity: 0.5,
            getPolygon: f => f.polygon,
            getElevation: f => f.height,
            getFillColor: this.theme.buildingColor,
            material: this.theme.material
          })
        ]
      })
      this.deck = myDeck
      mapboxgl.accessToken = 'pk.eyJ1IjoiYWJpbmdjYmMiLCJhIjoiY2s1aHV5YWNvMDJhNjNmcDRqNjQ1aTBvbSJ9.Zvyu6jrndnVlkXsiytMW-w'
      const map = new mapboxgl.Map({
        container: 'background',
        center: [INITIAL_VIEW_STATE.longitude, INITIAL_VIEW_STATE.latitude],
        zoom: INITIAL_VIEW_STATE.zoom,
        pitch: INITIAL_VIEW_STATE.pitch,
        style: 'mapbox://styles/mapbox/dark-v9'
      })
      const myLayer = new MapboxLayer({
        id: 'my-scatterplot',
        type: ScatterplotLayer,
        data: [
          { position: [-74.50, 40], size: 100 }
        ],
        getPosition: d => d.position,
        getRadius: d => d.size,
        getColor: [255, 0, 255],
        radiusMinPixels: 1
      })
      // map.on('load', () => {
      //   map.addLayer(new MapboxLayer({ id: 'myBack', myDeck }))
      // })
      map.on('load', () => {
        map.addLayer(myLayer)
      })
    },
    deckDestroy () {
      this.deck.finalize()
    },
    deckAnimation () {
      const loopLength = 1800
      const animationSpeed = 30
      const timestamp = Date.now() / 1000
      const loopTime = loopLength / animationSpeed
      this.time = ((timestamp % loopTime) / loopTime) * loopLength
      this.deck.setProps(
        {
          layers: [
            new PolygonLayer({
              id: 'ground',
              data: landCover,
              getPolygon: f => f,
              stroked: false,
              getFillColor: [0, 0, 0, 0]
            }),
            new TripsLayer({
              id: 'trips',
              data: this.trips,
              getPath: d => d.path,
              getTimestamps: d => d.timestamps,
              getColor: d => (d.vendor === 0 ? this.theme.trailColor0 : this.theme.trailColor1),
              opacity: 0.3,
              widthMinPixels: 2,
              rounded: true,
              trailLength: this.trailLength,
              currentTime: this.time,
              shadowEnabled: false
            }),
            new PolygonLayer({
              id: 'buildings',
              data: this.buildings,
              extruded: true,
              wireframe: false,
              opacity: 0.5,
              getPolygon: f => f.polygon,
              getElevation: f => f.height,
              getFillColor: this.theme.buildingColor,
              material: this.theme.material
            })
          ]
        }
      )
      window.requestAnimationFrame(this.deckAnimation)
    }
  },
  mounted () {
    this.deckInitMap()
    this.deckAnimation()
  },
  beforeDestroy () {
    this.deckDestroy()
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

  #map1 {
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
  }
</style>
