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
    public KinderGartenDTO.KindergatenPage findByAddress(String addr, Pageable pageable) {

        Page<KinderGarten> allByNameContaining = kinderGartenRepository.findByAddressContaining(addr, pageable);
        KinderGartenDTO.KindergatenPage createKinderUser = new KinderGartenDTO.KindergatenPage();

        createKinderUser.setKinderGartens(allByNameContaining.getContent());
        createKinderUser.setTotalPage(allByNameContaining.getTotalPages());
        createKinderUser.setCurrentpage(allByNameContaining.getPageable().getPageNumber());

        return createKinderUser;
    }

    @Transactional//주소검색시 유치원 어린이집 판별
    public KinderGartenDTO.KindergatenPage findByAddresswithKinder(String addr, Pageable pageable, boolean iskinder) {
        Page<KinderGarten> allByAdressContaining = null;
        if (iskinder) {
            allByAdressContaining = kinderGartenRepository.findByAddressContainingAndIsKinderTrue(addr, pageable);
        } else if (!iskinder) {
            allByAdressContaining = kinderGartenRepository.findByAddressContainingAndIsKinderFalse(addr, pageable);
        }

        KinderGartenDTO.KindergatenPage createKinderUser = new KinderGartenDTO.KindergatenPage();

        createKinderUser.setKinderGartens(allByAdressContaining.getContent());
        createKinderUser.setTotalPage(allByAdressContaining.getTotalPages());
        createKinderUser.setCurrentpage(allByAdressContaining.getPageable().getPageNumber());

        return createKinderUser;
    }

    @Transactional
    public KinderGartenDTO.KindergatenPage findByAllByName(String name, Pageable pageable) {
        Page<KinderGarten> allByNameContaining = kinderGartenRepository.findByNameContaining(name, pageable);
        KinderGartenDTO.KindergatenPage createKinderUser = new KinderGartenDTO.KindergatenPage();

        createKinderUser.setKinderGartens(allByNameContaining.getContent());
        createKinderUser.setTotalPage(allByNameContaining.getTotalPages());
        createKinderUser.setCurrentpage(allByNameContaining.getPageable().getPageNumber());

        return createKinderUser;
    }

    @Transactional//이름으로 검색시 유치원어린이집 판별
    public KinderGartenDTO.KindergatenPage findByAllByNamewithKinder(String name, Pageable pageable, boolean iskinder) {
        Page<KinderGarten> allByNameContaining = null;
        if (iskinder) {
            allByNameContaining = kinderGartenRepository.findByNameContainingAndIsKinderTrue(name, pageable);
        } else if (!iskinder) {
            allByNameContaining = kinderGartenRepository.findByNameContainingAndIsKinderFalse(name, pageable);
        }
        KinderGartenDTO.KindergatenPage createKinderUser = new KinderGartenDTO.KindergatenPage();

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
