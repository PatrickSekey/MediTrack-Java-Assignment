package com.airtribe.meditrack.util;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataStore<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<String, T> storage;
    private final ReentrantReadWriteLock lock;

    public DataStore() {
        this.storage = new HashMap<>();
        this.lock = new ReentrantReadWriteLock();
    }

    public void add(String id, T item) {
        lock.writeLock().lock();
        try {
            storage.put(id, item);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Optional<T> get(String id) {
        lock.readLock().lock();
        try {
            return Optional.ofNullable(storage.get(id));
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<T> getAll() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(storage.values());
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean remove(String id) {
        lock.writeLock().lock();
        try {
            return storage.remove(id) != null;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void clear() {
        lock.writeLock().lock();
        try {
            storage.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int size() {
        lock.readLock().lock();
        try {
            return storage.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean containsKey(String id) {
        lock.readLock().lock();
        try {
            return storage.containsKey(id);
        } finally {
            lock.readLock().unlock();
        }
    }
}