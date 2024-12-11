import com.luxoft.bankapp.exceptions.ActiveAccountNotSet;
import com.luxoft.bankapp.model.AbstractAccount;
import com.luxoft.bankapp.model.CheckingAccount;
import com.luxoft.bankapp.model.Client;
import com.luxoft.bankapp.model.SavingAccount;
import com.luxoft.bankapp.service.BankReportService;
import com.luxoft.bankapp.service.BankReportServiceImpl;
import com.luxoft.bankapp.service.Banking;
import com.luxoft.bankapp.service.BankingImpl;
import com.luxoft.bankapp.model.Client.Gender;
import com.luxoft.bankapp.service.storage.ClientRepository;
import com.luxoft.bankapp.service.storage.MapClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan("com.luxoft.bankapp")
@PropertySource("classpath:clients.properties")
public class BankApplication {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Environment environment;

    private static final String[] CLIENT_NAMES =
            {"Jonny Bravo", "Adam Budzinski", "Anna Smith"};

    public static void main(String[] args) {


//        ClientRepository repository = new MapClientRepository();
//        Banking banking = initialize(repository);
//
//        workWithExistingClients(banking);
//
//        bankingServiceDemo(banking);

//        bankReportsDemo(repository);

//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");

//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
//                new String[] {
//                        "application-context.xml",
//                        "test-clients.xml"
//                }
//        );

        ApplicationContext context = new AnnotationConfigApplicationContext(BankApplication.class);

        Banking banking = initialize(context);

        workWithExistingClients(banking);
//        bankingServiceDemo(banking);
        bankingServiceDemo(banking);
        bankReportsDemo(context);


    }

//    public static void bankReportsDemo(ClientRepository repository) {
//
//        System.out.println("\n=== Using BankReportService ===\n");
//
//        BankReportService reportService = new BankReportServiceImpl();
//        reportService.setRepository(repository);
//
//        System.out.println("Number of clients: " + reportService.getNumberOfBankClients());
//
//        System.out.println("Number of accounts: " + reportService.getAccountsNumber());
//
//        System.out.println("Bank Credit Sum: " + reportService.getBankCreditSum());
//    }

    public static void bankReportsDemo(ApplicationContext context){
        System.out.println("\n=== Using BankReportService ===\n");

        BankReportService reportService = (BankReportService) context.getBean(BankReportService.class);

        ClientRepository repository = (ClientRepository) context.getBean(MapClientRepository.class);

        reportService.setRepository(repository);

        System.out.println("Number of clients: " + reportService.getNumberOfBankClients());

        System.out.println("Number of accounts: " + reportService.getAccountsNumber());

        System.out.println("Bank Credit Sum: " + reportService.getBankCreditSum());


    }

    public static void bankingServiceDemo(Banking banking) {

        System.out.println("\n=== Initialization using Banking implementation ===\n");

        Client anna = new Client(CLIENT_NAMES[2], Gender.FEMALE);
        anna = banking.addClient(anna);

        AbstractAccount saving = banking.createAccount(anna, SavingAccount.class);
        saving.deposit(1000);

        banking.updateAccount(anna, saving);

        AbstractAccount checking = banking.createAccount(anna, CheckingAccount.class);
        checking.deposit(3000);

        banking.updateAccount(anna, checking);

        banking.getAllAccounts(anna).stream().forEach(System.out::println);
    }

    public static void workWithExistingClients(Banking banking) {

        System.out.println("\n=======================================");
        System.out.println("\n===== Work with existing clients ======");

        Client jonny = banking.getClient(CLIENT_NAMES[0]);

        try {

            jonny.deposit(5_000);

        } catch (ActiveAccountNotSet e) {

            System.out.println(e.getMessage());

            jonny.setDefaultActiveAccountIfNotSet();
            jonny.deposit(5_000);
        }

        System.out.println(jonny);

        Client adam = banking.getClient(CLIENT_NAMES[1]);
        adam.setDefaultActiveAccountIfNotSet();

        adam.withdraw(1500);

        double balance = adam.getBalance();
        System.out.println("\n" + adam.getName() + ", current balance: " + balance);

        banking.transferMoney(jonny, adam, 1000);

        System.out.println("\n=======================================");
        banking.getClients().forEach(System.out::println);
    }

