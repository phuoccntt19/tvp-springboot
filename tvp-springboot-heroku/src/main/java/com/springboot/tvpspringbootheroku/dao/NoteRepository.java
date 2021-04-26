package com.springboot.tvpspringbootheroku.dao;

import com.springboot.tvpspringbootheroku.entity.TakeNotesEntity;
import com.springboot.tvpspringbootheroku.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<TakeNotesEntity, Long> {
    List<TakeNotesEntity> findByUser(User user);
}
