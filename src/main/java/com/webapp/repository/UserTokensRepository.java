package com.webapp.repository;

import com.webapp.models.UserTokens;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@EnableScan
public interface UserTokensRepository extends CrudRepository<UserTokens, String> {
}
