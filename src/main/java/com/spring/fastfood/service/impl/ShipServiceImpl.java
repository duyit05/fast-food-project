package com.spring.fastfood.service.impl;

import com.spring.fastfood.dto.request.ShipRequest;
import com.spring.fastfood.dto.response.ShipResponse;
import com.spring.fastfood.exception.DataExistException;
import com.spring.fastfood.exception.ResourceNotFoundException;
import com.spring.fastfood.model.Ship;
import com.spring.fastfood.repository.ShipRepository;
import com.spring.fastfood.service.ShipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShipServiceImpl implements ShipService {
    private final ShipRepository shipRepository;

    @Override
    public ShipResponse createShip(ShipRequest request) {
        if (shipRepository.existsByShipName(request.getShipName())) {
            throw new DataExistException("ship name already exist in system");
        }
        Ship ship = Ship.builder()
                .description(request.getDescription())
                .shipName(request.getShipName())
                .shipType(request.getType())
                .shipPrice(request.getShipPrice())
                .build();
        shipRepository.save(ship);
        return ShipResponse.builder()
                .shipId(ship.getId())
                .description(ship.getDescription())
                .shipName(ship.getShipName())
                .shipPrice(ship.getShipPrice())
                .type(ship.getShipType())
                .build();
    }

    @Override
    public List<ShipResponse> getAllShip() {
        List<Ship> ships = shipRepository.findAll();
        return ships.stream().map(
                ship -> {
                    return ShipResponse.builder()
                            .shipId(ship.getId())
                            .description(ship.getDescription())
                            .shipName(ship.getShipName())
                            .shipPrice(ship.getShipPrice())
                            .type(ship.getShipType())
                            .build();
                }

        ).collect(Collectors.toList());
    }

    @Override
    public ShipResponse updateShipById(long shipId, ShipRequest request) {
        Ship ship = shipRepository.findById(shipId)
                .orElseThrow(() -> new ResourceNotFoundException("can't found ship id: " + shipId));
        ship.setDescription(request.getDescription());
        ship.setShipName(request.getShipName());
        ship.setShipType(request.getType());
        ship.setShipPrice(request.getShipPrice());
        shipRepository.save(ship);

        return ShipResponse.builder()
                .shipId(ship.getId())
                .description(ship.getDescription())
                .shipName(ship.getShipName())
                .shipPrice(ship.getShipPrice())
                .type(ship.getShipType())
                .build();
    }

    @Override
    public void deleteShipById(long shipId) {
        shipRepository.deleteById(shipId);
    }
}
