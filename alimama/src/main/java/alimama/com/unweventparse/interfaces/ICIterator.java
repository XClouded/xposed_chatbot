package alimama.com.unweventparse.interfaces;

public interface ICIterator {
    void first();

    ICommand getCurrentCommand();

    boolean hasNext();

    boolean isFirst();

    boolean isLast();

    void next();
}
