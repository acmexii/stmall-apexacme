package stmallapexacme.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import stmallapexacme.DeliveryApplication;
import stmallapexacme.domain.DeliveryCancelled;
import stmallapexacme.domain.DeliveryCompleted;
import stmallapexacme.domain.DeliveryReturned;
import stmallapexacme.domain.DeliveryStarted;

@Entity
@Table(name = "DeliveryMgmt_table")
@Data
//<<< DDD / Aggregate Root
public class DeliveryMgmt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String userId;
    private Long orderId;
    private String productName;
    private Long productId;
    private Integer qty;
    private String status;
    private Date deliveryDt;

    @PostPersist
    public void onPostPersist() {
        DeliveryStarted deliveryStarted = new DeliveryStarted(this);
        deliveryStarted.publishAfterCommit();
    }

    @PostUpdate
    public void onPostUpdate() {
        DeliveryCompleted deliveryCompleted = new DeliveryCompleted(this);
        deliveryCompleted.publishAfterCommit();

        DeliveryCancelled deliveryCancelled = new DeliveryCancelled(this);
        deliveryCancelled.publishAfterCommit();
    }

    @PreUpdate
    public void onPreUpdate() {
        DeliveryReturned deliveryReturned = new DeliveryReturned(this);
        deliveryReturned.publishAfterCommit();
    }

    public static DeliveryMgmtRepository repository() {
        DeliveryMgmtRepository deliveryMgmtRepository = DeliveryApplication.applicationContext.getBean(
            DeliveryMgmtRepository.class
        );
        return deliveryMgmtRepository;
    }

    public static void startDelivery(Ordered ordered) {
        DeliveryMgmt deliveryMgmt = new DeliveryMgmt();
        deliveryMgmt.setProductName(ordered.getProducName());
        deliveryMgmt.setProductId(ordered.getProductId());
        deliveryMgmt.setQty(ordered.getQty());
        deliveryMgmt.setStatus("DeliveryStarted");

        repository().save(deliveryMgmt);
        
        // DeliveryStarted deliveryStarted = new DeliveryStarted(deliveryMgmt);
        // deliveryStarted.publishAfterCommit();

/*         repository().findByOrderId(ordered.getId()).ifPresent(deliveryMgmt->{
            deliveryMgmt.setProductName(ordered.getProducName());
            repository().save(deliveryMgmt);
         }); */
    }



    public static void cancelDelivery(OrderCancelled orderCancelled) {
        //implement business logic here:

        /** Example 1:  new item 
        DeliveryMgmt deliveryMgmt = new DeliveryMgmt();
        repository().save(deliveryMgmt);

        DeliveryCancelled deliveryCancelled = new DeliveryCancelled(deliveryMgmt);
        deliveryCancelled.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        repository().findById(orderCancelled.get???()).ifPresent(deliveryMgmt->{
            
            deliveryMgmt // do something
            repository().save(deliveryMgmt);

            DeliveryCancelled deliveryCancelled = new DeliveryCancelled(deliveryMgmt);
            deliveryCancelled.publishAfterCommit();

         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
