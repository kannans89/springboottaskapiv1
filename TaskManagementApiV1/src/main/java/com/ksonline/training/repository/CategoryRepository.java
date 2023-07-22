package com.ksonline.training.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ksonline.training.entities.Category;
import com.ksonline.training.entities.User;



public interface CategoryRepository extends JpaRepository<Category, Long> {

	Optional<List<Category>> findCategoryByUser(User user);
}