    /*
     * Method that creates a few clients and initializes them with sample values
     */
//    public static Banking initialize(ClientRepository repository) {
//
//        Banking banking = new BankingImpl();
//        banking.setRepository(repository);
//
//        Client client_1 = new Client(CLIENT_NAMES[0], Gender.MALE);
//
//        AbstractAccount savingAccount = new SavingAccount(1000);
//        client_1.addAccount(savingAccount);
//
//        AbstractAccount checkingAccount = new CheckingAccount(1000);
//        client_1.addAccount(checkingAccount);
//
//        Client client_2 = new Client(CLIENT_NAMES[1], Gender.MALE);
//
//        AbstractAccount checking = new CheckingAccount(1500);
//        client_2.addAccount(checking);
//
//        banking.addClient(client_1);
//        banking.addClient(client_2);
//
//        return banking;
//    }

//    public static Banking initialize(ApplicationContext context){
//        Banking banking = (Banking) context.getBean("banking");
//
//        ClientRepository repository = (ClientRepository) context.getBean("repository");
//
//        BankReportServiceImpl reportService = (BankReportServiceImpl) context.getBean("reportService");
//
//        banking.setRepository(repository);
//
//        reportService.setRepository(repository);
//
//        Client client_1 = new Client(CLIENT_NAMES[0], Gender.MALE);
//
//        AbstractAccount savingAccount = new SavingAccount(1000);
//        client_1.addAccount(savingAccount);
//
//        AbstractAccount checkingAccount = new CheckingAccount(1000);
//        client_1.addAccount(checkingAccount);
//
//        Client client_2 = new Client(CLIENT_NAMES[1], Gender.MALE);
//
//        AbstractAccount checking = new CheckingAccount(1500);
//        client_2.addAccount(checking);
//
//        banking.addClient(client_1);
//        banking.addClient(client_2);
//
//        return  banking;
//    }

//    public static Banking initialize(ApplicationContext context){
//        Banking banking = (Banking) context.getBean("banking");
//
//        ClientRepository repository = (ClientRepository) context.getBean("repository");
//
//        BankReportServiceImpl reportService = (BankReportServiceImpl) context.getBean("reportService");
//
//        banking.setRepository(repository);
//
//        reportService.setRepository(repository);
//
//        Client client_1 = (Client) context.getBean("client1");
//        Client client_2 = (Client) context.getBean("client2");

//        Client client_2 = new Client(CLIENT_NAMES[1], Gender.MALE);
//
//        AbstractAccount checking = new CheckingAccount(1500);
//        client_2.addAccount(checking);

//        banking.addClient(client_1);
//        banking.addClient(client_2);
//
//        System.out.println("Hello from initialize " + client_1.getName());
//
//        return  banking;
//    }

        public static Banking initialize(ApplicationContext context){

        Banking banking = (Banking) context.getBean(BankingImpl.class);

        ClientRepository repository = (ClientRepository) context.getBean(MapClientRepository.class);

        BankReportServiceImpl reportService = (BankReportServiceImpl) context.getBean(BankReportServiceImpl.class);

        banking.setRepository(repository);

        reportService.setRepository(repository);

        Client client_1 = (Client) context.getBean("client1");
        Client client_2 = (Client) context.getBean("client2");

//        AbstractAccount checking = new CheckingAccount(1500);
//        client_2.addAccount(checking);

        banking.addClient(client_1);
        banking.addClient(client_2);


        return  banking;
    }

    @Bean(name="checkingAccount1")
    public CheckingAccount getDemoCheckingAccount1() {
        return new CheckingAccount(1000);
    }

    @Bean(name="savingAccount1")
    public SavingAccount getDemoSavingAccount1() {
        return new SavingAccount(1000);
    }

    @Bean(name = "checkingAccount2")
    public CheckingAccount getDemoCheckingAccount2() {
        return new CheckingAccount(1500);
    }

    @Bean(name = "client1")
    public Client getDemoClient1() {
        String name = environment.getProperty("client1");
        Client client = new Client(name, Gender.MALE);
        client.setCity("Bucharest");
        AbstractAccount checking = (CheckingAccount)
                applicationContext.getBean("checkingAccount1");
        AbstractAccount saving = (SavingAccount) applicationContext.getBean("savingAccount1");
        client.addAccount(checking);
        client.addAccount(saving);
        return client;
    }

    @Bean(name = "client2")
    public Client getDemoClient2() {
        String name = environment.getProperty("client2");
        Client client = new Client(name, Gender.MALE);
        client.setCity("Kiev");
        AbstractAccount checking = (CheckingAccount)
                applicationContext.getBean("checkingAccount2");
        client.addAccount(checking);
        return client;
    }
}
