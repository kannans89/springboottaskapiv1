package com.ksonline.training.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ksonline.training.dto.RegistrationDto;
import com.ksonline.training.dto.SubTaskDto;
import com.ksonline.training.dto.TaskDto;
import com.ksonline.training.entities.Category;
import com.ksonline.training.entities.SubTask;
import com.ksonline.training.entities.Task;
import com.ksonline.training.entities.User;
import com.ksonline.training.log.Logger;
import com.ksonline.training.repository.CategoryRepository;
import com.ksonline.training.repository.SubTaskRepository;
import com.ksonline.training.repository.TaskRepository;
import com.ksonline.training.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private SubTaskRepository subtaskRepository;

	@Transactional
	public void registerUser(RegistrationDto registerDto) {

		User user = new User();
		user.setEmail(registerDto.getEmail());
		user.setName(registerDto.getName());
		user.setPassword(registerDto.getPassword());
		userRepository.save(user);
	}

	@Transactional
	public void auth(String userName, String password) {

		Logger.log("inside auth");
		Logger.log(userName + " " + password);
		var optionalUser = userRepository.findUserByname(userName);
		if (optionalUser.isPresent()) {
			var user = optionalUser.get();
			if (!user.getPassword().equals(password)) {

				throw new BadCredentialsException("Bad Credentials");
			} else
				System.err.println("credentails correct");

		} else
			throw new BadCredentialsException("Bad Credentials");
	}

	@Transactional
	public int getTasksCount(String userName) {

		int taskCount = -1;
		var optionalUser = userRepository.findUserByname(userName);
		if (optionalUser.isPresent()) {

			var user = optionalUser.get();
			return user.getTasks().size();
		}

		return taskCount;
	}

	@Transactional
	public int getCategroyCount(String userName) {
		return getAllCategories(userName).size();
	}

	@Transactional
	public void addCategory(String userName, String title) {

		var optionalUser = userRepository.findUserByname(userName);
		if (optionalUser.isPresent()) {

			var user = optionalUser.get();
			var category = new Category();
			category.setUser(user);
			category.setTitle(title);
			categoryRepository.save(category);

		}

	}

	public List<Category> getAllCategories(String userName) {
		var optionalUser = userRepository.findUserByname(userName);
		List<Category> categories = new ArrayList<>();
		if (optionalUser.isPresent()) {

			var user = optionalUser.get();
			var userId = user.getId();

			var optionalCategoreis = categoryRepository.findCategoryByUser(user);
			if (optionalCategoreis.isPresent())
				categories = optionalCategoreis.get();
			// Logger.log(categories.size()+ "");
		}

		return categories;
	}

	@Transactional
	public void displayTasksOfUserId(long id) {

		var optionalUser = userRepository.findById(id);
		if (optionalUser.isPresent()) {
			var tasks = optionalUser.get().getTasks();
			System.err.println(tasks.size());
			for (var task : tasks)
				System.err.println(task.getTitle() + " " + task.getDescription());
		} else
			System.err.println("no taks found");

	}

	@Transactional
	public List<SubTaskDto> getSubTasks(String userName, long taskId) {

		var optionalUser = userRepository.findUserByname(userName);
		List<SubTaskDto> subtaskDtoList = new ArrayList<>();
		if (optionalUser.isPresent()) {
			var user = optionalUser.get();
			var optionalTask = taskRepository.findById(taskId);
			if (optionalTask.isPresent()) {
				var task = optionalTask.get();

				subtaskDtoList = task.getSubtasks().stream().map(s -> {
					var dto = new SubTaskDto();
					dto.setId(s.getId());
					dto.setDescription(s.getDescription());
					dto.setTitle(s.getTitle());
					return dto;
				}).collect(Collectors.toList());

			}
		}
		return subtaskDtoList;
	}

	@Transactional
	public List<TaskDto> getTasks(String userName) {

		var optionalUser = userRepository.findUserByname(userName);
		List<TaskDto> taskDtoList = new ArrayList<>();
		if (optionalUser.isPresent()) {

			var user = optionalUser.get();
			var tasks = user.getTasks();
			taskDtoList = tasks.stream().map((t) -> {

				var dto = new TaskDto();
				dto.setId(t.getId());
				dto.setCategoryId((int) t.getCategory().getId());
				dto.setCategoryName(t.getCategory().getTitle());
				dto.setSubTaskCount(t.getSubtasks().size());
				dto.setDescription(t.getDescription());
				dto.setPriority(t.getPriority());
				dto.setStatus(t.isCompleted() ? "completed" : "pending");
				dto.setTitle(t.getTitle());
				return dto;
			}).collect(Collectors.toList());

		}
		return taskDtoList;
	}
	
	@Transactional
	public SubTaskDto getSubTaskById(String userName,long taskId,long subTaskId) {
		
		var optionalUser = userRepository.findUserByname(userName);
		var dto = new SubTaskDto();

		if (optionalUser.isPresent()) {
			var optionalTask = taskRepository.findById(taskId);
			var user = optionalUser.get();
			if (optionalTask.isPresent()) {
				var task = optionalTask.get();
				var subtaskOptional = subtaskRepository.findById(subTaskId);
				if (subtaskOptional.isPresent()) {
					var subTask = subtaskOptional.get();
					dto.setDescription(subTask.getDescription());
					dto.setTitle(subTask.getTitle());
					dto.setId(subTask.getId());
					
				}
			}

		}
		return dto;
	}

	public void removeSubTask(String userName, long taskId, long subTaskId) {

		var optionalUser = userRepository.findUserByname(userName);

		if (optionalUser.isPresent()) {
			var optionalTask = taskRepository.findById(taskId);
			var user = optionalUser.get();
			if (optionalTask.isPresent()) {
				var task = optionalTask.get();
				var subtaskOptional = subtaskRepository.findById(subTaskId);
				if (subtaskOptional.isPresent()) {
					var subTask = subtaskOptional.get();
					if (task.getSubtasks().contains(subTask)) {
						task.getSubtasks().remove(subTask);
						subtaskRepository.delete(subTask);
					}
				}
			}

		}

	}

	@Transactional
	public void removeTask(String userName, long taskId) {

		Logger.log("in remove task service");

		var optionalUser = userRepository.findUserByname(userName);

		if (optionalUser.isPresent()) {
			var optionalTask = taskRepository.findById(taskId);
			var user = optionalUser.get();
			if (optionalTask.isPresent()) {
				var task = optionalTask.get();
				if (user.getTasks().contains(task)) {

					user.getTasks().remove(task);
					taskRepository.delete(task);
					Logger.log("task found for same user and deleted");
				} else
					Logger.log("task not found for the same user");
			}

		}
	}

	@Transactional
	public TaskDto getTaskById(String userName, long taskId) {

		var optionalUser = userRepository.findUserByname(userName);
		var dto = new TaskDto();
		if (optionalUser.isPresent()) {
			var user = optionalUser.get();
			var optionalTask = taskRepository.findById(taskId);
			if (optionalTask.isPresent()) {
				var task = optionalTask.get();
				dto.setId(taskId);
				dto.setTitle(task.getTitle());
				dto.setCategoryId((int) task.getCategory().getId());
				dto.setCategoryName(task.getCategory().getTitle());
				dto.setDescription(task.getDescription());
				dto.setPriority(task.getPriority());
				dto.setStatus(task.isCompleted() ? "completed" : "pending");

			}
		}
		return dto;

	}

	@Transactional
	public void addTask(String userName, TaskDto dto) {

		var optionalUser = userRepository.findUserByname(userName);

		if (optionalUser.isPresent()) {
			var user = optionalUser.get();
			var categoryId = dto.getCategoryId();
			var optionalCategory = categoryRepository.findById((long) categoryId);
			if (optionalCategory.isPresent()) {

				var category = optionalCategory.get();
				var task = new Task();
				task.setCategory(category);
				if (dto.getStatus().equalsIgnoreCase("completed"))
					task.setCompleted(true);
				task.setDescription(dto.getDescription());
				task.setTitle(dto.getTitle());
				task.setPriority(dto.getPriority());
				task.setUser(user);

				taskRepository.save(task);
				Logger.log("task saved");
			}
		}

	}

	@Transactional
	public void addSubTask(String userName, long taskId, SubTaskDto dto) {
		var optionalUser = userRepository.findUserByname(userName);

		if (optionalUser.isPresent()) {
			var user = optionalUser.get();
			var optionalTask = taskRepository.findById(taskId);
			if (optionalTask.isPresent()) {
				var task = optionalTask.get();

				var subtask = new SubTask();
				subtask.setDescription(dto.getDescription());
				subtask.setTitle(dto.getTitle());
				subtask.setTask(task);

				subtaskRepository.save(subtask);

			}

		}

	}

	
	@Transactional
	public void updateSubTask(String userName,long taskId,long subTaskId,SubTaskDto dto) {
		var optionalUser = userRepository.findUserByname(userName);

		if (optionalUser.isPresent()) {
			var user = optionalUser.get();
			var optionalTask = taskRepository.findById(taskId);
			if (optionalTask.isPresent()) {
			var optionalSubTask  =	subtaskRepository.findById(subTaskId);
			if(optionalSubTask.isPresent()) {
				
				var subtask= optionalSubTask.get();
				subtask.setDescription(dto.getDescription());
				subtask.setTitle(dto.getTitle());
				subtaskRepository.save(subtask);
			}
			
			}
		}
		
	}
	
	@Transactional
	public void updateTask(String userName, long taskId, TaskDto dto) {
		var optionalUser = userRepository.findUserByname(userName);

		if (optionalUser.isPresent()) {
			var user = optionalUser.get();
			var categoryId = dto.getCategoryId();
			var optionalCategory = categoryRepository.findById((long) categoryId);

			if (optionalCategory.isPresent()) {

				var category = optionalCategory.get();
				var optionalTask = taskRepository.findById(taskId);
				if (optionalTask.isPresent()) {
					var task = optionalTask.get();
					task.setCategory(category);
					task.setTitle(dto.getTitle());
					task.setCompleted(dto.getStatus().equalsIgnoreCase("completed"));
					task.setDescription(dto.getDescription());
					task.setPriority(dto.getPriority());

					// task.setUser(user);
					taskRepository.save(task);
				}
			}

		}

	}
}
