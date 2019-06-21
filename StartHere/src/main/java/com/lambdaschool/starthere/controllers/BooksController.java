package com.lambdaschool.starthere.controllers;

import com.lambdaschool.starthere.models.Authors;
import com.lambdaschool.starthere.models.Book;
import com.lambdaschool.starthere.models.ErrorDetail;
import com.lambdaschool.starthere.services.AuthorService;
import com.lambdaschool.starthere.services.BookService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BooksController
{
    @Autowired
    BookService bookService;

    @Autowired
    AuthorService authorService;

    @ApiOperation(value = "Return all books and their authors", response = Book.class, responseContainer = "List")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integr", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")})
    @GetMapping(value = "/books", produces = {"application/json"})
    public ResponseEntity<?> listRoles(@PageableDefault(page = 0,
            size = 5)
                                               Pageable pageable)
    {
        List<Book> allBooks = bookService.findAll(pageable);
        return new ResponseEntity<>(allBooks, HttpStatus.OK);
    }

    @ApiOperation(value = "Return all Authors and their books", response = Authors.class, responseContainer = "List")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integr", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")})
    @GetMapping(value = "/authors", produces = {"application/json"})
    public ResponseEntity<?> listAuthors(@PageableDefault(page = 0,
            size = 5)
                                                 Pageable pageable)
    {
        List<Authors> allAuthors = authorService.findAll(pageable);
        return new ResponseEntity<>(allAuthors, HttpStatus.OK);
    }

    @ApiOperation(value = "Updates a Book",
            response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book Updated Successfully", response = void.class),
            @ApiResponse(code = 500, message = "Error updating Book", response = ErrorDetail.class)
    } )
    @PutMapping(value = "/data/books/{id}")
    public ResponseEntity<?> updateBook(@RequestBody
                                                Book updateBook,
                                        @PathVariable
                                                long id)
    {
        bookService.update(updateBook, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Deletes a Book",
            response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Book Deleted Successfully", response = void.class),
            @ApiResponse(code = 500, message = "Error Deleting Book", response = ErrorDetail.class)
    } )
    @DeleteMapping("/data/books/{id}")
    public ResponseEntity<?> deleteBookById(
            @PathVariable
                    long id
    )
    {
        bookService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Adds a book to an author",
            response = void.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Book added Successfully", response = void.class),
            @ApiResponse(code = 500, message = "Error adding book", response = ErrorDetail.class)
    } )
    @PostMapping(value = "/data/{bookid}/authors/{authorid}")
    public ResponseEntity<?> addBookAuthors(@PathVariable long bookid, @PathVariable long authorid)
    {
        bookService.saveAuthorBook(bookid, authorid);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}