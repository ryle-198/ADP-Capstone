package za.ac.cput.repository;

import za.ac.cput.domain.Customer;

import java.util.Set;

public interface IRepository <T, ID> {
    //create
    T create(T t);
    //read
    T read(ID id);
    //update
    T update(T t);
    //delete
    boolean delete(ID id);


}

