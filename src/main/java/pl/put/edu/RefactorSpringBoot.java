package pl.put.edu;

import jakarta.persistence.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@SpringBootApplication
public class RefactorSpringBoot {
    public static void main(String[] args) {
        SpringApplication.run(RefactorSpringBoot.class, args);
    }
}

@Entity
@Table(name = "tasks")
class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private boolean completed;

    public Task() {}

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.completed = false;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}

interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCompleted(boolean completed);
}

@Service
class TaskService {
    private final TaskRepository repo;

    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    public List<Task> findAll() { return repo.findAll(); }
    public Optional<Task> findById(Long id) { return repo.findById(id); }
    public Task create(Task t) { return repo.save(t); }
    public Task update(Task t) { return repo.save(t); }
    public void delete(Long id) { repo.deleteById(id); }
}

@RestController
@RequestMapping("/api/tasks")
class TaskRestController {
    private final TaskService service;

    public TaskRestController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public List<Task> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Task getById(@PathVariable Long id) {
        return service.findById(id).orElseThrow();
    }

    @PostMapping
    public Task create(@RequestBody Task t) {
        return service.create(t);
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @RequestBody Task updated) {
        Task t = service.findById(id).orElseThrow();
        t.setTitle(updated.getTitle());
        t.setDescription(updated.getDescription());
        t.setCompleted(updated.isCompleted());
        return service.update(t);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

@Controller
class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tasks", service.findAll());
        return "index";
    }

    @GetMapping("/task/new")
    public String createForm(Model model) {
        model.addAttribute("task", new Task());
        return "task-form";
    }

    @PostMapping("/task")
    public String create(Task task) {
        service.create(task);
        return "redirect:/";
    }

    @GetMapping("/task/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("task", service.findById(id).orElseThrow());
        return "task-form";
    }

    @PostMapping("/task/update/{id}")
    public String update(@PathVariable Long id, Task updated) {
        Task t = service.findById(id).orElseThrow();
        t.setTitle(updated.getTitle());
        t.setDescription(updated.getDescription());
        t.setCompleted(updated.isCompleted());
        service.update(t);
        return "redirect:/";
    }

    @GetMapping("/task/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/";
    }
}