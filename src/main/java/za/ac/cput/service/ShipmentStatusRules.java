package za.ac.cput.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import za.ac.cput.domain.Shipment.Status;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import static za.ac.cput.domain.Shipment.Status.*;

public final class ShipmentStatusRules {

    private static final Map<Status, Set<Status>> ALLOWED = new EnumMap<>(Status.class);

    static {
        ALLOWED.put(CREATED, EnumSet.of(READY_FOR_PICKUP, PICKED_UP, ON_HOLD, CANCELLED));
        ALLOWED.put(READY_FOR_PICKUP, EnumSet.of(PICKED_UP, DELAYED, ON_HOLD, CANCELLED));
        ALLOWED.put(PICKED_UP, EnumSet.of(IN_TRANSIT, AT_HUB, OUT_FOR_DELIVERY, DELAYED, ON_HOLD, LOST, DAMAGED));
        ALLOWED.put(IN_TRANSIT, EnumSet.of(AT_HUB, OUT_FOR_DELIVERY, DELAYED, ON_HOLD, LOST, DAMAGED));
        ALLOWED.put(AT_HUB, EnumSet.of(IN_TRANSIT, OUT_FOR_DELIVERY, DELAYED, ON_HOLD, RETURN_INITIATED, LOST, DAMAGED));
        ALLOWED.put(OUT_FOR_DELIVERY, EnumSet.of(DELIVERED, FAILED_DELIVERY, DELAYED, LOST, DAMAGED));
        ALLOWED.put(DELAYED, EnumSet.of(PICKED_UP, IN_TRANSIT, AT_HUB, OUT_FOR_DELIVERY, ON_HOLD, CANCELLED, LOST, DAMAGED));
        ALLOWED.put(ON_HOLD, EnumSet.of(READY_FOR_PICKUP, PICKED_UP, IN_TRANSIT, AT_HUB, OUT_FOR_DELIVERY, CANCELLED));
        ALLOWED.put(FAILED_DELIVERY, EnumSet.of(OUT_FOR_DELIVERY, AT_HUB, DELAYED, RETURN_INITIATED));
        ALLOWED.put(RETURN_INITIATED, EnumSet.of(RETURN_IN_TRANSIT, CANCELLED));
        ALLOWED.put(RETURN_IN_TRANSIT, EnumSet.of(RETURNED, LOST, DAMAGED));
    }

    private ShipmentStatusRules() {
    }

    public static boolean isAllowed(Status from, Status to) {
        if (to == null) return false;
        if (from == null || from == to) return true; // old rows with no status, or no change
        return ALLOWED.getOrDefault(from, EnumSet.noneOf(Status.class)).contains(to);
    }


    public static void check(Status from, Status to) {
        if (!isAllowed(from, to)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cannot change a shipment from " + from + " to " + to);
        }
    }
}