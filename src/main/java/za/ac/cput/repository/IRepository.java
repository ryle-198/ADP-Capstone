package za.ac.cput.repository;

public interface IRepository <T, ID> {
    //create
    T create(T t);
    //read
    T read(ID id);
    //update
    T update(T t);
    //delete
    boolean delete(ID id);
    //getAll
}

