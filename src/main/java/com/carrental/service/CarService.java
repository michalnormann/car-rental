package com.carrental.service;

import com.carrental.model.CarResponse;
import com.carrental.repository.CarRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepo carRepo;

    public CarResponse getCar(Long id) {
        return carRepo.findById(id)
                .map(CarMapper::map)
                .orElseThrow(RuntimeException::new);
    }
}
