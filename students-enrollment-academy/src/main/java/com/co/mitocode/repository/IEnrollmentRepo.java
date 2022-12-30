package com.co.mitocode.repository;

import com.co.mitocode.model.Enrollment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IEnrollmentRepo extends IGenericRepo<Enrollment,String> {
}
