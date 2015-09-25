package com.pavitlabs.timetracker.web.rest;

import com.pavitlabs.timetracker.Application;
import com.pavitlabs.timetracker.domain.TimesheetEntry;
import com.pavitlabs.timetracker.repository.TimesheetEntryRepository;
import com.pavitlabs.timetracker.web.rest.mapper.TimesheetEntryMapper;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TimesheetEntryResource REST controller.
 *
 * @see TimesheetEntryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TimesheetEntryResourceTest {


    private static final BigDecimal DEFAULT_HOURS = new BigDecimal(0);
    private static final BigDecimal UPDATED_HOURS = new BigDecimal(1);

    @Inject
    private TimesheetEntryRepository timesheetEntryRepository;

    @Inject
    private TimesheetEntryMapper timesheetEntryMapper;

    private MockMvc restTimesheetEntryMockMvc;

    private TimesheetEntry timesheetEntry;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TimesheetEntryResource timesheetEntryResource = new TimesheetEntryResource();
        ReflectionTestUtils.setField(timesheetEntryResource, "timesheetEntryRepository", timesheetEntryRepository);
        ReflectionTestUtils.setField(timesheetEntryResource, "timesheetEntryMapper", timesheetEntryMapper);
        this.restTimesheetEntryMockMvc = MockMvcBuilders.standaloneSetup(timesheetEntryResource).build();
    }

    @Before
    public void initTest() {
        timesheetEntry = new TimesheetEntry();
        timesheetEntry.setHours(DEFAULT_HOURS);
    }

    @Test
    @Transactional
    public void createTimesheetEntry() throws Exception {
        int databaseSizeBeforeCreate = timesheetEntryRepository.findAll().size();

        // Create the TimesheetEntry
        restTimesheetEntryMockMvc.perform(post("/api/timesheetEntrys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(timesheetEntry)))
                .andExpect(status().isCreated());

        // Validate the TimesheetEntry in the database
        List<TimesheetEntry> timesheetEntrys = timesheetEntryRepository.findAll();
        assertThat(timesheetEntrys).hasSize(databaseSizeBeforeCreate + 1);
        TimesheetEntry testTimesheetEntry = timesheetEntrys.get(timesheetEntrys.size() - 1);
        assertThat(testTimesheetEntry.getHours()).isEqualTo(DEFAULT_HOURS);
    }

    @Test
    @Transactional
    public void checkHoursIsRequired() throws Exception {
        int databaseSizeBeforeTest = timesheetEntryRepository.findAll().size();
        // set the field null
        timesheetEntry.setHours(null);

        // Create the TimesheetEntry, which fails.
        restTimesheetEntryMockMvc.perform(post("/api/timesheetEntrys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(timesheetEntry)))
                .andExpect(status().isBadRequest());

        List<TimesheetEntry> timesheetEntrys = timesheetEntryRepository.findAll();
        assertThat(timesheetEntrys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTimesheetEntrys() throws Exception {
        // Initialize the database
        timesheetEntryRepository.saveAndFlush(timesheetEntry);

        // Get all the timesheetEntrys
        restTimesheetEntryMockMvc.perform(get("/api/timesheetEntrys"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(timesheetEntry.getId().intValue())))
                .andExpect(jsonPath("$.[*].hours").value(hasItem(DEFAULT_HOURS.intValue())));
    }

    @Test
    @Transactional
    public void getTimesheetEntry() throws Exception {
        // Initialize the database
        timesheetEntryRepository.saveAndFlush(timesheetEntry);

        // Get the timesheetEntry
        restTimesheetEntryMockMvc.perform(get("/api/timesheetEntrys/{id}", timesheetEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(timesheetEntry.getId().intValue()))
            .andExpect(jsonPath("$.hours").value(DEFAULT_HOURS.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTimesheetEntry() throws Exception {
        // Get the timesheetEntry
        restTimesheetEntryMockMvc.perform(get("/api/timesheetEntrys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTimesheetEntry() throws Exception {
        // Initialize the database
        timesheetEntryRepository.saveAndFlush(timesheetEntry);

		int databaseSizeBeforeUpdate = timesheetEntryRepository.findAll().size();

        // Update the timesheetEntry
        timesheetEntry.setHours(UPDATED_HOURS);
        restTimesheetEntryMockMvc.perform(put("/api/timesheetEntrys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(timesheetEntry)))
                .andExpect(status().isOk());

        // Validate the TimesheetEntry in the database
        List<TimesheetEntry> timesheetEntrys = timesheetEntryRepository.findAll();
        assertThat(timesheetEntrys).hasSize(databaseSizeBeforeUpdate);
        TimesheetEntry testTimesheetEntry = timesheetEntrys.get(timesheetEntrys.size() - 1);
        assertThat(testTimesheetEntry.getHours()).isEqualTo(UPDATED_HOURS);
    }

    @Test
    @Transactional
    public void deleteTimesheetEntry() throws Exception {
        // Initialize the database
        timesheetEntryRepository.saveAndFlush(timesheetEntry);

		int databaseSizeBeforeDelete = timesheetEntryRepository.findAll().size();

        // Get the timesheetEntry
        restTimesheetEntryMockMvc.perform(delete("/api/timesheetEntrys/{id}", timesheetEntry.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TimesheetEntry> timesheetEntrys = timesheetEntryRepository.findAll();
        assertThat(timesheetEntrys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
