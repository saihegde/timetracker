package com.pavitlabs.timetracker.web.rest.mapper;

import com.pavitlabs.timetracker.domain.*;
import com.pavitlabs.timetracker.web.rest.dto.TimesheetEntryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TimesheetEntry and its DTO TimesheetEntryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TimesheetEntryMapper {

    @Mapping(source = "timesheet.id", target = "timesheetId")
    @Mapping(source = "task.id", target = "taskId")
    @Mapping(source = "task.name", target = "taskName")
    TimesheetEntryDTO timesheetEntryToTimesheetEntryDTO(TimesheetEntry timesheetEntry);

    @Mapping(source = "timesheetId", target = "timesheet")
    @Mapping(source = "taskId", target = "task")
    TimesheetEntry timesheetEntryDTOToTimesheetEntry(TimesheetEntryDTO timesheetEntryDTO);

    default Timesheet timesheetFromId(Long id) {
        if (id == null) {
            return null;
        }
        Timesheet timesheet = new Timesheet();
        timesheet.setId(id);
        return timesheet;
    }

    default Task taskFromId(Long id) {
        if (id == null) {
            return null;
        }
        Task task = new Task();
        task.setId(id);
        return task;
    }
}
