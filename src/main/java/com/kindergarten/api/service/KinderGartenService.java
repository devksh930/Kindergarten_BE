package com.kindergarten.api.service;

import com.kindergarten.api.model.entity.KinderGarten;
import com.kindergarten.api.repository.KinderGartenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
public class KinderGartenService {
    @Autowired
    private KinderGartenRepository kinderGartenRepository;

    @Transactional
    public List<KinderGarten> findByAddress(String address) {
        List<KinderGarten> kinderGartens = kinderGartenRepository.findByAddressContaining(address);

        return kinderGartenRepository.findByAddressContaining(address);
    }

    @Transactional
    public Page<KinderGarten> findByAllByName(String name, Pageable pageable) {

        return kinderGartenRepository.findAllByNameContaining(name, pageable);
    }
}
