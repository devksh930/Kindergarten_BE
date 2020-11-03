package com.kindergarten.api.service;

import com.kindergarten.api.common.exception.CKinderGartenNotFoundException;
import com.kindergarten.api.model.dto.KinderGartenDTO;
import com.kindergarten.api.model.entity.KinderGarten;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("dev")
public class KinderGartenServiceTest {
    @Autowired
    private KinderGartenService kinderGartenService;

    @Test
    public void findByAddress() {
        PageRequest pageRequest = PageRequest.of(1, 20);

        KinderGartenDTO.KindergatenPage kinderGartenServiceByAddress = kinderGartenService.findByAddress("", pageRequest);


        Assertions.assertThat(kinderGartenServiceByAddress.getCurrentpage()).isEqualTo(1);
        Assertions.assertThat(kinderGartenServiceByAddress.getKinderGartens()).isNotNull();
    }

    @Test
    public void findByAllByName() {
        PageRequest pageRequest = PageRequest.of(1, 20);

        KinderGartenDTO.KindergatenPage kinderGartenServiceByAddress = kinderGartenService.findByAllByName("", pageRequest);


        Assertions.assertThat(kinderGartenServiceByAddress.getCurrentpage()).isEqualTo(1);
        Assertions.assertThat(kinderGartenServiceByAddress.getKinderGartens()).isNotNull();
    }

    @Test
    public void findById() {
        KinderGarten byId = kinderGartenService.findById(1L);
        Assertions.assertThat(byId.getId()).isEqualTo(1L);
    }

    @Test(expected = CKinderGartenNotFoundException.class)
    public void findById_exception() {
        KinderGarten byId = kinderGartenService.findById(9999L);

    }

}