package com.example.garage;

import com.example.garage.model.Role;
import com.example.garage.repository.RoleRepository;
import com.example.garage.repository.RoleRepositoryClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.PersistenceException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GarageApplicationTests {

	@Test
	public void contextLoads() {
	}

}

