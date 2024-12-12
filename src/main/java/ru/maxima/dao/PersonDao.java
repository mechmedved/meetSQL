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

        Person person = null;
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM person WHERE id = " + id;

            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                person = new Person();
                person.setId(resultSet.getLong("id"));
                person.setName(resultSet.getString("name"));
                person.setAge(resultSet.getInt("age"));
                person.setEmail(resultSet.getString("email"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return person;
    }

    public void savePerson(Person person) {

        Long max = getAllPeople().stream()
                .map(p -> p.getId())
                .max(Long::compareTo)
                .orElse(null);

        try {
            Statement statement = connection.createStatement();
            String sql ="INSERT INTO person (id, name, age, email) VALUES (" +
                                    ++max + ",'" + person.getName() + "'," + person.getAge() + ",'" + person.getEmail() + "')";

            statement.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Person personFromForm, Long id) {
        //update person set name = '', age = 255, email = '' where id = 1;
        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "update person set name = '" + personFromForm.getName() + "', age = " + personFromForm.getAge() + ", email = '" + personFromForm.getEmail() + "' where id = " + id;

            statement.executeUpdate(SQLQuery);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Long id) {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE FROM person WHERE id = " + id;

            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
