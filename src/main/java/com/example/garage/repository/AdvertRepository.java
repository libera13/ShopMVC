package com.example.garage.repository;

import com.example.garage.model.Advert;
import com.example.garage.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertRepository extends JpaRepository<Advert,Long> {
    boolean existsByCar(Car c);
}
