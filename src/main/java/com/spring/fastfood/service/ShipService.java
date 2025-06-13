package com.spring.fastfood.service;

import com.spring.fastfood.dto.request.ShipRequest;
import com.spring.fastfood.dto.response.ShipResponse;
import com.spring.fastfood.model.Ship;

import java.util.List;

public interface ShipService {
    ShipResponse createShip (ShipRequest request);
    List<ShipResponse> getAllShip ();
    ShipResponse updateShipById (long shipId, ShipRequest request);
    void deleteShipById (long shipId);
    Ship findShipById (long shipId);
}
