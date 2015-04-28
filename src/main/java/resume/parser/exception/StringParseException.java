package resume.parser.exception;

/**
 * Исключение вызывается в случае если не удается получить значение из строки.
 */
public class StringParseException extends Exception
{
    public StringParseException() {}

    public StringParseException(String message)
    {
        super(message);
    }
}