package com.co.mitocode.service.impl;

import com.co.mitocode.model.Students;
import com.co.mitocode.repository.IGenericRepo;
import com.co.mitocode.repository.IStudentsRepo;
import com.co.mitocode.service.IStudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StudentsServiceImpl extends CRUDImpl<Students, String> implements IStudentsService {

    @Autowired
    private IStudentsRepo studentsRepo;

    @Override
    protected IGenericRepo<Students, String> getRepo() {
        return studentsRepo;
    }
}
