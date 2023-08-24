package com.springstudy.blogportfolio.service;

import com.springstudy.blogportfolio.dto.VisitDTO;
import com.springstudy.blogportfolio.entity.Visit;
import com.springstudy.blogportfolio.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

@Service
@RequiredArgsConstructor
@Log4j2
public class VisitServiceImpl implements VisitService{

    private final VisitRepository visitRepository;
    private final ModelMapper modelMapper;



    @Override
    public void GetVisit_Info(Long blogNo) {

        Visit visit = visitRepository.findByBlogsetting_BlogNo(blogNo);

        //블로그의 투데이 / 토탈  + 1
        visit.updateVisit(visit.getToday(), visit.getTotal());


         LocalDateTime date = LocalDateTime.now();
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        int dayOfWeekNumber = dayOfWeek.getValue();


        if (dayOfWeekNumber != visit.getWeek()) {
            visit.updateVisit(0, visit.getTotal());
            visit.setWeek(dayOfWeekNumber);
        }


        visitRepository.save(visit);

    }
}
