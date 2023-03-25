package com.example.translatorapp.service;

import com.example.translatorapp.model.Request;
import com.example.translatorapp.repository.RequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class RequestService implements RequestRepository {

    private final DataSource dataSource;
    private static Connection connection;
    private static PreparedStatement preparedStatement;

    public RequestService(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    @Transactional
    public void save(Request request) {
        String insertRequestQuery = "INSERT INTO REQUEST (REQUESTIP, EXECUTETIME, TRANSLATEPARAM) VALUES (?, ?, ?);";

        try {
            connection = dataSource.getConnection();
            preparedStatement =
                    connection.prepareStatement(insertRequestQuery);

            preparedStatement.setString(1, request.getREQUESTIP());
            preparedStatement.setTime(2, Time.valueOf(request.getEXECUTETIME()));
            preparedStatement.setString(3, request.getTRANSLATEPARAM());

            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                connection.close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        String selectIDRequestQuery = "SELECT (ID) FROM REQUEST ORDER BY ID DESC LIMIT 1;";
        long lastInsertedID = 0;
        try {
            connection = dataSource.getConnection();
            preparedStatement =
                    connection.prepareStatement(selectIDRequestQuery);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                lastInsertedID = resultSet.getLong("ID");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                connection.close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        String insertInputData = "INSERT INTO INPUTDATA (SOURCE, REQUESTID) VALUES (?, ?);";
        String insertOutputData = "INSERT INTO OUTPUTDATA (SOURCE, REQUESTID) VALUES (?, ?);";

        if (request.getInputWords() != null) {
            for (String word : request.getInputWords()) {
                saveEachWord(word, insertInputData, lastInsertedID);
            }
        }
        if (request.getOutputWords() != null) {
            for (String word : request.getOutputWords()) {
                saveEachWord(word, insertOutputData, lastInsertedID);
            }
        }

    }

    private void saveEachWord(String data, String query, Long id) {
        try {
            connection = dataSource.getConnection();
            preparedStatement =
                    connection.prepareStatement(query);

            preparedStatement.setString(1, data);
            preparedStatement.setLong(2, id);

            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                connection.close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public Request getById(Long id) {
        String query = "SELECT * FROM REQUEST WHERE ID = ?;";
        Request request = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement =
                    connection.prepareStatement(query);

            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                request = new Request();

                request.setID(resultSet.getLong("ID"));
                request.setREQUESTIP(resultSet.getString("REQUESTIP"));
                request.setEXECUTETIME(resultSet.getTime("EXECUTETIME").toLocalTime());
                request.setTRANSLATEPARAM(resultSet.getString("TRANSLATEPARAM"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return request;
    }

    @Override
    public void update(Long id, Request updatedRequest) {
        String query = "UPDATE REQUEST SET REQUESTIP = ?, EXECUTETIME = ?, TRANSLATEPARAM = ? WHERE ID = ?;";

        try {
            connection = dataSource.getConnection();
            preparedStatement =
                    connection.prepareStatement(query);

            preparedStatement.setString(1, updatedRequest.getREQUESTIP());
            preparedStatement.setTime(2, Time.valueOf(updatedRequest.getEXECUTETIME()));
            preparedStatement.setString(3, updatedRequest.getTRANSLATEPARAM());
            preparedStatement.setLong(4, id);

            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(Long id) {
        String query = "DELETE FROM REQUEST WHERE ID = ?;";

        try {
            connection = dataSource.getConnection();
            preparedStatement =
                    connection.prepareStatement(query);

            preparedStatement.setLong(1, id);

            int out = preparedStatement.executeUpdate();
            if (out != 0) {
                log.info("Request delete with id " + id);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Request> getAll() {
        List<Request> requestList = new ArrayList<>();
        String query = "SELECT * FROM REQUEST;";

        try {
            connection = dataSource.getConnection();
            preparedStatement =
                    connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Request request = new Request();

                request.setID(resultSet.getLong("ID"));
                request.setREQUESTIP(resultSet.getString("REQUESTIP"));
                request.setEXECUTETIME(resultSet.getTime("EXECUTETIME").toLocalTime());
                request.setTRANSLATEPARAM(resultSet.getString("TRANSLATEPARAM"));

                requestList.add(request);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return requestList;
    }
}
