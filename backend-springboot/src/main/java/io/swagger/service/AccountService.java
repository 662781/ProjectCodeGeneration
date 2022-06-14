package io.swagger.service;

import io.swagger.model.entity.Account;
import io.swagger.repo.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {
    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private AccountIbanGenService accountIbanGenService;

    //account validation
    public Account addAccount(Account a) {
        //check if all fields have been filled
        if(!accountIbanGenService.allFieldsFilled(a)) {
            throw new IllegalArgumentException("Something went wrong creating your account.");
        }
        //check if pincode is of type INT and 4 digits long
        if (!accountIbanGenService.pinCheck(a.getPinCode())) {
            throw new IllegalArgumentException("Pin code has to be 4 digits long and of type Integer.");
        }

        if(accountIbanGenService.allFieldsFilled(a) && accountIbanGenService.pinCheck(a.getPinCode())) {
            String iban = accountIbanGenService.generateIban();
            //check if there is an iban already assigned to the account and if the generated iban is valid to set the generated iban
                if(iban.length() != 0 && a.getIban().length() != 0) {
                    a.setIban(iban);
                }
                //if the active status is not specified put it on active
                if (a.getActive() == null) {
                    a.setActive(true);
                }
            return accountRepo.save(a);
        }
        throw new IllegalArgumentException("Something went wrong trying to add your account.");

    }


    public List<Account> findAccountsByUserId(UUID userId) {
        if(!accountRepo.findAccountsByUserId(userId).isEmpty()) {
            return accountRepo.findAccountsByUserId(userId);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public Account updateAccount(Account updatedAccount) {
            return accountRepo.save(updatedAccount);
    }

    public Account findAccountByIban(String iban) {
        //find account by iban logic
        if (accountRepo.findAccountByIban(iban) != null) {
            return accountRepo.findAccountByIban(iban);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }
    public List<Account> getAll() {
        //this deletes the bank account from the list
        List<Account> accountList =  accountRepo.findAll();
        accountList.removeIf(account -> account.getIban().equals("NL01INHO0000000001"));
        return accountList;
    }
}
