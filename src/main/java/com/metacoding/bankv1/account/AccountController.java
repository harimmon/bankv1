package com.metacoding.bankv1.account;

import com.metacoding.bankv1.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AccountController {
    private final AccountService accountService;
    private final HttpSession session;

    @PostMapping("/account/transfer")
    public String transfer(AccountRequest.TransferDTO transferDTO) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new RuntimeException("로그인 후 사용해주세요");

        accountService.계좌이체(transferDTO, sessionUser.getId());

        return "redirect:/"; // TODO
    }

    @GetMapping("/account/transfer-form")
    public String transferForm() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new RuntimeException("로그인 후 사용해주세요");
        return "account/transfer-form";
    }

    @GetMapping("/account")
    public String list(HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new RuntimeException("로그인 후 사용해주세요");

        // select * from account_tb where user_id = 1
        List<Account> accountList = accountService.나의계좌목록(sessionUser.getId());
        request.setAttribute("models", accountList);

        return "account/list";
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/account/save-form")
    public String saveForm() {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new RuntimeException("로그인 후 사용해주세요");
        return "account/save-form";
    }

    @PostMapping("/account/save")
    public String save(AccountRequest.SaveDTO saveDTO) {
        // 공통 부가 로직
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new RuntimeException("로그인 후 사용해주세요");

        accountService.계좌생성(saveDTO, sessionUser.getId());
        return "redirect:/account";
    }
}
