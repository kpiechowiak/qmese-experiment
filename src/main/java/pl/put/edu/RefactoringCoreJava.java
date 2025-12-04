package pl.put.edu;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//Tomek to pisał, na pewno jest dobrze
public class RefactoringCoreJava {

    public static void main(String[] args) throws InterruptedException {
        Library library = new Library("Central City Library");

        library.addBook(new Book("978-0134685991", "Effective Java", "Joshua Bloch", 2018));
        library.addBook(new Book("978-0201633610", "Design Patterns", "Erich Gamma", 1994));
        library.addBook(new Book("978-0132350884", "Clean Code", "Robert C. Martin", 2008));
        library.addBook(new Book("978-1491950357", "Designing Data-Intensive Applications", "Martin Kleppmann", 2017));
        library.addBook(new Book("978-0262033848", "Introduction to Algorithms", "Cormen, Leiserson, Rivest, Stein", 2009));

        Member firstMember = library.registerMember("Alice");
        Member secondMember = library.registerMember("Bob");

        System.out.println("== Initial Library State ==");
        System.out.println(library);

        library.checkout(firstMember.getId(), "978-0134685991");
        Thread.sleep(10);
        library.checkout(secondMember.getId(), "978-0201633610");

        System.out.println("\n== After Two Checkouts ==");
        System.out.println(library);

        Optional<Loan> maybeLoan = library.findLoan(firstMember.getId(), "978-0134685991");
        if (maybeLoan.isPresent()) {
            Loan loan = maybeLoan.get();
            loan.setLoanDate(LocalDate.now().minusDays(40));
        }

        System.out.println("\n== Overdue Loans ==");
        for (Loan l : library.getOverdueLoans()) {
            System.out.println(l);
        }

        library.returnBook(firstMember.getId(), "978-0134685991");
        System.out.println("\n== After Return ==");
        System.out.println(library.searchByTitle("Design"));

        library.checkout(firstMember.getId(), "000-0000000000");

        System.out.println("\n== Top Borrowed Books ==");
        for (Book b : library.topBorrowed(3)) {
            System.out.println(b.getTitle() + " - borrowed " + b.getTimesBorrowed() + " times");
        }
    }
}

class Library {
    private final String name;
    private final List<Book> books = new ArrayList<>();
    private final List<Member> members = new ArrayList<>();
    private final List<Loan> loans = new ArrayList<>();

    public Library(String name) {
        this.name = name;
    }

    public void addBook(Book b) {
        books.add(b);
    }

    public Member registerMember(String fullName) {
        Member m = new Member(fullName);
        members.add(m);
        return m;
    }

    public boolean checkout(UUID memberId, String isbn) {
        Member member = findMemberById(memberId);
        if (member == null) {
            System.out.println("Member not found: " + memberId);
            return false;
        }
        Book book = findBookByIsbn(isbn);
        if (book == null) {
            System.out.println("Book not found: " + isbn);
            return false;
        }
        if (!book.isAvailable()) {
            System.out.println("Book currently not available: " + book.getTitle());
            return false;
        }
        Loan loan = new Loan(member, book);
        loans.add(loan);
        book.setAvailable(false);
        book.incrementTimesBorrowed();
        System.out.println(member.getFullName() + " checked out '" + book.getTitle() + "'");
        return true;
    }

    public boolean returnBook(UUID memberId, String isbn) {
        Optional<Loan> loanOpt = findLoan(memberId, isbn);
        if (!loanOpt.isPresent()) {
            System.out.println("No loan found for member " + memberId + " and book " + isbn);
            return false;
        }
        Loan loan = loanOpt.get();
        loans.remove(loan);
        Book book = loan.getBook();
        book.setAvailable(true);
        System.out.println(loan.getMember().getFullName() + " returned '" + book.getTitle() + "'");
        return true;
    }

    public Optional<Loan> findLoan(UUID memberId, String isbn) {
        for (Loan l : loans) {
            if (l.getMember().getId().equals(memberId) && l.getBook().getIsbn().equals(isbn)) {
                return Optional.of(l);
            }
        }
        return Optional.empty();
    }

    public List<Loan> getOverdueLoans() {
        List<Loan> result = new ArrayList<>();
        for (Loan l : loans) {
            if (l.isOverdue()) {
                result.add(l);
            }
        }
        return result;
    }

    public List<Book> searchByTitle(String query) {
        List<Book> result = new ArrayList<>();
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(query.toLowerCase())) {
                result.add(b);
            }
        }
        return result;
    }

    public Book findBookByIsbn(String isbn) {
        for (Book b : books) {
            if (b.getIsbn().equals(isbn)) {
                return b;
            }
        }
        return null;
    }

    public Member findMemberById(UUID id) {
        for (Member m : members) {
            if (m.getId().equals(id)) {
                return m;
            }
        }
        return null;
    }

    public List<Book> topBorrowed(int n) {
        List<Book> copy = new ArrayList<>(books);
        copy.sort(Comparator.comparingInt(Book::getTimesBorrowed).reversed());
        if (n >= copy.size()) {
            return copy;
        }
        return copy.subList(0, n);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Library: ").append(name).append('\n');
        sb.append("Books: ").append(books.size()).append('\n');
        sb.append("Members: ").append(members.size()).append('\n');
        sb.append("Active Loans: ").append(loans.size()).append('\n');
        return sb.toString();
    }
}

class Book {
    private final String isbn;
    private final String title;
    private final String author;
    private final int year;
    private boolean available = true;
    private int timesBorrowed = 0;

    public Book(String isbn, String title, String author, int year) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.year = year;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public int getTimesBorrowed() { return timesBorrowed; }
    public void incrementTimesBorrowed() { this.timesBorrowed++; }

    @Override
    public String toString() {
        return title + " (" + isbn + ") by " + author + " [" + year + "]";
    }
}

class Member {
    private final UUID id = UUID.randomUUID();
    private final String fullName;

    public Member(String fullName) {
        this.fullName = fullName;
    }

    public UUID getId() { return id; }
    public String getFullName() { return fullName; }

    @Override
    public String toString() {
        return fullName + " (" + id + ")";
    }
}

class Loan {
    private final UUID id = UUID.randomUUID();
    private final Member member;
    private final Book book;
    private LocalDate loanDate;
    private LocalDate dueDate;

    private static final int LOAN_PERIOD_DAYS = 21;

    public Loan(Member member, Book book) {
        this.member = member;
        this.book = book;
        this.loanDate = LocalDate.now();
        this.dueDate = loanDate.plusDays(LOAN_PERIOD_DAYS);
    }

    public UUID getId() { return id; }
    public Member getMember() { return member; }
    public Book getBook() { return book; }
    public LocalDate getLoanDate() { return loanDate; }
    public LocalDate getDueDate() { return dueDate; }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
        this.dueDate = loanDate.plusDays(LOAN_PERIOD_DAYS);
    }

    public boolean isOverdue() {
        return LocalDate.now().isAfter(dueDate);
    }

    public long daysOverdue() {
        if (!isOverdue()) return 0;
        return ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Loan[").append(id).append("]: ");
        sb.append(member.getFullName()).append(" -> ").append(book.getTitle());
        sb.append(" (loaned: ").append(loanDate).append(", due: ").append(dueDate).append(")");
        if (isOverdue()) {
            sb.append(" OVERDUE by ").append(daysOverdue()).append(" days");
        }
        return sb.toString();
    }
}