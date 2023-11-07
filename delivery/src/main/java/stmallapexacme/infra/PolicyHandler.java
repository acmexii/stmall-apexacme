package stmallapexacme.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import stmallapexacme.config.kafka.KafkaProcessor;
import stmallapexacme.domain.*;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    DeliveryMgmtRepository deliveryMgmtRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener( value = KafkaProcessor.INPUT, condition = "headers['type']=='Ordered'")
    public void wheneverOrdered_StartDelivery(@Payload Ordered ordered) {
        Ordered event = ordered;
        DeliveryMgmt.startDelivery(event);
    }

    
    @StreamListener(value = KafkaProcessor.INPUT, condition = "headers['type']=='OrderCancelled'")
    public void wheneverOrderCancelled_CancelDelivery(
        @Payload OrderCancelled orderCancelled
    ) {
        OrderCancelled event = orderCancelled;
        DeliveryMgmt.cancelDelivery(event);
    }
}
