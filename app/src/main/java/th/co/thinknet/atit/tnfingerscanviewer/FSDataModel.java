package th.co.thinknet.atit.tnfingerscanviewer;

/**
 * Created by atit on 7/15/14 AD.
 */
public class FSDataModel {
    private String username;
    private String password;

    public FSDataModel(){

    }

    public FSDataModel(String username,String password){
        this.username = username;
        this.password = password;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }
}
