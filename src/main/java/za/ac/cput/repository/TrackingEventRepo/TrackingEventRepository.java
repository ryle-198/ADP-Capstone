package za.ac.cput.repository.TrackingEventRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.ac.cput.domain.TrackingEvent;

import java.util.List;

@Repository
public interface TrackingEventRepository extends JpaRepository<TrackingEvent, String> {

    List<TrackingEvent> findByShipment_ShipmentIdOrderByEventTimeAsc(String shipmentId);

    boolean existsByShipment_ShipmentId(String shipmentId);

    @Transactional
    void deleteByShipment_ShipmentId(String shipmentId);
}