package com.pavitlabs.timetracker.repository;

import com.pavitlabs.timetracker.domain.Project;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Project entity.
 */
public interface ProjectRepository extends JpaRepository<Project,Long> {

}
