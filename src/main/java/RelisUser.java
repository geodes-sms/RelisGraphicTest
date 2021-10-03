import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelisUser {

    // relis user fields
    private String full_name;
    private String username;
    private String user_email;

    private String password;
    private String user_usergroup;
    private String creation_time;
    private String create_by;
    private String user_state;
    private String user_active;


    /**
     * Redefinition of the equals method
     * in order to compare two relis user
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o){
        RelisUser user = (RelisUser) o;
        if(user != null){

            return user.username.equals(this.username)
                    && user.password.equals(this.password)
                    && user.user_email.equals(this.user_email);
        }
        return false;
    }

    /**
     * redifintion of the toString method for pretty print of RelisUser
     * @return
     */
    @Override
    public String toString(){

        return "fullName: " + full_name +" , user_name: " + username
                +" user_email " + user_email +" password: "+password +" usergroup " + user_usergroup;

    }


    /**
     * this procedure return the sql code
     *in order to insertion users into the relis user databses
     *
     * @return
     */
    public String getRelisUserDBFormatSQL(){

        // we encript the user password using MD5 as relis is using.
        String encriptedPassword = Utility.encriptWiht_MD5(password);
        // return the specific format for the insertion
        return String.format("(\"%s\",\"%s\",\"%s\",%s,\"%s\",\"%s\",%s,%s,%s)", full_name,username,user_email,
                user_usergroup,encriptedPassword,creation_time,create_by,user_state,user_active);

    }

}

