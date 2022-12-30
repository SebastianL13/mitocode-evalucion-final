package com.co.mitocode.service.impl;

import com.co.mitocode.model.Courses;
import com.co.mitocode.repository.ICoursesRepo;
import com.co.mitocode.repository.IGenericRepo;
import com.co.mitocode.service.ICoursesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CoursesServiceImpl extends CRUDImpl<Courses, String> implements ICoursesService {

    @Autowired
    private ICoursesRepo coursesRepo;

    @Override
    protected IGenericRepo<Courses, String> getRepo() {
        return coursesRepo;
    }
}
