package com.ksonline.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ksonline.training.entities.SubTask;

public interface SubTaskRepository extends JpaRepository<SubTask, Long>{

}
