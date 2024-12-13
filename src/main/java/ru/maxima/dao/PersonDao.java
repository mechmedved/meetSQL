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
                .orElse(0L);

        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO person (id, name, age, email) VALUES (?,?,?,?)");
            preparedStatement.setLong(1, ++max);
            preparedStatement.setString(2, person.getName());
            preparedStatement.setInt(3, person.getAge());
            preparedStatement.setString(4, person.getEmail());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Person personFromForm, Long id) {

        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE person SET name = ?, age = ?, email = ? WHERE id = ?");
            preparedStatement.setString(1, personFromForm.getName());
            preparedStatement.setInt(2, personFromForm.getAge());
            preparedStatement.setString(3, personFromForm.getEmail());
            preparedStatement.setLong(4, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Long id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM person WHERE id = ?");
            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
