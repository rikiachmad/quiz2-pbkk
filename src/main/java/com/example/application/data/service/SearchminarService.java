package com.example.application.data.service;

import com.example.application.data.entity.Seminar;
import com.example.application.data.entity.User;
import com.example.application.data.repository.SeminarRepository;
import com.example.application.data.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchminarService {

    private final SeminarRepository seminarRepository;

    public SearchminarService(SeminarRepository seminarRepository) {
        this.seminarRepository = seminarRepository;
    }

    public List<Seminar> findAllSeminars(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            return seminarRepository.findAll();
        }else{
            return seminarRepository.search(filterText);
        }
    }


    public long countSeminars(){
        return seminarRepository.count();
    }

    public void deleteSeminar(Seminar seminar){
        seminarRepository.delete(seminar);
    }

    public void saveSeminar(Seminar seminar){
        if (seminar == null) {
            System.err.println("Seminar is Null");
            return;
        }

        seminarRepository.save(seminar);
    }
}
