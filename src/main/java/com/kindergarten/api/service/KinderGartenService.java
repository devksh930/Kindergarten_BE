package com.kindergarten.api.service;

import com.kindergarten.api.model.entity.KinderGarten;
import com.kindergarten.api.repository.KinderGartenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class KinderGartenService {
    @Autowired
    private KinderGartenRepository kinderGartenRepository;

    public List<KinderGarten> findByAddress(String address) {
        List<KinderGarten> kinderGartens = kinderGartenRepository.findByAddressContaining(address);

        return kinderGartenRepository.findByAddressContaining(address);
    }
}
