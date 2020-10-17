package com.kindergarten.api.service;

import com.kindergarten.api.common.exception.CKinderGartenNotFoundException;
import com.kindergarten.api.model.dto.KinderGartenDTO;
import com.kindergarten.api.model.entity.KinderGarten;
import com.kindergarten.api.repository.KinderGartenRepository;
import com.kindergarten.api.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
public class KinderGartenService {
    @Autowired
    private KinderGartenRepository kinderGartenRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public KinderGartenDTO.UserCreate findByAddress(String name, Pageable pageable) {
        Page<KinderGarten> allByNameContaining = kinderGartenRepository.findByAddressContaining(name, pageable);
        KinderGartenDTO.UserCreate createKinderUser = new KinderGartenDTO.UserCreate();

        createKinderUser.setKinderGartens(allByNameContaining.getContent());
        createKinderUser.setTotalPage(allByNameContaining.getTotalPages());
        createKinderUser.setCurrentpage(allByNameContaining.getPageable().getPageNumber());

        return createKinderUser;
    }

    @Transactional
    public KinderGartenDTO.UserCreate findByAllByName(String name, Pageable pageable) {
        Page<KinderGarten> allByNameContaining = kinderGartenRepository.findByNameContaining(name, pageable);
        KinderGartenDTO.UserCreate createKinderUser = new KinderGartenDTO.UserCreate();

        createKinderUser.setKinderGartens(allByNameContaining.getContent());
        createKinderUser.setTotalPage(allByNameContaining.getTotalPages());
        createKinderUser.setCurrentpage(allByNameContaining.getPageable().getPageNumber());

        return createKinderUser;
    }


    @Transactional
    public KinderGarten findById(Long id) {
        Optional<KinderGarten> byId = kinderGartenRepository.findById(id);

        return byId.orElseThrow(CKinderGartenNotFoundException::new);
    }


}
