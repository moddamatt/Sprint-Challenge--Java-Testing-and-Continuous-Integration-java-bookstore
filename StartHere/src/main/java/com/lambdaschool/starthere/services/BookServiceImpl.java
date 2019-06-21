package com.lambdaschool.starthere.services;

import com.lambdaschool.starthere.models.Book;
import com.lambdaschool.starthere.repository.BookRepository;
import org.hibernate.event.spi.EntityCopyObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service(value = "bookService")
public class BookServiceImpl implements BookService
{
    @Autowired
    private BookRepository bookRepos;

    @Override
    public List<Book> findAll(Pageable pageable)
    {
        List<Book> list = new ArrayList<>();
        bookRepos.findAll(pageable).iterator().forEachRemaining(list::add);
        return list;
    }

    @Transactional
    @Override
    public Book update(Book book, long id)
    {
        Book currentBook = bookRepos.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Long.toString(id)));
        if (book.getBooktitle() != null)
        {
            currentBook.setBooktitle(book.getBooktitle());
        }
        if (book.getCopy() != 0)
        {
            currentBook.setCopy(book.getCopy());
        }
        if (book.getISBN() != null)
        {
            currentBook.setISBN(book.getISBN());
        }
        return bookRepos.save(currentBook);
    }

    @Override
    public void delete(long id) throws EntityNotFoundException
    {
        if (bookRepos.findById(id).isPresent())
        {
            bookRepos.deleteById(id);
        } else
        {
            throw new EntityNotFoundException(Long.toString(id));
        }
    }

    @Transactional
    @Override
    public void saveAuthorBook(long bookid, long authorid)
    {
        bookRepos.insertAuthorBooks(bookid, authorid);
    }
}
