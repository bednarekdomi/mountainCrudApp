package com.crudApp.mountain.service;

import com.crudApp.mountain.domain.*;
import com.crudApp.mountain.mapper.MountainMapper;
import com.crudApp.mountain.mapper.MountainRangeMapper;
import com.crudApp.mountain.repository.MountainRangeRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class MountainRangeServiceTest {

    private MountainRangeService mountainRangeService;

    @Mock
    private MountainRangeRepository mountainRangeRepository;

    @InjectMocks
    private MountainRangeMapper mountainRangeMapper;

    @InjectMocks
    private MountainMapper mountainMapper;

    private List<MountainRange> polishMountains = new ArrayList<>();
    private MountainRange tatraMountains;
    private MountainRange theSudetes;
    private List<Mountain> tatry = new ArrayList<>();
    private List<UserRating> userRatings = new ArrayList<>();
    private List<Mountain> sudetes = new ArrayList<>();
    private Set<User> usersList;

    @Before
    public void setUp() {
        mountainRangeService = new MountainRangeService(mountainRangeRepository, mountainRangeMapper, mountainMapper );
        Mountain rysy = new Mountain(1L, "Rysy", 2499, tatraMountains, "Poland", "Europe", userRatings, usersList);
        Mountain łomnica = new Mountain(2L, "Łomnica", 2634, tatraMountains, "Slovakia", "Europe", userRatings, usersList);
        tatry.add(rysy);
        tatry.add(łomnica);

        Mountain śnieżnik = new Mountain(3L, "Śnieżnik", 1423, theSudetes, "Poland", "Europe", userRatings, usersList);
        Mountain śnieżka = new Mountain(4L, "Śnieżka", 1603, theSudetes, "Poland", "Europe", userRatings, usersList);
        sudetes.add(śnieżnik);
        sudetes.add(śnieżka);

        tatraMountains = new MountainRange(1L, "Tatra Mountains", tatry);
        theSudetes = new MountainRange(2L, "Sudetes", sudetes);

        polishMountains.add(tatraMountains);
        polishMountains.add(theSudetes);

    }

    @Test
    public void shouldGetAllMountainRanges() {
        //Given
        when(mountainRangeRepository.findAll()).thenReturn(polishMountains);

        //When
        List<MountainRangeDto> allRanges = mountainRangeService.getAllMountainRanges();
        //Then
        Assert.assertEquals(2, allRanges.size());
    }

    @Test
    public void shouldGetMountainRange() {
        //Given
        when(mountainRangeRepository.getReferenceById(1L)).thenReturn(tatraMountains);
        //When
        MountainRangeDto tatra = mountainRangeService.getMountainRange(1L);
        //Then
        Assert.assertEquals("Tatra Mountains", tatra.getRangeName());
    }

    @Test
    public void shouldFindMountainRangeByNameLike() {
        //Given
        List<MountainRange> mountainRangeDtoList = new ArrayList<>();
        mountainRangeDtoList.add(tatraMountains);
        when(mountainRangeRepository.findByRangeNameLike("tat")).thenReturn(mountainRangeDtoList);
        //When
        mountainRangeService.findMountainRangeByNameLike("tat");
        //Then
        Assert.assertEquals(1, mountainRangeDtoList.size());

    }

    @Test
    public void shouldCreateMountainRange() {
        //Given
        MountainRangeDto mountainRangeDto = mountainRangeMapper.mapToMountainRangeDto(tatraMountains);
        //When
        mountainRangeService.createMountainRange(mountainRangeDto);
        //Then
        verify(mountainRangeRepository, times(1)).save(any(MountainRange.class));
    }

    @Test
    public void shouldUpdateMountainRange() {
        //Given
        MountainRangeDto mountainRangeDto = new MountainRangeDto(3L, "Tatra Mountains", tatry);
        //When
        mountainRangeService.updateMountainRange(mountainRangeDto);
        //Then
        Assert.assertEquals(Optional.of(3L), mountainRangeDto.getId());
    }

    @Test
    public void shouldDeleteMountainRange() {
        //Given
        MountainRangeDto mountainRangeDto = new MountainRangeDto(3L, "Tatra Mountains", tatry);
        Long mountainRangeId = mountainRangeDto.getId();
        //When
        mountainRangeService.deleteMountainRange(mountainRangeId);
        //Then
        verify(mountainRangeRepository, times(1)).deleteById(mountainRangeId);
    }

    @Test
    public void shouldGetMountainsFromRange() {
        //Given
        tatraMountains = new MountainRange(1L, "Tatra Mountains", tatry);
        Long rangeId = tatraMountains.getId();
        when(mountainRangeRepository.findById(rangeId)).thenReturn(Optional.ofNullable(tatraMountains));
        //When
        List<MountainDto> mountainsFromRange = mountainRangeService.getMountainsFromRange(rangeId);
        //Then
        Assert.assertEquals(2, mountainsFromRange.size());
    }
}