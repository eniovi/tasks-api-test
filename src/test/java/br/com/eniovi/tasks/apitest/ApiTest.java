package br.com.eniovi.tasks.apitest;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class ApiTest {

	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://localhost:8001/tasks-backend";
	}

	@Test
	public void shouldAddTasksWithSuccess() {
		RestAssured.given().body("{ \"task\": \"Teste via API\", \"dueDate\": \"2022-12-30\" }")
				.contentType(ContentType.JSON).when().post("/todo").then().statusCode(201);
	}

	@Test
	public void shouldNotAddTasksWithoutDate() {
		RestAssured.given().body("{ \"task\": \"Teste via API\" }").contentType(ContentType.JSON).when().post("/todo")
				.then().statusCode(400).body("message", CoreMatchers.is("Fill the due date"));
	}

	@Test
	public void shouldNotAddTasksWithoutDescription() {
		RestAssured.given().body("{ \"dueDate\": \"2020-12-30\" }").contentType(ContentType.JSON).when().post("/todo")
				.then().statusCode(400).body("message", CoreMatchers.is("Fill the task description"));
	}

	@Test
	public void shouldNotAddTasksWithPastDate() {
		RestAssured.given().body("{ \"task\": \"Teste via API\", \"dueDate\": \"2020-12-30\" }")
				.contentType(ContentType.JSON).when().post("/todo").then().statusCode(400)
				.body("message", CoreMatchers.is("Due date must not be in past"));
	}

	@Test
	public void shouldReturnTasks() {
		RestAssured.given().when().get("/todo").then().statusCode(200);
	}

}
