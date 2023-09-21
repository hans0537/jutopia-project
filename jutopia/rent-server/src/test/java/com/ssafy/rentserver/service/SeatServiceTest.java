package com.ssafy.rentserver.service;

import com.ssafy.rentserver.enums.SeatStatus;
import com.ssafy.rentserver.model.Seat;
import com.ssafy.rentserver.repository.SeatRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;


@SpringBootTest
class SeatServiceTest {

    @Autowired
    private SeatService seatService;

    @Autowired
    private SeatRepository seatRepository;

    @Test
    @DisplayName("행,렬,반 번호가 주어졌을 때 해당 개수의 반좌석을 생성한다.")
    public void test1(){
        //given
        int row = 2;
        int col = 3;
        int classNumber = 1;
        //when
        List<Seat> seats = seatService.createGrid(row, col, classNumber);

        //then
        Assertions.assertThat(seats).hasSize(row*col);
        Assertions.assertThat(seats).allMatch(seat -> seat.getPrice().equals(new BigDecimal(1000)));
        Assertions.assertThat(seats).allMatch(seat -> seat.getClass_number() == classNumber);
        Assertions.assertThat(seats).allMatch(seat -> seat.getSeatStatus() == SeatStatus.POSSIBLE);

    }
}