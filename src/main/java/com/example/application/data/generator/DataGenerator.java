package com.example.application.data.generator;

import com.example.application.data.entity.*;
import com.example.application.data.repository.*;
import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(UserRepository userRepository, SeminarRepository seminarRepository) {

        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (userRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            logger.info("Generating demo data");
            User user = new User();
            user.setUsername("riki");
            user.setEmail("riki@gmail.com");
            user.setPassword("rikiachmad123");
            user.setRole("ADMIN");
            userRepository.save(user);

            User user2 = new User();
            user2.setUsername("zea");
            user2.setEmail("zea@gmail.com");
            user2.setPassword("zea123123");
            user2.setRole("USER");
            userRepository.save(user2);


            Seminar seminar = new Seminar();
            seminar.setName("Belajar Spring Framework Programming Bersama Riki Mi'roj Achmad dan Kirana Zea Sachdania Mayz");
            seminar.setDate(LocalDateTime.now());
            seminar.setAddress("Jalan Taman Pinang Indah D3/24, Sidoarjo, Sidoarjo");
            seminar.setDescription("Pada seminar ini, peserta akan diajari bagaimana cara membuat sebuah aplikasi berbasis web dengan menggunakan teknologi spring boot dan framework spring yang disediakan" +
                    "java");
            seminar.setLink("Https://instagram.com/dearsycko");
            seminar.setPrice(100000);
            seminar.setStatus("Offline");
            seminarRepository.save(seminar);

            Seminar seminar1 = new Seminar();
            seminar1.setName("Belajar Backend Programming Dengan Menggunakan Teknologi Laravel Framework");
            seminar1.setDate(LocalDateTime.now());
            seminar1.setAddress("Jalan Malang 1/24, Malang, Malang, JawaTimur");
            seminar1.setDescription("Seminar ini diperuntukkan bagi kalian yang ingin menjadi seorang Software Engineer khususnya Backend Developer.");
            seminar1.setLink("Https://instagram.com/rikiachmd");
            seminar1.setPrice(200000);
            seminar1.setStatus("Online");
            seminarRepository.save(seminar1);

//
//            for (int i = 2; i<10; i++){
//                Seminar seminar2 = new Seminar();
//                seminar2.setName("Tes Seminar " + i);
//                seminar2.setDate(LocalDateTime.now());
//                seminar2.setAddress("Tes Address " + i);
//                seminar2.setDescription("Tes Description " + i);
//                seminar2.setLink("Tes Link " + i);
//                seminar2.setPrice(10000 + i);
//                seminar2.setStatus("Offline");
//                seminarRepository.save(seminar2);
//            }

        };
    }


//    @Bean
//    public CommandLineRunner loadData(ContactRepository contactRepository, CompanyRepository companyRepository,
//            StatusRepository statusRepository) {
//
//        return args -> {
//            Logger logger = LoggerFactory.getLogger(getClass());
//            if (contactRepository.count() != 0L) {
//                logger.info("Using existing database");
//                return;
//            }
//            int seed = 123;
//
//            logger.info("Generating demo data");
//            ExampleDataGenerator<Company> companyGenerator = new ExampleDataGenerator<>(Company.class,
//                    LocalDateTime.now());
//            companyGenerator.setData(Company::setName, DataType.COMPANY_NAME);
//            List<Company> companies = companyRepository.saveAll(companyGenerator.create(5, seed));
//
//            List<Status> statuses = statusRepository
//                    .saveAll(Stream.of("Imported lead", "Not contacted", "Contacted", "Customer", "Closed (lost)")
//                            .map(Status::new).collect(Collectors.toList()));
//
//            logger.info("... generating 50 Contact entities...");
//            ExampleDataGenerator<Contact> contactGenerator = new ExampleDataGenerator<>(Contact.class,
//                    LocalDateTime.now());
//            contactGenerator.setData(Contact::setFirstName, DataType.FIRST_NAME);
//            contactGenerator.setData(Contact::setLastName, DataType.LAST_NAME);
//            contactGenerator.setData(Contact::setEmail, DataType.EMAIL);
//
//            Random r = new Random(seed);
//            List<Contact> contacts = contactGenerator.create(50, seed).stream().map(contact -> {
//                contact.setCompany(companies.get(r.nextInt(companies.size())));
//                contact.setStatus(statuses.get(r.nextInt(statuses.size())));
//                return contact;
//            }).collect(Collectors.toList());
//
//            contactRepository.saveAll(contacts);
//
//            logger.info("Generated demo data");
//        };
//    }

}
