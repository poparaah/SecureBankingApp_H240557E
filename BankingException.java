package BankingApp;

public class BankingException extends Exception
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BankingException(String message) {
        super(message);
    }
    
    public BankingException(String message, Throwable cause) {
        super(message, cause);
    }
}