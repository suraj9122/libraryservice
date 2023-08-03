package com.cms.studentportal.doa;

import com.cms.studentportal.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepositoryIfc extends JpaRepository<Account, Long> {

	Account findByStudentId(long studentId);

}
