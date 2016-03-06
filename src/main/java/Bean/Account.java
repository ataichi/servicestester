package Servlet;

/**
 *
 * @author nievabri
 */
public class Account {

    private String fullname;
    private String accountno;
    private String email;
    private String password;
    private String currency;
    private String currentbalance;
    private String availablebalance;
	private int theresareturnedvalue;
	
	public int getTheresareturnedvalue() {
        return theresareturnedvalue;
    }

    public void setTheresareturnedvalue(int theresareturnedvalue) {
        this.theresareturnedvalue = theresareturnedvalue;
    }
	
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAccountno() {
        return accountno;
    }

    public void setAccountno(String accountno) {
        this.accountno = accountno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrentbalance() {
        return currentbalance;
    }

    public void setCurrentbalance(String currentbalance) {
        this.currentbalance = currentbalance;
    }

    public String getAvailablebalance() {
        return availablebalance;
    }

    public void setAvailablebalance(String availablebalance) {
        this.availablebalance = availablebalance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
