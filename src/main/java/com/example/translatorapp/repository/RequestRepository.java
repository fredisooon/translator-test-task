package com.example.translatorapp.repository;

import com.example.translatorapp.model.Request;

import java.util.List;

public interface RequestRepository {

    void save(Request employee);

    Request getById(Long id);

    void update(Long id, Request updatedRequest);

    void deleteById(Long id);

    List<Request> getAll();
}
