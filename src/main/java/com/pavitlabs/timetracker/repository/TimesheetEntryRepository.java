package com.pavitlabs.timetracker.repository;

import com.pavitlabs.timetracker.domain.TimesheetEntry;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TimesheetEntry entity.
 */
public interface TimesheetEntryRepository extends JpaRepository<TimesheetEntry,Long> {

}
