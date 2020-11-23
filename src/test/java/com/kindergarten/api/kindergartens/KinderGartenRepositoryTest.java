package com.kindergarten.api.kindergartens;

import com.kindergarten.api.common.exception.CKinderGartenNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("dev")
public class KinderGartenRepositoryTest {
    @Autowired
    KinderGartenRepository kinderGartenRepository;


    @Test
    public void findByAddressContaining() {
        //given
        Pageable page = PageRequest.of(1, 10);
        String address = "";
        //when
        Page<KinderGarten> byAddressContaining = kinderGartenRepository.findByAddressContaining(address, page);
        //then
        List<KinderGarten> content = byAddressContaining.getContent();
        Assertions.assertThat(content.size()).isEqualTo(10);
    }

    @Test
    public void findByAddressContainingAndIsKinderFalse() {
        //given
        Pageable page = PageRequest.of(1, 10);
        String address = "";
        //when
        Page<KinderGarten> byAddressContainingAndIsKinderFalse = kinderGartenRepository.findByAddressContainingAndIsKinderFalse(address, page);
        //then
//        현재는 어린이집 데이터가 존재하지 않음
        List<KinderGarten> content = byAddressContainingAndIsKinderFalse.getContent();
        Assertions.assertThat(content.size()).isEqualTo(0);

    }

    @Test
    public void findByAddressContainingAndIsKinderTrue() {
        //given
        Pageable page = PageRequest.of(1, 10);
        String address = "";
        //when
        Page<KinderGarten> byAddressContaining = kinderGartenRepository.findByAddressContainingAndIsKinderTrue(address, page);
        //then
        List<KinderGarten> content = byAddressContaining.getContent();
        Assertions.assertThat(content.size()).isEqualTo(10);
    }

    @Test
    public void findByNameContaining() {
        //given
        Pageable page = PageRequest.of(1, 10);
        String name = "";
        //when
        Page<KinderGarten> byNameContaining = kinderGartenRepository.findByNameContaining(name, page);
        //then
        List<KinderGarten> content = byNameContaining.getContent();
        Assertions.assertThat(content.size()).isEqualTo(10);
    }

    @Test
    public void findByNameContainingAndIsKinderFalse() {
        //given
        Pageable page = PageRequest.of(1, 10);
        String name = "";
        //when
        Page<KinderGarten> byNameContainingAndIsKinderFalse = kinderGartenRepository.findByNameContainingAndIsKinderFalse(name, page);
        //then
        List<KinderGarten> content = byNameContainingAndIsKinderFalse.getContent();
        Assertions.assertThat(content.size()).isEqualTo(0);
    }

    @Test
    public void findByNameContainingAndIsKinderTrue() {
        //given
        Pageable page = PageRequest.of(1, 10);
        String name = "";
        //when
        Page<KinderGarten> byNameContainingAndIsKinderTrue = kinderGartenRepository.findByNameContainingAndIsKinderTrue(name, page);
        //then
        List<KinderGarten> content = byNameContainingAndIsKinderTrue.getContent();
        Assertions.assertThat(content.size()).isEqualTo(10);
    }

    @Test
    public void findById() {
        //given
        Long kindergartensID = 1L;
        //when
        KinderGarten kinderGarten = kinderGartenRepository.findById(kindergartensID).orElseThrow(CKinderGartenNotFoundException::new);
        Assertions.assertThat(kinderGarten.getId()).isEqualTo(kindergartensID);

    }
}