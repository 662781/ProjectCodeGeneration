package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.dto.TokenDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.Transaction;
import io.swagger.model.entity.User;
import io.swagger.model.enumeration.AccountType;
import io.swagger.model.enumeration.TransactionType;
import io.swagger.model.enumeration.UserType;
import io.swagger.repo.AccountRepo;
import io.swagger.repo.UserRepo;
import io.swagger.service.AccountService;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@WebMvcTest(TransactionsApiController.class)
public class TransactionsApiControllerTest {

    private static final String testIban1 = "TESTIBAN001";
    private static final String testIban2 = "TESTIBAN002";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private UserService userService;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountRepo accountRepo;

    @MockBean
    private UserRepo userRepo;


    @Autowired
    private ObjectMapper objectMapper;

    public Transaction transaction;



    private String jwt;



//    @BeforeEach
//    void setup() throws IOException {
//        mockMvc = MockMvcBuilders.webAppContextSetup(context)
//                .build();

//
//        List<User> users = new ArrayList<>();
//        List<Account> accounts = new ArrayList<>();
//        List<Transaction> transactions = new ArrayList<>();
//
//
//        // Bas is customer
//        User bas = new User();
//        bas.setUsername("BasH");
//        bas.setPassword("test123");
//        bas.setFirstname("Bas");
//        bas.setLastname("Hulskamp");
//        bas.setDob(LocalDate.of(2000, 1, 1));
//        bas.setAddress("Testweg 1");
//        bas.setEmail("test@mail.com");
//        bas.setPhone("+31612345678");
//        bas.setRegisteredOn(OffsetDateTime.now());
//        bas.setDayLimit(500.0);
//        bas.setTransLimit(100.0);
//        bas.setActive(true);
//        bas.setUserTypes(List.of(UserType.ROLE_CUSTOMER));
//        userService.addUser(bas);
//
//        // Sander is employee
//        User sander = new User();
//        sander.setUsername("SanderB");
//        sander.setPassword("test123");
//        sander.setFirstname("Sander");
//        sander.setLastname("Baak");
//        sander.setDob(LocalDate.of(2000, 1, 1));
//        sander.setAddress("Testweg 1");
//        sander.setEmail("test1@mail.com");
//        sander.setPhone("+31612345679");
//        sander.setRegisteredOn(OffsetDateTime.now());
//        sander.setDayLimit(500.0);
//        sander.setTransLimit(100.0);
//        sander.setActive(true);
//        sander.setUserTypes(List.of(UserType.ROLE_EMPLOYEE));
//        users.add(sander);
//
//        // Account Bas current
//        Account basCurrent = new Account();
//        basCurrent.setIban("NL01INHO0000000002");
//        basCurrent.setPincode(0000);
//        basCurrent.setAccountType(AccountType.CURRENT);
//        basCurrent.setBalance(1000.0);
//        basCurrent.setAbsLimit(-10.0);
//        basCurrent.setActive(true);
//        basCurrent.setUser(bas);
//        accountService.addAccount(basCurrent);
//
//        // Account Bas savings
//        Account basSavings = new Account();
//        basSavings.setIban("NL01INHO0000000003");
//        basSavings.setPincode(0000);
//        basSavings.setAccountType(AccountType.SAVINGS);
//        basSavings.setBalance(1000.0);
//        basSavings.setAbsLimit(-10.0);
//        basSavings.setActive(true);
//        basSavings.setUser(bas);
//        accountService.addAccount(basSavings);
//
//        // Account Sander current
//        Account sanderCurrent = new Account();
//        sanderCurrent.setIban("NL01INHO0000000004");
//        sanderCurrent.setPincode(0000);
//        sanderCurrent.setAccountType(AccountType.CURRENT);
//        sanderCurrent.setBalance(10.0);
//        sanderCurrent.setAbsLimit(0.0);
//        sanderCurrent.setActive(true);
//        sanderCurrent.setUser(sander);
//
//        // Account Sander savings
//        Account sanderSavings = new Account();
//        sanderSavings.setIban("NL01INHO0000000005");
//        sanderSavings.setPincode(0000);
//        sanderSavings.setAccountType(AccountType.SAVINGS);
//        sanderSavings.setBalance(10.0);
//        sanderSavings.setAbsLimit(0.0);
//        sanderSavings.setActive(true);
//        sanderSavings.setUser(sander);
//        accounts.add(sanderSavings);
//
//        Transaction transBas = new Transaction();
//        transBas.setTimestamp(LocalDate.now());
//        transBas.setTransactionType(TransactionType.REGULAR);
//        transBas.setFrom(basCurrent);
//        transBas.setTo(basSavings.getIban());
//        transBas.setAmount(50.0);
//        transBas.setUserPerforming(basCurrent.getUser().getId());
//        transBas.setPinCode(0000);
//        transactions.add(transBas);
//
//            MvcResult res = mockMvc.perform(post("/login")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content("{\"username\": \"BasH\", \"password\": \"test123\"}"))
//                    .andExpect(status().isOk()
//                    ).andReturn();
//
//            String contentAsString = res.getResponse().getContentAsString();
//
//           TokenDTO tokenDTO = objectMapper.readValue(contentAsString, TokenDTO.class);
//
//            this.jwt = tokenDTO.getToken();
//
//
//        }
//
//    @Test
//    public void createTransaction() throws Exception {
//        mockMvc.perform(post("/BankAPI/transactions")
//                .header("Authorization", "Bearer " + this.jwt)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(String.format("{\"from\": \"%s\", \"to\": \"%s\", \"amount\": \"10.0\"}",testIban1, testIban2)))
//                .andExpect(status().isOk());
//
//        MvcResult res = mockMvc.perform(post("/BankAPI/transactions")
//                        .header("Authorization", "Bearer " + this.jwt)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(String.format("{\"from\": \"%s\", \"to\": \"%s\", \"amount\": \"10.0\"}",testIban1, testIban1)))
//                .andExpect(status().is4xxClientError()).andReturn();
//        System.out.println(res);
//        System.out.println("asd");
//    }
//    @Test
//    public void deleteTransaction() throws Exception {
//        Transaction mockTransaction = createMockTransaction(from, to, user);
//
//        mockMvc.perform(delete("/BankAPI/transactions/" + mockTransaction.getId())
//                .header("Authorization", "Bearer " + this.jwt)
//                .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(status().isOk());
//    }

}
