package ru.maxima.dao;

import org.springframework.stereotype.Component;
import ru.maxima.model.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDao {

    private static Long NEXT_ID = 0L;

    private final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private final String USER = "postgres";
    private final String PASSWORD = "postgres";

    private Connection connection;

    {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(URL,USER,PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*private List<Person> allPeople;
    {
        allPeople = new ArrayList<>();
        allPeople.add(new Person(++NEXT_ID,"Alex",25,"Alex@email"));
        allPeople.add(new Person(++NEXT_ID,"Nicolay",33,"Nicolay@email"));
        allPeople.add(new Person(++NEXT_ID,"George",27,"George@email"));
        allPeople.add(new Person(++NEXT_ID,"Rafael",29,"Rafael@email"));
        allPeople.add(new Person(++NEXT_ID,"Viktor",41,"Victor@email"));
    }*/

    public List<Person> getAllPeople() {

        List<Person> allPeople = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM person";

            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()){
                Person person = new Person();
                person.setId(resultSet.getLong("id"));
                person.setName(resultSet.getString("name"));
                person.setAge(resultSet.getInt("age"));
                person.setEmail(resultSet.getString("email"));
                allPeople.add(person);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allPeople;
    }

    public Person getAllPeopleById(Long id) {

        //return allPeople.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
        return null;
    }

    public void savePerson(Person person) {
        //person.setId(++NEXT_ID);
        //allPeople.add(person);
    }

    public void update(Person personFromForm, Long id) {
        Person person = getAllPeopleById(id);
        person.setName(personFromForm.getName());
        person.setAge(personFromForm.getAge());
        person.setEmail(personFromForm.getEmail());
    }

    public void delete(Long id) {
        //allPeople.removeIf(person -> person.getId().equals(id));
    }
}
