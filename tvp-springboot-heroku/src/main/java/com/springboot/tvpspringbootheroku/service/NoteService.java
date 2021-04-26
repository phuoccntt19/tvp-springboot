package com.springboot.tvpspringbootheroku.service;

import com.springboot.tvpspringbootheroku.entity.TakeNotesEntity;
import com.springboot.tvpspringbootheroku.entity.User;

public interface NoteService {
    TakeNotesEntity postNote(TakeNotesEntity takeNotesEntity, User user);
}
