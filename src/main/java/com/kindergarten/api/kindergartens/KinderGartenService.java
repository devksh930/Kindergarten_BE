package com.kindergarten.api.kindergartens;

import com.kindergarten.api.common.exception.CKinderGartenNotFoundException;
import com.kindergarten.api.student.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor

public class KinderGartenService {
    private final KinderGartenRepository kinderGartenRepository;
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public KinderGartenDTO.KindergatenPage findByAddress(String addr, Pageable pageable) {

        Page<KinderGarten> allByNameContaining = kinderGartenRepository.findByAddressContaining(addr, pageable);
        KinderGartenDTO.KindergatenPage createKinderUser = new KinderGartenDTO.KindergatenPage();

        createKinderUser.setTotalElements(allByNameContaining.getTotalElements());
        createKinderUser.setKinderGartens(allByNameContaining.getContent());
        createKinderUser.setTotalPage(allByNameContaining.getTotalPages());
        createKinderUser.setCurrentpage(allByNameContaining.getPageable().getPageNumber());

        return createKinderUser;
    }

    @Transactional
    public KinderGartenDTO.KindergatenPage findByAllByName(String name, Pageable pageable) {
        Page<KinderGarten> allByNameContaining = kinderGartenRepository.findByNameContaining(name, pageable);
        KinderGartenDTO.KindergatenPage createKinderUser = new KinderGartenDTO.KindergatenPage();

        createKinderUser.setTotalElements(allByNameContaining.getTotalElements());
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
        createKinderUser.setTotalElements(allByAdressContaining.getTotalElements());
        createKinderUser.setKinderGartens(allByAdressContaining.getContent());
        createKinderUser.setTotalPage(allByAdressContaining.getTotalPages());
        createKinderUser.setCurrentpage(allByAdressContaining.getPageable().getPageNumber());

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

        createKinderUser.setTotalElements(allByNameContaining.getTotalElements());
        createKinderUser.setKinderGartens(allByNameContaining.getContent());
        createKinderUser.setTotalPage(allByNameContaining.getTotalPages());
        createKinderUser.setCurrentpage(allByNameContaining.getPageable().getPageNumber());

        return createKinderUser;
    }


    @Transactional
    public KinderGarten findById(Long id) {

        return kinderGartenRepository.findById(id).orElseThrow(CKinderGartenNotFoundException::new);
    }


}
