package org.tongji.traffic.dispatch.controller;

import com.csvreader.CsvReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tongji.traffic.dispatch.domain.TrafficOrder;
import org.tongji.traffic.dispatch.service.AndroidWSService;
import org.tongji.traffic.dispatch.service.DataScreenWSService;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.util.Random;

@Slf4j
@RestController
public class DispatchController {

    @Autowired
    DataScreenWSService dataScreenWSService;

    @Autowired
    AndroidWSService androidWSService;

    private ClassPathResource resource = new ClassPathResource("static/pick_up_infos.csv");

    @GetMapping("order")
    public TrafficOrder changeRunMode(HttpServletResponse response) {
        TrafficOrder trafficOrder = null;
        try {
            CsvReader csvReader = new CsvReader(new InputStreamReader(resource.getInputStream()));
            csvReader.readHeaders();
            Random random = new Random();
            int randomOrder = random.nextInt(54);
            for (int i = 0; i <= randomOrder; i++) {
                csvReader.readRecord();
            }
            trafficOrder = new TrafficOrder();
            trafficOrder.setReqId(Integer.valueOf(csvReader.get("req_id")));
            trafficOrder.setTimestamp(csvReader.get("pick_up_time"));
            trafficOrder.setDriverLatitude(Double.valueOf(csvReader.get("lat")));
            trafficOrder.setDriverLongitude(Double.valueOf(csvReader.get("lng")));
            trafficOrder.setPassengerLatitude(Double.valueOf(csvReader.get("olat")));
            trafficOrder.setPassengerLongitude(Double.valueOf(csvReader.get("olng")));
            trafficOrder.setDestinationLatitude(Double.valueOf(csvReader.get("dlat")));
            trafficOrder.setDestinationLongitude(Double.valueOf(csvReader.get("dlng")));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        log.info("order", trafficOrder);
        dataScreenWSService.sendMessage(trafficOrder.getTimestamp());
        androidWSService.sendMessage(trafficOrder);
        response.setStatus(HttpServletResponse.SC_OK);
        return trafficOrder;
    }

    @GetMapping("metrics")
    public String getCurrentConnection() {
        return "web: "+DataScreenWSService.sessionMap.size() +
                "\napp: "+AndroidWSService.sessionMap.size();
    }
}
