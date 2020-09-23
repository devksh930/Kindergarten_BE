package com.kindergarten.api.repository;

import com.kindergarten.api.model.entity.KinderGarten;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KinderGartenRepository extends JpaRepository<KinderGarten, Long> {
    List<KinderGarten> findByAddressContaining(String address);

}
