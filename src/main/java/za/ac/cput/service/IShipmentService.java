/* IShipmentService.java
   Shipment Service interface
   Author: NOMPUMELELO MBATHA (240256727)
   Date: 2026 */

package za.ac.cput.service;

import za.ac.cput.domain.Shipment;
import java.util.List;

public interface IShipmentService extends IService<Shipment, String>{
    List<Shipment> getAll();
}