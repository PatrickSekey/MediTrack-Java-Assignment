package com.airtribe.meditrack.interfaces;

import java.util.List;
import java.util.Optional;

public interface Searchable<T> {
    Optional<T> searchById(String id);
    List<T> searchByName(String name);
    List<T> getAll();
}