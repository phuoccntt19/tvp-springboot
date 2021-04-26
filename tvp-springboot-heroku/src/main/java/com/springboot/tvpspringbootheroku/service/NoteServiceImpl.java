package com.springboot.tvpspringbootheroku.service;

import com.springboot.tvpspringbootheroku.dao.NoteRepository;
import com.springboot.tvpspringbootheroku.entity.TakeNotesEntity;
import com.springboot.tvpspringbootheroku.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    NoteRepository noteRepository;


    @Override
    public TakeNotesEntity postNote(TakeNotesEntity takeNotesEntity, User user) {
        takeNotesEntity.setDateCreate(LocalDateTime.now());
        takeNotesEntity.setUser(user);
        noteRepository.save(takeNotesEntity);
        return takeNotesEntity;
    }
}
