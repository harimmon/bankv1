package com.metacoding.bankv1.account;

import com.metacoding.bankv1.account.history.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final HistoryRepository historyRepository;

    @Transactional
    public void 계좌생성(AccountRequest.SaveDTO saveDTO, int userId) {
        accountRepository.save(saveDTO.getNumber(), saveDTO.getPassword(), saveDTO.getBalance(), userId);
    }

    public List<Account> 나의계좌목록(Integer userId) {
        return accountRepository.findAllByUserId(userId);
    }

    @Transactional
    public void 계좌이체(AccountRequest.TransferDTO transferDTO, int userId) {
        // 1. 출금 계좌 조회, 없으면 RuntimeException
        Account withdrawAccount = accountRepository.findByNumber(transferDTO.getWithdrawNumber());
        if (withdrawAccount == null) throw new RuntimeException("출금 계좌가 존재하지 않습니다.");

        // 2. 입금 계좌 조회, 없으면 RuntimeException
        Account depositAccount = accountRepository.findByNumber(transferDTO.getDepositNumber());
        if (depositAccount == null) throw new RuntimeException("입금 계좌가 존재하지 않습니다.");

        // 3. 출금 계좌의 잔액 조회
        if (withdrawAccount.getBalance() < transferDTO.getAmount()) {
            throw new RuntimeException("출금 계좌의 잔액 : " + withdrawAccount.getBalance() + ", 이체하려는 금액 : " + transferDTO.getAmount());
        }

        // 4. 출금 계좌 비밀번호 확인해서 동일한지 체크
        if (!(withdrawAccount.getPassword().equals(transferDTO.getWithdrawPassword()))) {
            throw new RuntimeException("출금 계좌 비밀번호가 틀렸습니다.");
        }

        // 5. 출금 계좌 주인이 맞는지 확인 (로그인한 유저가)
        if (!(withdrawAccount.getUserId().equals(userId))) {
            throw new RuntimeException("출금 계좌의 권한이 없습니다.");
        }

        // 6. Account Update 출금 계좌
        int withdrawBalance = withdrawAccount.getBalance();
        withdrawBalance = withdrawBalance - transferDTO.getAmount();
        accountRepository.updateByNumber(withdrawBalance, withdrawAccount.getPassword(), withdrawAccount.getNumber());

        // 7. Account Update 입금 계좌
        int depositBalance = depositAccount.getBalance();
        depositBalance = depositBalance + transferDTO.getAmount();
        accountRepository.updateByNumber(depositBalance, depositAccount.getPassword(), depositAccount.getNumber());

        // 8. History Save
        historyRepository.save(transferDTO.getWithdrawNumber(), transferDTO.getDepositNumber(), transferDTO.getAmount(), withdrawBalance, depositAccount.getBalance());
    }

    public List<AccountResponse.DetailDTO> 계좌상세보기(int number, String type, Integer sessionUserId) {
        // 1. 계좌 존재 확인
        Account account = accountRepository.findByNumber(number);
        if (account == null) throw new RuntimeException("계좌가 존재하지 않습니다.");

        // 2. 계좌 주인 확인
        if (!(account.getUserId().equals(sessionUserId))) {
            throw new RuntimeException("계좌의 권한이 없습니다.");
        }

        // 3. 조회해서 주기
        List<AccountResponse.DetailDTO> detailList = accountRepository.findAllByNumber(number, type);
        return detailList;
    }
}
