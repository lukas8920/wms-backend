package org.kehrbusch.receipt;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/v1/test")
public class TestController {
    private static final Logger logger = Logger.getLogger(TestController.class.getName());

    @PostMapping("inbound")
    public ResponseEntity<String> orderReceived(@RequestBody InboundPicking inboundPicking){
        logger.info(inboundPicking.getTest() + ", " + inboundPicking.getShipping_volume() + ", " + inboundPicking.getShipping_weight()
            + ", " + inboundPicking.getMoves());
        return ResponseEntity.ok("ok");
    }
}
