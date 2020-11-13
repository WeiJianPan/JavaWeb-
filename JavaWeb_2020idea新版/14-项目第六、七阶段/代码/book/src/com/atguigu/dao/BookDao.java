package com.atguigu.dao;

import com.atguigu.pojo.Book;

import java.util.List;

public interface BookDao {


    //添加图书
    public int addBook(Book book);

    //删除图书
    public int deleteBookById(Integer id);

    //更新图书内容
    public int updateBook(Book book);

    //根据图书id查询一条图书数据
    public Book queryBookById(Integer id);

    //返回多条数据
    public List<Book> queryBooks();

    //返回sql语句查询出来的总记录数
    public Integer queryPageTotalCount();

    //返回指定开始索引和索引长度中查询出来的数据。
    public  List<Book> queryForPageItems(Integer begin, int pageSize);

    //返回指定价格区间的count数据总记录数。
    public Integer queryPageTotalCountByPrice(int min, int max);

    //返回指定价格区间的分页实体类数据。
    public List<Book> queryForPageItemsByPrice(int min, int max, Integer begin, int pageSize);
}
