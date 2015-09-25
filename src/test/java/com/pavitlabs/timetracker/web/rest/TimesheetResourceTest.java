package com.pavitlabs.timetracker.web.rest;

import com.pavitlabs.timetracker.Application;
import com.pavitlabs.timetracker.domain.Timesheet;
import com.pavitlabs.timetracker.repository.TimesheetRepository;
import com.pavitlabs.timetracker.web.rest.mapper.TimesheetMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pavitlabs.timetracker.domain.enumeration.TimesheetStatus;

/**
 * Test class for the TimesheetResource REST controller.
 *
 * @see TimesheetResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TimesheetResourceTest {


    private static final TimesheetStatus DEFAULT_STATUS = TimesheetStatus.SAVED;
    private static final TimesheetStatus UPDATED_STATUS = TimesheetStatus.PENDING;

    @Inject
    private TimesheetRepository timesheetRepository;

    @Inject
    private TimesheetMapper timesheetMapper;

    private MockMvc restTimesheetMockMvc;

    private Timesheet timesheet;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TimesheetResource timesheetResource = new TimesheetResource();
        ReflectionTestUtils.setField(timesheetResource, "timesheetRepository", timesheetRepository);
        ReflectionTestUtils.setField(timesheetResource, "timesheetMapper", timesheetMapper);
        this.restTimesheetMockMvc = MockMvcBuilders.standaloneSetup(timesheetResource).build();
    }

    @Before
    public void initTest() {
        timesheet = new Timesheet();
        timesheet.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createTimesheet() throws Exception {
        int databaseSizeBeforeCreate = timesheetRepository.findAll().size();

        // Create the Timesheet
        restTimesheetMockMvc.perform(post("/api/timesheets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(timesheet)))
                .andExpect(status().isCreated());

        // Validate the Timesheet in the database
        List<Timesheet> timesheets = timesheetRepository.findAll();
        assertThat(timesheets).hasSize(databaseSizeBeforeCreate + 1);
        Timesheet testTimesheet = timesheets.get(timesheets.size() - 1);
        assertThat(testTimesheet.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void getAllTimesheets() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

        // Get all the timesheets
        restTimesheetMockMvc.perform(get("/api/timesheets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(timesheet.getId().intValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    public void getTimesheet() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

        // Get the timesheet
        restTimesheetMockMvc.perform(get("/api/timesheets/{id}", timesheet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(timesheet.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTimesheet() throws Exception {
        // Get the timesheet
        restTimesheetMockMvc.perform(get("/api/timesheets/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTimesheet() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

		int databaseSizeBeforeUpdate = timesheetRepository.findAll().size();

        // Update the timesheet
        timesheet.setStatus(UPDATED_STATUS);
        restTimesheetMockMvc.perform(put("/api/timesheets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(timesheet)))
                .andExpect(status().isOk());

        // Validate the Timesheet in the database
        List<Timesheet> timesheets = timesheetRepository.findAll();
        assertThat(timesheets).hasSize(databaseSizeBeforeUpdate);
        Timesheet testTimesheet = timesheets.get(timesheets.size() - 1);
        assertThat(testTimesheet.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteTimesheet() throws Exception {
        // Initialize the database
        timesheetRepository.saveAndFlush(timesheet);

		int databaseSizeBeforeDelete = timesheetRepository.findAll().size();

        // Get the timesheet
        restTimesheetMockMvc.perform(delete("/api/timesheets/{id}", timesheet.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Timesheet> timesheets = timesheetRepository.findAll();
        assertThat(timesheets).hasSize(databaseSizeBeforeDelete - 1);
    }
}
