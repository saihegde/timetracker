package com.pavitlabs.timetracker.repository;

import com.pavitlabs.timetracker.domain.Timesheet;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Timesheet entity.
 */
public interface TimesheetRepository extends JpaRepository<Timesheet,Long> {

}
