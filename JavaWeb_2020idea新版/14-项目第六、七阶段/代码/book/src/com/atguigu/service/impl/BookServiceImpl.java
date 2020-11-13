package com.atguigu.service.impl;

import com.atguigu.dao.BookDao;
import com.atguigu.dao.impl.BookDaoImpl;
import com.atguigu.pojo.Book;
import com.atguigu.pojo.Page;
import com.atguigu.service.BookService;

import java.util.List;

public class BookServiceImpl implements BookService {

    private BookDao bookDao = new BookDaoImpl();

    @Override
    public void addBook(Book book) {
        bookDao.addBook(book);
    }

    @Override
    public void updateBook(Book book) {
        bookDao.updateBook(book);
    }

    @Override
    public void deleteBookById(Integer id) {
        bookDao.deleteBookById(id);
    }



    @Override
    public Book queryBookById(Integer id) {
        return bookDao.queryBookById(id);
    }

    @Override
    public List<Book> queryBooks() {
        return bookDao.queryBooks();
    }

    @Override
    public Page page(int pageNo, int pageSize) {
        //创建一个Page对象
        Page<Book> bookPage = new Page<Book>();

        //1.填充总记录数
        Integer pageTotalCount = bookDao.queryPageTotalCount();
        bookPage.setPageTotalCount(pageTotalCount);

        //2. 填充总页码
        // 求总页码
        Integer pageTotal = pageTotalCount / pageSize;
        if (pageTotalCount % pageSize > 0) {
            pageTotal += 1;
        }
        //设置总页码
        bookPage.setPageTotal(pageTotal);


        // 设置当前页码
        bookPage.setPageNo(pageNo);

        //3.填充当前页数据
        // 求当前页数据的开始索引,mysql的litmi初始记录行的偏移量是 0(而不是 1)
        Integer begin = ( bookPage.getPageNo() - 1) * pageSize ;
        // 求当前页数据
        List<Book> books = bookDao.queryForPageItems(begin, pageSize);
        // 设置当前页数据
        bookPage.setItems(books);


        return bookPage;
    }

    @Override
    public Page pageByPrice(int pageNo, int pageSize, int min, int max) {
        Page<Book> bookPage = new Page<>();

        //设置每页数量
        bookPage.setPageSize(pageSize);

        //1.填充总记录数
        Integer pageTotalCountByPrice = bookDao.queryPageTotalCountByPrice(min,max);
        bookPage.setPageTotalCount(pageTotalCountByPrice);
        //2.总页码
        Integer pageTotal = pageTotalCountByPrice / pageSize;
        if (pageTotalCountByPrice % pageSize > 0) {
            pageTotal += 1;
        }
        //设置总页码
        bookPage.setPageTotal(pageTotal);

        // 设置当前页码
        bookPage.setPageNo(pageNo);

        //3.当前页数据
        //设置当前页数据的开始索引
        Integer begin = ( bookPage.getPageNo() - 1) * pageSize ;

        //求当前页数据
        List<Book> books = bookDao.queryForPageItemsByPrice(min, max, begin, pageSize);
        //设置当前页数据
        bookPage.setItems(books);
        return bookPage;
    }
}
