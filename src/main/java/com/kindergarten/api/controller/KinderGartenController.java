package com.kindergarten.api.controller;

import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.common.result.SingleResult;
import com.kindergarten.api.kindergartens.KinderGarten;
import com.kindergarten.api.kindergartens.KinderGartenDTO;
import com.kindergarten.api.kindergartens.KinderGartenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RestController
@RequestMapping("/api/kindergartens")
@EnableSwagger2
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class KinderGartenController {
    private final KinderGartenService kinderGartenService;
    private final ResponseService responseService;
    private final ModelMapper modelMapper;


    //유치원 이름으로 검색
    @GetMapping("/name") //GET:/api/kindergartens&name
    public SingleResult<KinderGartenDTO.KindergatenPage> findbyName(@RequestParam(value = "name") String name, @RequestParam(required = false) String iskinder, Pageable pageable) {
        KinderGartenDTO.KindergatenPage page = new KinderGartenDTO.KindergatenPage();
        if (iskinder == null || iskinder.equals("")) {
            log.debug("전체검색");
            page = kinderGartenService.findByAllByName(name, pageable);

        } else if (iskinder.equals("daycare")) {
            log.debug("어린이집");
            page = kinderGartenService.findByAllByNamewithKinder(name, pageable, false);

        } else if (iskinder.equals("kinder")) {
            log.debug("유치원");
            page = kinderGartenService.findByAllByNamewithKinder(name, pageable, true);

        }
        return responseService.getSingleResult(page);
    }

    //유치원 주소로 검색
    @GetMapping("/addr")//GET:/api/kindergartens/addr&addr
    public SingleResult<KinderGartenDTO.KindergatenPage> findbyAdress(@RequestParam(value = "addr") String addr, @RequestParam(required = false) String iskinder, Pageable pageable) {
        KinderGartenDTO.KindergatenPage page = new KinderGartenDTO.KindergatenPage();
        if (iskinder == null || iskinder.equals("")) {
            log.debug("전체검색");
            page = kinderGartenService.findByAddress(addr, pageable);

        } else if (iskinder.equals("daycare")) {
            log.debug("어린이집");
            page = kinderGartenService.findByAddresswithKinder(addr, pageable, false);

        } else if (iskinder.equals("kinder")) {
            log.debug("유치원");
            page = kinderGartenService.findByAddresswithKinder(addr, pageable, true);

        }
        return responseService.getSingleResult(page);
    }

    //유치원 상세조회
    @GetMapping("/{id}")//GET:/api/kindergartens/{id}
    public SingleResult<KinderGartenDTO.KinderGartenDetail> detailKindergarten(@PathVariable Long id) {
        KinderGarten byId = kinderGartenService.findById(id);
        KinderGartenDTO.KinderGartenDetail map = modelMapper.map(byId, KinderGartenDTO.KinderGartenDetail.class);
        return responseService.getSingleResult(map);
    }

}
