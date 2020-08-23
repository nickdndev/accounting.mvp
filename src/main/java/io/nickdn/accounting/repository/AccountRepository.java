package io.nickdn.accounting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import io.nickdn.accounting.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}
