package com.pavitlabs.timetracker.repository;

import com.pavitlabs.timetracker.domain.Task;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Task entity.
 */
public interface TaskRepository extends JpaRepository<Task,Long> {

}
