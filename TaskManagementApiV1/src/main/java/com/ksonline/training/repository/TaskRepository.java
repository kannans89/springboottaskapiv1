package com.ksonline.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ksonline.training.entities.Task;

public interface TaskRepository  extends JpaRepository<Task, Long>{

}
