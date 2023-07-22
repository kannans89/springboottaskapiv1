package com.ksonline.training.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ksonline.training.dto.CategoryDto;
import com.ksonline.training.dto.SubTaskDto;
import com.ksonline.training.dto.TaskCategoryCountDto;
import com.ksonline.training.dto.TaskDto;
import com.ksonline.training.log.Logger;
import com.ksonline.training.services.UserService;

@RestController
@RequestMapping("/api/v1/taskapp")
public class TaskController {

	@Autowired
	private UserService userService;

	@RequestMapping("/user/{userName}/taskcount")
	public ResponseEntity<Integer> getTaskCount(@PathVariable String userName) {

		Logger.log("received ," + userName);
		int count = userService.getTasksCount(userName);
		return ResponseEntity.status(HttpStatus.OK).body(count);

	}

	@RequestMapping("/user/{userName}/countSummary")
	public ResponseEntity<TaskCategoryCountDto> getCountSummary(@PathVariable String userName) {

		int taskCount = userService.getTasksCount(userName);
		int categoryCount = userService.getCategroyCount(userName);
		var dto = new TaskCategoryCountDto();
		dto.setCategoryCount(categoryCount);
		dto.setTaskCount(taskCount);
		return ResponseEntity.status(HttpStatus.OK).body(dto);

	}

	@PostMapping("/user/{userName}/addcategory")
	public ResponseEntity<?> addCategory(@PathVariable String userName, @RequestBody CategoryDto dto) {

		Logger.log(dto.getTitle());
		userService.addCategory(userName, dto.getTitle());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@RequestMapping("/user/{userName}/categories")
	public ResponseEntity<List<CategoryDto>> getCategories(@PathVariable String userName) {

		var listDto = userService.getAllCategories(userName).stream().map(c -> {
			var dto = new CategoryDto();
			dto.setId(c.getId());
			dto.setTitle(c.getTitle());
			return dto;
		}).collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(listDto);
	}

	@PostMapping("/user/{userName}/addTask")
	public ResponseEntity<?> addTask(@PathVariable String userName, @RequestBody TaskDto task) {

		userService.addTask(userName, task);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@RequestMapping("/user/{userName}/tasks")
	public ResponseEntity<List<TaskDto>> getTasks(@PathVariable String userName) {

		var tasksDto = userService.getTasks(userName);
		return ResponseEntity.status(HttpStatus.OK).body(tasksDto);
	}

	@GetMapping("/user/{userName}/task/{taskId}/subtasks")
	public ResponseEntity<List<SubTaskDto>> getSubTasks(@PathVariable String userName, @PathVariable long taskId) {

		var subTaskDto = userService.getSubTasks(userName, taskId);
		return ResponseEntity.status(HttpStatus.OK).body(subTaskDto);
	}

	@GetMapping("/user/{userName}/task/{taskId}")
	public ResponseEntity<TaskDto> getTaskById(@PathVariable String userName, @PathVariable long taskId) {

		TaskDto dto = userService.getTaskById(userName, taskId);
		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}

	@DeleteMapping("/user/{userName}/task/{taskId}")
	public ResponseEntity<?> deleteTask(@PathVariable String userName, @PathVariable long taskId) {
		Logger.log(userName);
		Logger.log(taskId + " ");
		userService.removeTask(userName, taskId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/user/{userName}/task/{taskId}/subtask/{subtaskId}")
	public ResponseEntity<?> deleteSubTask(@PathVariable String userName, @PathVariable long taskId,
			@PathVariable long subtaskId) {
		Logger.log(userName);
		Logger.log(taskId + " ");
		userService.removeSubTask(userName, taskId, subtaskId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@GetMapping("/user/{userName}/task/{taskId}/subtask/{subtaskId}")
	public ResponseEntity<SubTaskDto> getSubTaskById(@PathVariable String userName, @PathVariable long taskId,
			@PathVariable long subtaskId) {
		Logger.log(userName);
		Logger.log(taskId + " ");
		SubTaskDto dto = userService.getSubTaskById(userName, taskId, subtaskId);
		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}

	@PutMapping("/user/{userName}/task/{taskId}")
	public ResponseEntity<?> updateTask(@PathVariable String userName, @PathVariable long taskId,
			@RequestBody TaskDto taskDto) {

		Logger.log(taskDto.getTitle());
		userService.updateTask(userName, taskId, taskDto);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping("/user/{userName}/task/{taskId}/addSubtask")
	public ResponseEntity<?> addSubTask(@PathVariable String userName, @PathVariable long taskId,
			@RequestBody SubTaskDto subtaskDto) {

		// Logger.log(subtaskDto.getTitle()+ " "+subtaskDto.getDescription());
		userService.addSubTask(userName, taskId, subtaskDto);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PutMapping("/user/{userName}/task/{taskId}/subtask/{subtaskId}")
	public ResponseEntity<?> updateSubTask(@PathVariable String userName, @PathVariable long taskId,
			@PathVariable long subtaskId, @RequestBody SubTaskDto dto) {

		Logger.log(dto.getDescription());
		userService.updateSubTask(userName, taskId, subtaskId, dto);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
