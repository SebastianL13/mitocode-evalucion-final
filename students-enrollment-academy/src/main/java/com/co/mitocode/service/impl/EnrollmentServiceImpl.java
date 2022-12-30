package com.co.mitocode.service.impl;

import com.co.mitocode.model.Enrollment;
import com.co.mitocode.repository.IEnrollmentRepo;
import com.co.mitocode.repository.IGenericRepo;
import com.co.mitocode.service.IEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentServiceImpl extends CRUDImpl<Enrollment, String> implements IEnrollmentService {
    @Autowired
    private IEnrollmentRepo enrollmentRepo;

    @Override
    protected IGenericRepo<Enrollment, String> getRepo() {
        return enrollmentRepo;
    }
}
