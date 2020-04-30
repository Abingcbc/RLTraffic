import pandas as pd
import json
import requests
from coordTransform_utils import gcj02_to_wgs84

def getKeyPoints():
    veh = pd.read_csv('new_veh.csv', dtype=str)
    with open('keypoints.txt', 'w') as file:
        for i in range(10):
            print('--- {} ---'.format(i))
            cur = veh[veh['id'] == str(i)]
            points = []
            time = []
            for j in range(len(cur)):
                if cur[j:j+1]['stime'].values[0] == cur[j:j+1]['etime'].values[0]:
                    continue
                points.append(str(round(eval(cur[j:j+1]['lng'].values[0]),6))+','+str(round(eval(cur[j:j+1]['lat'].values[0]),6)))
                time.append(int(eval(cur[j:j+1]['stime'].values[0])))
                if j+1 >= len(cur) or eval(cur[j:j+1]['etime'].values[0]) < eval(cur[j+1:j+2]['stime'].values[0]):
                    points.append(str(round(eval(cur[j:j+1]['tlng'].values[0]),6))+','+str(round(eval(cur[j:j+1]['tlat'].values[0]),6)))
                    time.append(int(eval(cur[j:j+1]['etime'].values[0])))
            print(len(points))
            file.write(str(points))
            file.write('\n')
            file.write(str(time))
            file.write('\n')

def getPolylines():
    keypoints = []
    with open('keypoints.txt', 'r') as file:
        for line in file:
            keypoints.append(eval(line))
    trips = []
    for i in range(0, 20, 2):
        path = []
        timestamps = []
        for j in range(len(keypoints[i])-1):
            params = {
                'key': 'a71df54e83197af58ff56fa239e11bf5',
                'origin': keypoints[i][j],
                'destination': keypoints[i][j+1],
            }
            response = requests.get('https://restapi.amap.com/v3/direction/driving', params=params)
            print('{}({}) - {}'.format(i//2, j, response.status_code))
            route = eval(str(response.content, encoding='utf-8'))['route']
            span = keypoints[i+1][j+1] - keypoints[i+1][j]
            all_points = []
            for step in route['paths'][0]['steps']:
                for point in step['polyline'].split(';'):
                    all_points.append(eval('['+point+']'))
            gap_unit = span // len(all_points)
            while gap_unit == 0:
                temp = []
                for m, point in enumerate(all_points):
                    if m%2 == 0:
                        temp.append(point)
                all_points = temp
                gap_unit = span // len(all_points)
            cur_time = keypoints[i+1][j]
            path += all_points
            timestamps += [cur_time+gap_unit*x for x in range(len(all_points))]
            if gap_unit == 0:
                print('zero problem')
                return
            elif gap_unit < 0:
                print('minus error')
                return
        print(len(path) == len(timestamps))
        trips.append({
            'path': path,
            'timestamps': timestamps
        })
    with open('trips.json', 'w') as file:
        json.dump(trips, file)

def coordTransform():
    with open('trips.json', 'r') as file:
        paths = json.load(file)
    for i, path in enumerate(paths):
        for j, point in enumerate(path['path']):
            temp = gcj02_to_wgs84(point[0], point[1])
            path['path'][j][0] = temp[0]
            path['path'][j][1] = temp[1]
    with open('new_trips.json', 'w') as file:
        json.dump(paths, file)

def generateOrder():
    req = pd.read_csv('req.csv', dtype=str)
    orders = []
    for index, row in req.iterrows():
        temp = {}
        temp['timestamp'] = int(eval(row['Tr']))
        temp['coordinates'] = gcj02_to_wgs84(float(row['olng']), float(row['olat']))
        orders.append(temp)
    orders.sort(key=lambda x: x['timestamp'])
    with open('order.json', 'w') as file:
        json.dump(orders, file)

def getWaitTime():
    waitTimeDF = pd.read_csv('cum_wait_time.csv')
    waitTime = []
    for index, row in waitTimeDF.iterrows():
        temp = {}
        temp['no'] = row['no']
        temp['simple'] = row['sar']
        temp['best'] = row['orp']
        temp['dqn'] = row['dqn']
        waitTime.append(temp)
    with open('waittime.json', 'w') as file:
        json.dump(waitTime, file)

def getDistance():
    distanceDF = pd.read_csv('rebalance_distance.csv')
    distance = []
    for index, row in distanceDF.iterrows():
        temp = {}
        temp['simple'] = row['sar']
        temp['best'] = row['orp']
        temp['dqn'] = row['dqn']
        distance.append(temp)
    with open('distance.json', 'w') as file:
        json.dump(distance, file)

# getKeyPoints()
# getPolylines()
# coordTransform()
# generateOrder()
# getWaitTime()
getDistance()