package com.metacoding.bankv1.account;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import(AccountRepository.class)
@DataJpaTest // 엔티티 매니저 em
public class AccountRepositoryTest {
    @Autowired // DI
    private AccountRepository accountRepository;

    @Test
    public void findAllByNumber_test() {

        // given
        int number = 1111;

        // when
        List<AccountResponse.DetailDTO> detailList = accountRepository.findAllByNumber(number);

        // eye
        for (AccountResponse.DetailDTO detail : detailList) {
            System.out.println(detail);
        }
    }
}